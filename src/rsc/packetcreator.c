/*
 * packetcreator.c
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
#include "packetcreator.h"
#include "packet.h"
#include "region.h"
#include "util.h"

void update_player_positions(Player *player)
{
	Packet *packet = packet_create(145);
	packet_add_bits(packet, player->x, 11);
	packet_add_bits(packet, player->y, 13);
	packet_add_bits(packet, player->direction, 4);

	/* TODO: Minimize packet size when player doesn't need updating. */

	for(Node *node = player_get_regional_players(player)->first; node; node = node->next) {
		Player *player_to_update = (Player*)node->val;
		if(player_to_update->index != player->index && player_within_range(player, player_to_update->x, player_to_update->y)) {
			packet_add_bits(packet, player_to_update->index, 11);
			int x_offset = player_to_update->x - player->x;
			int y_offset = player_to_update->y - player->y;
			packet_add_bits(packet, x_offset < 0 ? x_offset + 32 : x_offset, 5);
			packet_add_bits(packet, x_offset < 0 ? y_offset + 32 : y_offset, 5);
			packet_add_bits(packet, player_to_update->direction, 4);
		}
	}

	player_packet_send(player, packet);
	return;
}

void update_player_appearances(Player *player)
{
	Packet *packet = packet_create(53);
	packet_add_short(packet, player_get_regional_players(player)->size);

	for(Node *node = player_get_regional_players(player)->first; node; node = node->next) {
		Player *player_to_update = (Player*)node->val;
		if(player_within_range(player, player_to_update->x, player_to_update->y)) {
			packet_add_short(packet, player_to_update->index);
			packet_add_byte(packet, 5); /* Appearance update or chat */
			packet_add_long(packet, username_to_hash(player_to_update->username));
			for(int i = 0; i < 12; i++)
				packet_add_byte(packet, player_to_update->worn_items[i]);
			packet_add_byte(packet, 0); /* hair color */
			packet_add_byte(packet, 0); /* top color */
			packet_add_byte(packet, 0); /* pant color */
			packet_add_byte(packet, 0); /* skin color */
			packet_add_byte(packet, 3); /* combat level */
			packet_add_byte(packet, 0); /* skulled */
		}
	}

	player_packet_send(player, packet);
	return;
}

void update_ground_items(Player *player)
{
	Packet *packet = packet_create(109);
	List *ground_items = player_get_regional_items(player);

	for(Node *node = ground_items->first; node; node = node->next) {
		GroundItem *ground_item = (GroundItem*)node->val;
		if(player_within_range(player, ground_item->x, ground_item->y)) {
			packet_add_short(packet, ground_item->id);
			packet_add_byte(packet, ground_item->x - player->x);
			packet_add_byte(packet, ground_item->y - player->y);
		} else {
			packet_add_short(packet, ground_item->id + 0x8000);
			packet_add_byte(packet, ground_item->x - player->x);
			packet_add_byte(packet, ground_item->y - player->y);
		}
	}

	free(ground_items);
	player_packet_send(player, packet);
	return;
}

void update_objects(Player *player)
{
	Packet *packet = packet_create(27);
	List *objects = player_get_regional_objects(player);

	for(Node *node = objects->first; node; node = node->next) {
		Object *object = (Object*)node->val;
		if(player_within_range(player, object->x, object->y)) {
			packet_add_integer(packet, object->id);
			packet_add_byte(packet, object->x - player->x);
			packet_add_byte(packet, object->y - player->y);
		} else {
			packet_add_integer(packet, 60000);
			packet_add_byte(packet, object->x - player->x);
			packet_add_byte(packet, object->y - player->y);
		}
	}

	free(objects);
	player_packet_send(player, packet);
	return;
}

void send_login_response(Player *player, char res)
{
	Packet *packet = packet_create(-1);
	packet_add_byte(packet, res);
	player_packet_send(player, packet);
	return;
}

void send_constants(Player *player)
{
	Packet *packet = packet_create(131);
	packet_add_short(packet, player->index);
	packet_add_short(packet, 2304); /* TODO: Name these magic numbers */
	packet_add_short(packet, 1776);
	packet_add_short(packet, 0);
	packet_add_short(packet, MAX_WIDTH);
	player_packet_send(player, packet);
	return;
}

void send_client_options(Player *player)
{
	Packet *packet = packet_create(158);

	for(int i = 0; i < 3; i++)
		packet_add_byte(packet, player->social_options[i]);
	player_packet_send(player, packet);

	packet = packet_create(152);
	for(int i = 0; i < 2; i++)
		packet_add_byte(packet, player->client_options[i]);
	player_packet_send(player, packet);
	return;
}

void send_welcome_box(Player *player)
{
	Packet *packet = packet_create(248);
	packet_add_byte(packet, 127);
	packet_add_byte(packet, 0);
	packet_add_byte(packet, 0);
	packet_add_byte(packet, 1);
	packet_add_short(packet, 24);
	packet_add_byte(packet, 0);
	packet_add_short(packet, 0);
	player_packet_send(player, packet);
	return;
}

void send_server_message(Player *player, char *msg)
{
	Packet *packet = packet_create(48);

	char c;
	while((c = *msg++) != '\0')
		packet_add_byte(packet, c);

	player_packet_send(player, packet);
	return;
}

void send_stats(Player *player)
{
	Packet *packet = packet_create(180);

	for(int i = 0; i < 18; i++)
		if(i == 3)
			packet_add_byte(packet, (char) 10);
		else
			packet_add_byte(packet, (char) 1);

	for(int i = 0; i < 18; i++)
		if(i == 3)
			packet_add_byte(packet, (char) 10);
		else
			packet_add_byte(packet, (char) 1);

	for(int i = 0; i < 18; i++)
		packet_add_integer(packet, player->skills[i]);

	packet_add_byte(packet, (char)0);

	player_packet_send(player, packet);
	return;
}

void send_inventory(Player *player)
{
	Packet *packet = packet_create(114);
	packet_add_byte(packet, player->inventory->index);

	for(int i = 0; i < player->inventory->index; i++) {
		packet_add_short(packet, player->inventory->items[i]->id + (player->inventory->items[i]->worn ? 0x8000 : 0));
		if(item_definitions[player->inventory->items[i]->id]->stackable) {
			if(player->inventory->items[i]->amount < 128)
				packet_add_byte(packet, player->inventory->items[i]->amount);
			else
				packet_add_integer(packet, player->inventory->items[i]->amount);
		}
	}

	player_packet_send(player, packet);
	return;
}

void send_update_item(Player *player, unsigned char slot)
{
	Packet *packet = packet_create(228);
	Item *item = player->inventory->items[slot];

	packet_add_byte(packet, slot);
	packet_add_short(packet, item->id + (item->worn ? 0x8000 : 0));
	if(item_definitions[item->id]->stackable)
		packet_add_integer(packet, item->amount);

	player_packet_send(player, packet);
	return;
}

void send_equipment_bonuses(Player *player)
{
	Packet *packet = packet_create(177);

	for(int i = 0; i < 5; i++)
		packet_add_byte(packet, player->bonuses[i]);

	player_packet_send(player, packet);
	return;
}

void send_session_key(Player *player)
{
	Packet *packet = packet_create(-1);
	packet_add_long(packet, rand64bits());
	player_packet_send(player, packet);
	return;
}
