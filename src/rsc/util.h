/*
 * util.h
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

#ifndef RSC_UTIL_H
#define RSC_UTIL_H

#include <json-c/json.h>
#include "player.h"
#include "region.h"
#include "../hashmap.h"
#include "../util.h"

#define MAX_HEIGHT 3776
#define MAX_WIDTH 944
#define REGIONS_TOTAL MAX_HEIGHT * MAX_WIDTH / 32
#define REGION_SIZE 32
#define REGION_LOWER_BOUND REGION_SIZE / 2 - 1

Player *players[MAX_CLIENTS];
hashmap_map *regions;

typedef struct {
	unsigned short id;
	unsigned short type;
	unsigned short width;
	unsigned short height;
	unsigned short elevation;
	const char *name;
	const char *description;
	const char *command1;
	const char *command2;
} ObjectDefinition;

typedef struct {
	unsigned short type;
	unsigned short unknown;
	const char *name;
	const char *description;
	const char *command1;
	const char *command2;
} DoorDefinition;

typedef struct {
	unsigned short id;
	unsigned short hits;
	unsigned short att;
	unsigned short def;
	unsigned short str;
	unsigned char attackable;
	short default_drop;
	const char *name;
	const char *description;
	const char *command;
} NPCDefinition;

typedef struct {
	unsigned short id;
	const char *command;
	const char *name;
	unsigned char female;
	unsigned char members;
	unsigned char stackable;
	unsigned char untradable;
	unsigned char wearable;
	unsigned short sprite;
	unsigned char armor_bonus;
	unsigned short base_price;
	unsigned char magic_bonus;
	unsigned char prayer_bonus;
	unsigned char required_level;
	char required_skill;
	unsigned char weapon_aim;
	unsigned char weapon_power;
	unsigned short wearable_id;
	short wearable_slot;
} ItemDefinition;

typedef struct {
	unsigned short id;
	unsigned short req_level;
	unsigned short drain_rate;
	const char *name;
	const char *description;
} PrayerDefinition;

typedef struct {
	unsigned short id;
	unsigned short req_level;
	unsigned short type;
	unsigned short rune_count;
	unsigned short **required_runes;
	unsigned short exp;
	const char *name;
	const char *description;
} SpellDefinition;

ObjectDefinition** object_definitions;
DoorDefinition** door_definitions;
NPCDefinition** npc_definitions;
ItemDefinition** item_definitions;
PrayerDefinition** prayer_definitions;
SpellDefinition** spell_definitions;

char get_height(int);
long username_to_hash(char*);
char *hash_to_username(long);
void parse_object_definitions(json_object*);
void parse_object_locations(json_object*);
void parse_door_definitions(json_object*);
void parse_npc_definitions(json_object*);
void parse_item_definitions(json_object*);
void parse_item_locations(json_object*);
void parse_prayer_definitions(json_object*);
void parse_spell_definitions(json_object*);

#endif
