/*
 * packetcreator.h
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

#ifndef RSC_PACKETCREATOR_H
#define RSC_PACKETCREATOR_H

#include "player.h"

void send_login_response(Player*, char);
void send_constants(Player*);
void send_session_key(Player*);
void send_client_options(Player*);
void send_welcome_box(Player*);
void send_server_message(Player*, char*);
void send_stats(Player*);
void send_inventory(Player*);
void send_update_item(Player*, unsigned char);
void send_equipment_bonuses(Player*);
void update_player_positions(Player*);
void update_player_appearances(Player*);
void update_ground_items(Player*);
void update_objects(Player*);

#endif
