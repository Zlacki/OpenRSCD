/*
 * player.c
 *
 * Copyright (C) 2011,2012,2013, Zach Knight <zach@libslack.so>
 *
 * Permission to use, copy, modify, and/or distribute this software
 * for any purpose with or without fee is hereby granted, provided
 * that the above copyright notice and this permission notice appear
 * in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL
 * WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL
 * THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 * FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF
 * CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT
 * OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include "packetcreator.h"
#include "player.h"
#include "object.h"
#include "util.h"

Player *player_create(struct ev_loop *loop, struct ev_io *watcher, unsigned int socket, unsigned int index)
{
	Player *player = safe_alloc(sizeof(Player));
	player->loop = loop;
	player->watcher = watcher;
	player->socket = socket;
	player->index = index;
	player->x = 0;
	player->y = 0;
	player->walking_waypoints = list_create();
	return player;
}

void player_connect(Player *player, Packet *packet)
{
	unsigned int version = packet_read_short(packet);
	if(version != 202) {
		send_login_response(player, 5);
		return;
	}

	char *username = hash_to_username(packet_read_long(packet));
	char *password = packet_read_string(packet);
	str_trim(password);
	player->username = username;
	free(password);
	player->region = NULL;
	player->direction = 1;
	for(int i = 0; i < 4; i++)
		player->social_options[i] = 0;
	for(int i = 0; i < 3; i++)
		player->client_options[i] = 0;
	player->inventory = inventory_initialize();
	inventory_add_item(player->inventory, item_create(81, 1));
	inventory_add_item(player->inventory, item_create(581, 1));
	inventory_add_item(player->inventory, item_create(10, 20));
	for(int i = 0; i < 5; i++)
		player->bonuses[i] = 1;
	player->worn_items[0] = 1;
	player->worn_items[1] = 2;
	player->worn_items[2] = 3;
	for(int i = 3; i < 12; i++)
		player->worn_items[i] = 0;
	for(int i = 0; i < 18; i++)
		if(i == 3)
			player->skills[i] = 1145L;
		else
			player->skills[i] = 0L;

	printf("Registering player: '%s'\n", player->username);
	send_login_response(player, 0);
	send_constants(player);
	send_client_options(player);
	send_server_message(player, "Welcome to OpenRSCD!");
	send_stats(player);
	send_inventory(player);
	send_equipment_bonuses(player);
	player_set_location(player, 216 - (random() % 5), 453 - (random() % 5));
	return;
}

void player_destroy(Player *player)
{
	printf("Unregistering player: '%s'\n", player->username);
	for(int i = 0; i < player->inventory->index; i++)
		free(player->inventory->items[i]);
	free(player->inventory);
	region_remove_player(player->region, player->index);
	player->region = NULL;
	free(player->username);
	players[player->index] = NULL;
	client_destroy(player->loop, player->watcher);
	close(player->socket);
	free(player);

	return;
}

void player_disconnect(Player *player)
{
	if(player != NULL) {
		player_packet_send(player, packet_create(222));
		player_destroy(player);
	}
	return;
}

void player_packet_send(Player *player, Packet *packet)
{
	if(packet->id == 255) {
		if(write(player->socket, packet->buffer, packet->offset + 1) < 0) {
			warning("Player sent less bytes than encoded for; disconnecting.");
			packet_destroy(packet);
			player_destroy(player);
			return;
		}
	} else {
		int len = packet->offset + 1;
		char *buffer = safe_alloc(len + 4);

		buffer[0] = (char) (len >> 8);
		buffer[1] = (char) len;
		buffer[2] = packet->id;

		memcpy(buffer + 3, packet->buffer, len);

		if(write(player->socket, buffer, packet->offset + 3) < 0) {
			warning("Player sent less bytes than encoded for; disconnecting.");
			free(buffer);
			packet_destroy(packet);
			player_destroy(player);
			return;
		}
		free(buffer);
	}
	packet_destroy(packet);
	return;
}

char player_within_range(Player *player, int x, int y)
{
	int xd = player->x - x;
	int yd = player->y - y;
	return xd <= 16 && xd >= -15 && yd <= 16 && yd >= -15;
}

void player_equip_item(Player *player, Item *item)
{
	ItemDefinition *def = item_definitions[item->id];
	if(def->wearable) {
		player->worn_items[def->wearable_slot] = def->sprite;
		player->bonuses[0] += def->armor_bonus;
		player->bonuses[1] += def->weapon_aim;
		player->bonuses[2] += def->weapon_power;
		player->bonuses[3] += def->magic_bonus;
		player->bonuses[4] += def->prayer_bonus;
		item->worn = 1;
		send_equipment_bonuses(player);
		send_update_item(player, inventory_get_item(player->inventory, item->id));
		region_send_player_appearance_update(player->index);
	}

	return;
}

void player_remove_item(Player *player, Item *item)
{
	ItemDefinition *def = item_definitions[item->id];
	player->worn_items[def->wearable_slot] = 0;
	player->bonuses[0] -= def->armor_bonus;
	player->bonuses[1] -= def->weapon_aim;
	player->bonuses[2] -= def->weapon_power;
	player->bonuses[3] -= def->magic_bonus;
	player->bonuses[4] -= def->prayer_bonus;
	item->worn = 0;
	send_equipment_bonuses(player);
	send_update_item(player, inventory_get_item(player->inventory, item->id));
	region_send_player_appearance_update(player->index);

	return;
}

void player_set_location(Player* player, int x, int y)
{

	player->x = x;
	player->y = y;

	Region *r = get_region(x, y);
	if(r != player->region) {
		if(player->region)
			region_remove_player(player->region, player->index);
		player->region = r;
		region_add_player(player->region, player->index);
	} else {
		region_send_player_position_update(player->index);
		region_send_player_appearance_update(player->index);
	}

	update_ground_items(player);
	update_objects(player);

	return;
}

List *player_get_regional_items(Player *player)
{
	Region **surrounding_regions = region_get_surrounding_regions(player->x, player->y);

	List *items_in_regional_area = list_create();
	for(int i = 0; i < 4; i++) {
		Region *region = surrounding_regions[i];
		for(Node *node = region->items->first; node; node = node->next) {
			GroundItem *item_in_regional_area = (GroundItem*) node->val;
			if(player_within_range(player, item_in_regional_area->x, item_in_regional_area->y))
				list_insert(items_in_regional_area, item_in_regional_area);
		}
	}

	free(surrounding_regions);

	return items_in_regional_area;
}

List *player_get_regional_objects(Player *player)
{
	Region **surrounding_regions = region_get_surrounding_regions(player->x, player->y);

	List *objects_in_regional_area = list_create();
	for(int i = 0; i < 4; i++) {
		Region *region = surrounding_regions[i];
		for(Node *node = region->objects->first; node; node = node->next) {
			Object *object_in_regional_area = (Object*) node->val;
			if(player_within_range(player, object_in_regional_area->x, object_in_regional_area->y))
				list_insert(objects_in_regional_area, object_in_regional_area);
		}
	}

	free(surrounding_regions);

	return objects_in_regional_area;
}

List *player_get_regional_players(Player *player)
{
	Region **surrounding_regions = region_get_surrounding_regions(player->x, player->y);

	List *players_in_regional_area = list_create();
	for(int i = 0; i < 4; i++) {
		Region *region = surrounding_regions[i];
		for(Node *node = region->players->first; node; node = node->next) {
			Player *player_in_regional_area = (Player*)node->val;
			if(player_within_range(player, player_in_regional_area->x, player_in_regional_area->y))
				list_insert(players_in_regional_area, player_in_regional_area);
		}
	}

	free(surrounding_regions);

	return players_in_regional_area;
}
