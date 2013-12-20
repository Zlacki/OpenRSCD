/*
 * object.c
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

#include <stdlib.h>
#include "region.h"
#include "object.h"
#include "util.h"

Object *object_create(unsigned short id, unsigned int x, unsigned int y, unsigned char direction, unsigned char type)
{
	Object *object = safe_alloc(sizeof(Object));
	object->id = id;
	object->x = x;
	object->y = y;
	object->direction = direction;
	object->type = type;
	region_add_object(get_region(x, y), object);
	return object;
}

List *object_get_regional_players(Object *object)
{
	Region **surrounding_regions = region_get_surrounding_regions(object->x, object->y);

	List *players_in_regional_area = list_create();
	for(int i = 0; i < 9; i++) {
		Region *region = surrounding_regions[i];
		for(Node *node = region->players->first; node; node = node->next) {
			Player *player_in_regional_area = (Player*)node->val;
			if(player_within_range(player_in_regional_area, object->x, object->y))
				list_insert(players_in_regional_area, player_in_regional_area);
		}
	}

	free(surrounding_regions);

	return players_in_regional_area;
}
