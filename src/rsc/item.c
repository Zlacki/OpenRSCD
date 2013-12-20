/*
 * item.c
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
#include "item.h"
#include "region.h"
#include "util.h"

Item *item_create(unsigned short id, unsigned int amount)
{
	Item *item = safe_alloc(sizeof(Item));
	item->id = id;
	item->amount = amount;
	item->worn = 0;
	return item;
}

GroundItem *ground_item_create(unsigned short id, unsigned int amount, unsigned int x, unsigned int y, unsigned char respawn_time)
{
	GroundItem *ground_item = safe_alloc(sizeof(GroundItem));
	GroundItem *tile_item = get_ground_item(ground_item->index, ground_item->id, ground_item->x, ground_item->y);
	Region *region = get_region(x, y);
	ground_item->id = id;
	ground_item->amount = amount;
	ground_item->x = x;
	ground_item->y = y;
	ground_item->respawn_time = respawn_time;
	for(int i = 0; i < MAX_ENTITIES; i++)
		if(items[i] != NULL && tile_item != NULL) {
			region_remove_item(region, tile_item);
		}
	region_add_item(region, ground_item);
	return ground_item;
}

void ground_item_destroy(GroundItem *ground_item)
{
	items[ground_item->index] = NULL;
	region_remove_item(get_region(ground_item->x, ground_item->y), ground_item);
	free(ground_item);
	return;
}

List *ground_item_get_regional_players(GroundItem *ground_item)
{
	Region **surrounding_regions = region_get_surrounding_regions(ground_item->x, ground_item->y);

	List *players_in_regional_area = list_create();
	for(int i = 0; i < 9; i++) {
		Region *region = surrounding_regions[i];
		for(Node *node = region->players->first; node; node = node->next) {
			Player *player_in_regional_area = (Player*)node->val;
			if(player_within_range(player_in_regional_area, ground_item->x, ground_item->y))
				list_insert(players_in_regional_area, player_in_regional_area);
		}
	}

	free(surrounding_regions);

	return players_in_regional_area;
}
