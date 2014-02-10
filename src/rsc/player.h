/*
 * player.h
 *
 * Copyright (C) 2013,2014, Zach Knight <zach@libslack.so>
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

#ifndef RSC_PLAYER_H
#define RSC_PLAYER_H

#include <stdint.h>
#include <ev.h>
#include "packet.h"
#include "inventory.h"
#include "region.h"
#include "item.h"

typedef struct {
	unsigned int socket;
	unsigned int index;
	uint64_t username_hash;
	char *username;
	unsigned short x;
	unsigned short y;
	Region *region;
	Inventory *inventory;
	int worn_items[12];
	unsigned char direction;
	unsigned char social_options[4];
	unsigned char client_options[3];
	unsigned char bonuses[5];
	unsigned long skills[18];
	struct ev_io *watcher;
	struct ev_loop *loop;
	List *walking_waypoints;
} Player;

Player *player_create(struct ev_loop *, struct ev_io *, unsigned int, unsigned int);
void player_connect(Player*, Packet*);
void player_destroy(Player*);
void player_disconnect(Player*);
void player_packet_send(Player*, Packet*);
void player_equip_item(Player*, Item*);
void player_remove_item(Player*, Item*);
void player_set_location(Player*, int, int);
char player_within_range(Player*, int, int);
List *player_get_regional_players(Player*);
List *player_get_regional_items(Player*);
List *player_get_regional_objects(Player*);

#endif
