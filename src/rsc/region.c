/*
 * region.c
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
#include "player.h"
#include "item.h"
#include "packetcreator.h"
#include "region.h"
#include "../hashmap.h"
#include "util.h"

Region *region_create(int x, int y)
{
	Region *region = safe_alloc(sizeof(Region));
	region->x = x;
	region->y = y;
	region->players = list_create();
	region->items = list_create();
	region->objects = list_create();
	return region;
}

int region_hash(int x, int y)
{
	int i = 1;
	i = 31 * i + x;
	i = 31 * i + y;
	return i;
}

Region *get_region(int x, int y)
{
	Region *region;
	if(hashmap_get(regions, region_hash(x / REGION_SIZE, y / REGION_SIZE), (void*)&region) == MAP_MISSING)
		hashmap_put(regions, region_hash(x / REGION_SIZE, y / REGION_SIZE), (region = region_create(x / REGION_SIZE, y / REGION_SIZE)));

	return region;
}

Region *get_region_with_coords(int x, int y)
{
	Region *region;
	if(hashmap_get(regions, region_hash(x, y), (void*)&region) == MAP_MISSING)
		hashmap_put(regions, region_hash(x, y), (region = region_create(x, y)));

	return region;
}

GroundItem *get_ground_item(int index, int id, int x, int y)
{
	List *items = get_region(x, y)->items;
	for(Node *node = items->first; node; node = node->next) {
		GroundItem *item = (GroundItem*) node->val;
		if(item->index == index && item->id == id && item->x == x && item->y == y)
			return item;
	}

	return NULL;
}

Region **region_get_surrounding_regions(int x, int y)
{
	int region_x = x / REGION_SIZE;
	int region_y = y / REGION_SIZE;

	Region **surrounding_regions = safe_alloc(sizeof(Region*) * 4);

	int relX = x % REGION_SIZE;
	int relY = y % REGION_SIZE;
	surrounding_regions[0] = get_region_with_coords(region_x, region_y);
	if(relX <= REGION_LOWER_BOUND)
			if(relY <= REGION_LOWER_BOUND) {
					surrounding_regions[1] = get_region_with_coords(region_x - 1, region_y);;
					surrounding_regions[2] = get_region_with_coords(region_x - 1, region_y - 1);
					surrounding_regions[3] = get_region_with_coords(region_x, region_y - 1);
			} else {
					surrounding_regions[1] = get_region_with_coords(region_x - 1, region_y);;
					surrounding_regions[2] = get_region_with_coords(region_x - 1, region_y + 1);
					surrounding_regions[3] = get_region_with_coords(region_x, region_y + 1);
			}
	else
			if(relY <= REGION_LOWER_BOUND) {
					surrounding_regions[1] = get_region_with_coords(region_x + 1, region_y);
					surrounding_regions[2] = get_region_with_coords(region_x + 1, region_y - 1);
					surrounding_regions[3] = get_region_with_coords(region_x, region_y - 1);
			} else {
					surrounding_regions[1] = get_region_with_coords(region_x + 1, region_y);;
					surrounding_regions[2] = get_region_with_coords(region_x + 1, region_y + 1);
					surrounding_regions[3] = get_region_with_coords(region_x, region_y + 1);
			}

	return surrounding_regions;
}

void region_remove_player(Region *region, int index)
{
	list_delete(region->players, list_search(region->players, players[index]));
	region_send_player_position_update(index);
	region_send_player_appearance_update(index);
	return;
}

void region_add_player(Region *region, int index)
{
	list_insert(region->players, players[index]);
	region_send_player_position_update(index);
	region_send_player_appearance_update(index);
	return;
}

void region_add_item(Region *region, GroundItem *ground_item)
{
	list_insert(region->items, ground_item);
	region_send_item_position_update(ground_item);
	return;
}

void region_remove_item(Region *region, GroundItem *ground_item)
{
	list_delete(region->items, list_search(region->items, ground_item));
	region_send_item_position_update(ground_item);
	return;
}

void region_add_object(Region *region, Object *object)
{
	list_insert(region->objects, object);
	region_send_object_position_update(object);
	return;
}

void region_remove_object(Region *region, Object *object)
{
	list_delete(region->objects, list_search(region->players, object));
	region_send_object_position_update(object);
	return;
}

void region_send_object_position_update(Object *object)
{
	List *players_to_update = object_get_regional_players(object);
	for(Node *node = players_to_update->first; node; node = node->next) {
		Player *player_to_update = (Player*) node->val;
		if(player_within_range(player_to_update, object->x, object->y))
			update_objects(player_to_update);
	}

	free(players_to_update);
	return;
}

void region_send_item_position_update(GroundItem *ground_item)
{
	List *players_to_update = ground_item_get_regional_players(ground_item);
	for(Node *node = players_to_update->first; node; node = node->next) {
		Player *player_to_update = (Player*) node->val;

		if(player_within_range(player_to_update, ground_item->x, ground_item->y) && get_ground_item(ground_item->index, ground_item->id, ground_item->x, ground_item->y) == NULL && list_search(player_get_regional_items(player_to_update), get_ground_item(ground_item->index, ground_item->id, ground_item->x, ground_item->y)) == NULL)
			update_ground_items(player_to_update);
	}

	free(players_to_update);
	return;
}

void region_send_player_position_update(int index)
{
	List *players_to_update = player_get_regional_players(players[index]);
	for(Node *node = players_to_update->first; node; node = node->next) {
		Player *player_to_update = (Player*) node->val;
		if(player_within_range(players[index], player_to_update->x, player_to_update->y))
			update_player_positions(player_to_update);
	}

	free(players_to_update);
	return;
}

void region_send_player_appearance_update(int index)
{
	List *players_to_update = player_get_regional_players(players[index]);
	for(Node *node = players_to_update->first; node; node = node->next) {
		Player *player_to_update = (Player*) node->val;
		if(player_within_range(players[index], player_to_update->x, player_to_update->y))
			update_player_appearances(player_to_update);
	}

	free(players_to_update);
	return;
}
