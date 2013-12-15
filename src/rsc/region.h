/*
 * region.h
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

#ifndef RSC_REGION_H
#define RSC_REGION_H

#include "../list.h"
#include "item.h"
#include "object.h"

typedef struct {
	List *players;
	List *items;
	List *objects;
	unsigned int x;
	unsigned int y;
} Region;

Region *region_create(int, int);
Region *get_region(int, int);
Region **region_get_surrounding_regions(int, int);
void region_send_player_position_update(int);
void region_send_player_appearance_update(int);
void region_add_player(Region*, int);
void region_remove_player(Region*, int);
void region_add_item(Region*, GroundItem*);
void region_remove_item(Region*, GroundItem*);
void region_add_object(Region*, Object*);
void region_remove_object(Region*, Object*);
void region_send_item_position_update(GroundItem*);
void region_send_object_position_update(Object*);

#endif
