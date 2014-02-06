/*
 * hashmap.h
 *
 * Copyright (C) 2011,2012,2013,2014 Zach Knight <zach@libslack.so>
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

#ifndef HASHMAP_H
#define HASHMAP_H

#define MAP_MISSING -3
#define MAP_FULL -2
#define MAP_OMEM -1
#define MAP_OK 0

typedef struct {
	int key;
	int in_use;
	void* data;
} hashmap_element;

typedef struct {
	int table_size;
	int size;
	hashmap_element* data;
} hashmap_map;

typedef int (*PFany)(void*, void*);
hashmap_map* hashmap_new();
int hashmap_iterate(hashmap_map* in, PFany f, void* item);
int hashmap_put(hashmap_map* in, int key, void* value);
int hashmap_get(hashmap_map* in, int key, void** arg);
int hashmap_remove(hashmap_map* in, int key);
int hashmap_get_one(hashmap_map* in, void** arg, int remove);
void hashmap_free(hashmap_map* in);
int hashmap_length(hashmap_map* in);

#endif
