/*
 * rsc.c
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
#include <pthread.h>
#include <unistd.h>
#include <sys/stat.h>
#include "../module.h"
#include "packet.h"
#include "player.h"
#include "packethandler.h"
#include "util.h"

void on_accept(struct ev_loop *loop, struct ev_io *watcher, int socket, int index) {
	players[index] = player_create(loop, watcher, socket, index);
	return;
}

void on_read(int index, int packet_length) {
	Player *player = players[index];
	if(player == NULL || index < 0)
		return;

	if(packet_length < 0 || packet_length > 5000) {
		warning("Packet length outside array bounds; dropping packet and disconnecting user.");
		player_disconnect(player);
		return;
	}

	char *buffer = safe_alloc(packet_length);
	int res = read(player->socket, buffer, packet_length);
	if(res < packet_length) {
		warning("Couldn't read full packet length; dropping packet and disconnecting user.");
		printf("%d:%d\n", res, packet_length);
		free(buffer);
		player_disconnect(player);
		return;
	}

	Packet *p = safe_alloc(sizeof(Packet));
	p->id = (*buffer & 0xFF);
	p->buffer = NULL;
	p->offset = 0;
	p->length = packet_length;

	if(packet_length > 0) {
		packet_length--;
		p->buffer = safe_alloc(sizeof(char) * packet_length);
		memcpy(p->buffer, buffer + 1, packet_length);
	}

	free(buffer);
	handle_packet(player, p);
	return;
}

void *engine_loop() {
	time_t walk_timer = 0;
	for(;;) {
		usleep(50000);
		time_t now = time(NULL);
		if(now - walk_timer >= 600000) {
			/** TODO: This **/
		}
	}
	return NULL;
}

void load(void) {
	printf("RuneScape Classic\n");
	printf("Loading definitions...\n\n");

	printf("Loading object definitions...");
	FILE *file = fopen("data/defs/objects.json", "r");
	struct stat buffer;
	if (fstat(fileno(file), &buffer) == -1)
		error("Could not read file size.");
	char *data = malloc(buffer.st_size + 1);
	if(fread(data, 1, buffer.st_size, file) < 1)
		error("Could not read file.");
	parse_object_definitions(json_tokener_parse(data));
	free(data);
	fclose(file);
	printf("done\n");

	printf("Loading door definitions...");
	file = fopen("data/defs/doors.json", "r");
	if (fstat(fileno(file), &buffer) == -1)
		error("Could not read file size.");
	data = safe_alloc(buffer.st_size + 1);
	if(fread(data, 1, buffer.st_size, file) < 1)
		error("Could not read file.");
	parse_door_definitions(json_tokener_parse(data));
	free(data);
	fclose(file);
	printf("done\n");

	printf("Loading NPC definitions...");
	file = fopen("data/defs/npcs.json", "r");
	if (fstat(fileno(file), &buffer) == -1)
		error("Could not read file size.");
	data = safe_alloc(buffer.st_size + 1);
	if(fread(data, 1, buffer.st_size, file) < 1)
		error("Could not read file.");
	parse_npc_definitions(json_tokener_parse(data));
	free(data);
	fclose(file);
	printf("done\n");

	printf("Loading item definitions...");
	file = fopen("data/defs/items.json", "r");
	if (fstat(fileno(file), &buffer) == -1)
		error("Could not read file size.");
	data = safe_alloc(buffer.st_size + 1);
	if(fread(data, 1, buffer.st_size, file) < 1)
		error("Could not read file.");
	parse_item_definitions(json_tokener_parse(data));
	free(data);
	fclose(file);
	printf("done\n");

	printf("Loading prayer definitions...");
	file = fopen("data/defs/prayers.json", "r");
	if (fstat(fileno(file), &buffer) == -1)
		error("Could not read file size.");
	data = safe_alloc(buffer.st_size + 1);
	if(fread(data, 1, buffer.st_size, file) < 1)
		error("Could not read file.");
	parse_prayer_definitions(json_tokener_parse(data));
	free(data);
	fclose(file);
	printf("done\n");

	printf("Loading spell definitions...");
	file = fopen("data/defs/spells.json", "r");
	if (fstat(fileno(file), &buffer) == -1)
		error("Could not read file size.");
	data = safe_alloc(buffer.st_size + 1);
	if(fread(data, 1, buffer.st_size, file) < 1)
		error("Could not read file.");
	parse_spell_definitions(json_tokener_parse(data));
	free(data);
	fclose(file);
	printf("done\n");

	printf("\nDefinitions loaded.\n");


	printf("Initializing regions...");
	regions = hashmap_new();
	printf("done\n");

	printf("Loading locations...\n\n");

	printf("Loading item locations...");
	file = fopen("data/locs/items.json", "r");
	if (fstat(fileno(file), &buffer) == -1)
		error("Could not read file size.");
	data = safe_alloc(buffer.st_size + 1);
	if(fread(data, 1, buffer.st_size, file) < 1)
		error("Could not read file.");
	parse_item_locations(json_tokener_parse(data));
	free(data);
	fclose(file);
	printf("done\n");

	printf("Loading object locations...");
	file = fopen("data/locs/objects.json", "r");
	if (fstat(fileno(file), &buffer) == -1)
		error("Could not read file size.");
	data = safe_alloc(buffer.st_size + 1);
	if(fread(data, 1, buffer.st_size, file) < 1)
		error("Could not read file.");
	parse_object_locations(json_tokener_parse(data));
	free(data);
	fclose(file);
	printf("done\n");

	printf("\nLocations loaded.\n");

	printf("Loading game engine...");

	pthread_t game_engine;
	if(pthread_create(&game_engine, NULL, engine_loop, NULL)) {
		error("Could not start game engine thread.");
	}

	printf("done\n");

	printf("RuneScape Classic module loaded.\n");

	return;
}
