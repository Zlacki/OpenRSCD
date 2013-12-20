/*
 * util.c
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

#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "object.h"
#include "util.h"

char get_height(int y)
{
	return ((char) (y / MAX_WIDTH));
}

long username_to_hash(char *username)
{
	char *s1 = safe_alloc(13);
	int i;
	for(i = 0; i < strlen(username); i++) {
		char c = username[i];
		if(c >= 'a' && c <= 'z')
			s1[i] = c;
		else if(c >= 'A' && c <= 'Z')
			s1[i] = ((c + 97) - 65);
		else if(c >= '0' && c <= '9')
			s1[i] = c;
		else
			s1[i] = ' ';
	}
	s1[i] = '\0';

	str_trim(s1);
	if(strlen(s1) > 12)
		s1[12] = '\0';

	long l = 0L;
	for(i = 0; i < strlen(s1); i++) {
		char c = s1[i];
		l *= 37L;
		if(c >= 'a' && c <= 'z')
			l += (1 + c) - 97;
		else if(c >= '0' && c <= '9')
			l += (27 + c) - 48;
	}

	free(s1);
	return l;
}

char *hash_to_username(long hash)
{
	if(hash < 0L)
		return "invalid_name";

	char *s = safe_alloc(13);
	s[0] = '\0';
	while(hash != 0L) {
		int i = (int) (hash % 37L);
		hash /= 37L;
		if(i == 0)
			str_prepend_char(s, ' ');
		else if(i < 27)
			if(hash % 37L == 0L)
				str_prepend_char(s, (char) ((i + 65) - 1));
			else
				str_prepend_char(s, (char) ((i + 97) - 1));
		else
			str_prepend_char(s, (char) ((i + 48) - 27));
	}

	return s;
}

void parse_object_definitions(json_object *jobj)
{
	jobj = json_object_object_get(jobj, "Objects");
	int len = json_object_array_length(jobj);
	object_definitions = safe_alloc(sizeof(ObjectDefinition) * len);
	for(int i = 0; i < len; i++) {
		json_object *jvalue = json_object_array_get_idx(jobj, i);
		ObjectDefinition *def = safe_alloc(sizeof(ObjectDefinition));
		def->name = json_object_get_string(json_object_object_get(jvalue, "name"));
		def->description = json_object_get_string(json_object_object_get(jvalue, "description"));
		def->command1 = json_object_get_string(json_object_object_get(jvalue, "command1"));
		def->command2 = json_object_get_string(json_object_object_get(jvalue, "command2"));
		def->id = json_object_get_int(json_object_object_get(jvalue, "id"));
		def->type = json_object_get_int(json_object_object_get(jvalue, "type"));
		def->width = json_object_get_int(json_object_object_get(jvalue, "width"));
		def->height = json_object_get_int(json_object_object_get(jvalue, "height"));
		def->elevation = json_object_get_int(json_object_object_get(jvalue, "elevation"));
		object_definitions[i] = def;
	}

	return;
}

void parse_object_locations(json_object *jobj)
{
	jobj = json_object_object_get(jobj, "GroundObjects");
	int len = json_object_array_length(jobj);
	for(int i = 0; i < len; i++) {
		json_object *jvalue = json_object_array_get_idx(jobj, i);
		object_create(json_object_get_int(json_object_object_get(jvalue, "id")),
			json_object_get_int(json_object_object_get(jvalue, "x")),
			json_object_get_int(json_object_object_get(jvalue, "y")),
			json_object_get_int(json_object_object_get(jvalue, "direction")),
			json_object_get_int(json_object_object_get(jvalue, "type"))
		);
	}

	return;
}

void parse_door_definitions(json_object *jobj)
{
	jobj = json_object_object_get(jobj, "Doors");
	int len = json_object_array_length(jobj);
	door_definitions = safe_alloc(sizeof(DoorDefinition) * len);
	for(int i = 0; i < len; i++){
		json_object *jvalue = json_object_array_get_idx(jobj, i);
		DoorDefinition *def = safe_alloc(sizeof(DoorDefinition));
		def->name = json_object_get_string(json_object_object_get(jvalue, "name"));
		def->description = json_object_get_string(json_object_object_get(jvalue, "description"));
		def->command1 = json_object_get_string(json_object_object_get(jvalue, "command1"));
		def->command2 = json_object_get_string(json_object_object_get(jvalue, "command2"));
		def->type = json_object_get_int(json_object_object_get(jvalue, "type"));
		def->unknown = json_object_get_int(json_object_object_get(jvalue, "unknown"));
		door_definitions[i] = def;
	}

	return;
}

void parse_npc_definitions(json_object *jobj)
{
	jobj = json_object_object_get(jobj, "NPCs");
	int len = json_object_array_length(jobj);
	npc_definitions = safe_alloc(sizeof(NPCDefinition) * len);
	for(int i = 0; i < len; i++){
		json_object *jvalue = json_object_array_get_idx(jobj, i);
		NPCDefinition *def = safe_alloc(sizeof(NPCDefinition));
		def->id = json_object_get_int(json_object_object_get(jvalue, "id"));
		def->command = json_object_get_string(json_object_object_get(jvalue, "command"));
		def->hits = json_object_get_int(json_object_object_get(jvalue, "hits"));
		def->att = json_object_get_int(json_object_object_get(jvalue, "att"));
		def->def = json_object_get_int(json_object_object_get(jvalue, "def"));
		def->str = json_object_get_int(json_object_object_get(jvalue, "str"));
		def->attackable = json_object_get_int(json_object_object_get(jvalue, "attackable"));
		def->default_drop = json_object_get_int(json_object_object_get(jvalue, "defaultDrop"));
		def->name = json_object_get_string(json_object_object_get(jvalue, "name"));
		def->description = json_object_get_string(json_object_object_get(jvalue, "description"));
		npc_definitions[i] = def;
	}

	return;
}

void parse_item_definitions(json_object *jobj)
{
	jobj = json_object_object_get(jobj, "Items");
	int len = json_object_array_length(jobj);
	item_definitions = safe_alloc(sizeof(ItemDefinition) * len);
	for(int i = 0; i < len; i++){
		json_object *jvalue = json_object_array_get_idx(jobj, i);
		ItemDefinition *def = safe_alloc(sizeof(ItemDefinition));
		def->id = json_object_get_int(json_object_object_get(jvalue, "id"));
		def->command = json_object_get_string(json_object_object_get(jvalue, "command"));
		def->name = json_object_get_string(json_object_object_get(jvalue, "name"));
		def->female = json_object_get_int(json_object_object_get(jvalue, "female"));
		def->members = json_object_get_int(json_object_object_get(jvalue, "members"));
		def->stackable = json_object_get_int(json_object_object_get(jvalue, "stackable"));
		def->untradable = json_object_get_int(json_object_object_get(jvalue, "untradable"));
		def->wearable = json_object_get_int(json_object_object_get(jvalue, "wearable"));
		def->sprite = json_object_get_int(json_object_object_get(jvalue, "sprite"));
		def->armor_bonus = json_object_get_int(json_object_object_get(jvalue, "armor_bonus"));
		def->base_price = json_object_get_int(json_object_object_get(jvalue, "base_price"));
		def->magic_bonus = json_object_get_int(json_object_object_get(jvalue, "magic_bonus"));
		def->prayer_bonus = json_object_get_int(json_object_object_get(jvalue, "prayer_bonus"));
		def->required_level = json_object_get_int(json_object_object_get(jvalue, "required_level"));
		def->required_skill = json_object_get_int(json_object_object_get(jvalue, "required_skill"));
		def->weapon_aim = json_object_get_int(json_object_object_get(jvalue, "weapon_aim"));
		def->weapon_power = json_object_get_int(json_object_object_get(jvalue, "weapon_power"));
		def->wearable_id = json_object_get_int(json_object_object_get(jvalue, "wearable_id"));
		def->wearable_slot = json_object_get_int(json_object_object_get(jvalue, "wearable_slot"));
		item_definitions[i] = def;
	}

	return;
}

void parse_item_locations(json_object *jobj)
{
	jobj = json_object_object_get(jobj, "Items");
	int len = json_object_array_length(jobj);
	for(int i = 0; i < len; i++) {
		json_object *jvalue = json_object_array_get_idx(jobj, i);
		int id = json_object_get_int(json_object_object_get(jvalue, "id"));
		int amount = json_object_get_int(json_object_object_get(jvalue, "amount"));
		if(item_definitions[id]->stackable)
			ground_item_create(id, amount,
				json_object_get_int(json_object_object_get(jvalue, "x")),
				json_object_get_int(json_object_object_get(jvalue, "y")),
				json_object_get_int(json_object_object_get(jvalue, "respawn_time"))
			);
		else
			for(int i1 = 0; i1 < amount; i1++)
				ground_item_create(id, amount,
					json_object_get_int(json_object_object_get(jvalue, "x")),
					json_object_get_int(json_object_object_get(jvalue, "y")),
					json_object_get_int(json_object_object_get(jvalue, "respawn_time"))
				);
	}

	return;
}

void parse_prayer_definitions(json_object *jobj)
{
	jobj = json_object_object_get(jobj, "Prayers");
	int len = json_object_array_length(jobj);
	prayer_definitions = safe_alloc(sizeof(PrayerDefinition) * len);
	for(int i = 0; i < len; i++){
		json_object *jvalue = json_object_array_get_idx(jobj, i);
		PrayerDefinition *def = safe_alloc(sizeof(PrayerDefinition));
		def->id = json_object_get_int(json_object_object_get(jvalue, "id"));
		def->req_level = json_object_get_int(json_object_object_get(jvalue, "reqlevel"));
		def->drain_rate = json_object_get_int(json_object_object_get(jvalue, "drainrate"));
		def->name = json_object_get_string(json_object_object_get(jvalue, "name"));
		def->description = json_object_get_string(json_object_object_get(jvalue, "description"));
		prayer_definitions[i] = def;
	}

	return;
}

void parse_spell_definitions(json_object *jobj)
{
	jobj = json_object_object_get(jobj, "Spells");
	int len = json_object_array_length(jobj);
	spell_definitions = safe_alloc(sizeof(SpellDefinition) * len);
	for(int i = 0; i < len; i++){
		json_object *jvalue = json_object_array_get_idx(jobj, i);
		SpellDefinition *def = safe_alloc(sizeof(SpellDefinition));
		def->id = json_object_get_int(json_object_object_get(jvalue, "id"));
		def->req_level = json_object_get_int(json_object_object_get(jvalue, "reqlevel"));
		def->type = json_object_get_int(json_object_object_get(jvalue, "type"));
		def->rune_count = json_object_get_int(json_object_object_get(jvalue, "runecount"));
		def->exp = json_object_get_int(json_object_object_get(jvalue, "exp"));
		def->name = json_object_get_string(json_object_object_get(jvalue, "name"));
		def->description = json_object_get_string(json_object_object_get(jvalue, "description"));
		json_object *runes = json_object_object_get(jvalue, "requiredrunes");
		int len_1 = json_object_array_length(runes);
		def->required_runes = safe_alloc(sizeof(int*) * len_1);
		for(int i1 = 0; i1 < len_1; i1++) {
			jvalue = json_object_array_get_idx(runes, i1);
			def->required_runes[i1] = safe_alloc(sizeof(int) * 2);
			def->required_runes[i1][0] = json_object_get_int(json_object_object_get(jvalue, "id"));
			def->required_runes[i1][1] = json_object_get_int(json_object_object_get(jvalue, "count"));
		}
		spell_definitions[i] = def;
	}

	return;
}
