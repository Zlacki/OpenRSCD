/*
 * packethandler.c
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
#include "packetcreator.h"
#include "packethandler.h"
#include "util.h"

void handle_player_indicies(Player *player, Packet *packet) {
	int length = packet_read_short(packet);
	printf("Length:%d,Indicies:", length);

	printf("{ ");
	for(int i = 0; i < length; i++)
		printf("%d, ", packet_read_short(packet));
	printf(" };\n");

	return;
}

void handle_walking(Player *player, Packet *packet) {
	unsigned short start_x = packet_read_short(packet);
	unsigned short start_y = packet_read_short(packet);
	unsigned char step_count = (packet->length - packet->offset) / 2;
	char *waypoints_x = safe_alloc(sizeof(char) * step_count);
	char *waypoints_y = safe_alloc(sizeof(char) * step_count);
	printf("startX:%d,startY:%d, waypoints: ", start_x, start_y);

	for(int i = 0; i < step_count; i++) {
		waypoints_x[i] = packet_read_byte(packet);
		waypoints_y[i] = packet_read_byte(packet);
		printf("[%d,%d], ", waypoints_x[i], waypoints_y[i]);
	}

	if(step_count > 0)
		player_set_location(player, waypoints_x[step_count - 1] + start_x, waypoints_y[step_count - 1] + start_y);
	else
		player_set_location(player, start_x, start_y);

	printf("\n");
	return;
}

void handle_remove_item(Player *player, Packet *packet) {
	short slot = packet_read_short(packet);
	if(slot > player->inventory->index || slot < 0) {
		printf("Suspicious behavior from ‘%s’ trying to remove something they don't have.\n", player->username);
		return;
	}

	Item *item = player->inventory->items[slot];
	if(item == NULL) {
		printf("Suspicious behavior from ‘%s’ trying to remove something they don't have.\n", player->username);
		return;
	}

	player_remove_item(player, item);
	return;
}

void handle_equip_item(Player *player, Packet *packet) {
	short slot = packet_read_short(packet);
	if(slot > player->inventory->index || slot < 0) {
		printf("Suspicious behavior from ‘%s’ trying to equip something they don't have.\n", player->username);
		return;
	}

	Item *item = player->inventory->items[slot];
	if(item == NULL) {
		printf("Suspicious behavior from ‘%s’ trying to equip something they don't have.\n", player->username);
		return;
	}

	if(!item_definitions[item->id]->wearable) {
		printf("Suspicious behavior from ‘%s’ trying to equip an unequippable item.\n", player->username);
		return;
	}

	player_equip_item(player, item);
	return;
}

void handle_packet(Player *player, Packet *packet) {
	switch(packet->id) {
	case 0:
		player_connect(player, packet);
		break;
	case 32:
		send_session_key(player);
		break;
	case 156:
		/* Runtime error in client */
		warning(packet_read_string(packet));
		break;
	case 153:
		player_packet_send(player, packet_create(5));
		break;
	case 129:
		player_disconnect(player);
		break;
	case 83:
		handle_player_indicies(player, packet);
		break;
	case 92:
		handle_remove_item(player, packet);
		break;
	case 181:
		handle_equip_item(player, packet);
		break;
	case 132:
		handle_walking(player, packet);
		break;
	default:
		printf("Unhandled packet: %d\n", packet->id);
		break;
	}

	packet_destroy(packet);

	return;
}
