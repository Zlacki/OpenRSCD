/*
 * hashmap.c
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
#include <stdio.h>
#include "hashmap.h"
#include "util.h"

#define INITIAL_SIZE 1024

hashmap_map* hashmap_new()
{
	hashmap_map* m = (hashmap_map*) safe_alloc(sizeof(hashmap_map));
	if(!m) {
		if (m)
			hashmap_free(m);
		return NULL;
	}

	m->data = (hashmap_element*) calloc(INITIAL_SIZE, sizeof(hashmap_element));
	if(!m->data) {
		if (m)
			hashmap_free(m);
		return NULL;
	}

	m->table_size = INITIAL_SIZE;
	m->size = 0;

	return m;
}

unsigned int hashmap_hash_int(hashmap_map* m, unsigned int key)
{
	key += (key << 12);
	key ^= (key >> 22);
	key += (key << 4);
	key ^= (key >> 9);
	key += (key << 10);
	key ^= (key >> 2);
	key += (key << 7);
	key ^= (key >> 12);

	key = (key >> 3) * 2654435761;

	return key % m->table_size;
}

int hashmap_hash(hashmap_map* m, int key)
{
	int curr;
	int i;

	if(m->size == m->table_size) return MAP_FULL;

	curr = hashmap_hash_int(m, key);

	for(i = 0; i< m->table_size; i++) {
		if(m->data[curr].in_use == 0)
			return curr;

		if(m->data[curr].key == key && m->data[curr].in_use == 1)
			return curr;

		curr = (curr + 1) % m->table_size;
	}

	return MAP_FULL;
}

int hashmap_rehash(hashmap_map* m)
{
	int i;
	int old_size;
	hashmap_element* curr;

	hashmap_element* temp = (hashmap_element *)
		calloc(2 * m->table_size, sizeof(hashmap_element));
	if(!temp) return MAP_OMEM;

	curr = m->data;
	m->data = temp;

	old_size = m->table_size;
	m->table_size = 2 * m->table_size;
	m->size = 0;

	for(i = 0; i < old_size; i++) {
		int status = hashmap_put(m, curr[i].key, curr[i].data);
		if (status != MAP_OK)
			return status;
	}

	free(curr);

	return MAP_OK;
}

int hashmap_put(hashmap_map* m, int key, void* value)
{
	int index;

	index = hashmap_hash(m, key);
	while(index == MAP_FULL) {
		if (hashmap_rehash(m) == MAP_OMEM) {
			return MAP_OMEM;
		}
		index = hashmap_hash(m, key);
	}

	m->data[index].data = value;
	m->data[index].key = key;
	m->data[index].in_use = 1;
	m->size++;

	return MAP_OK;
}

int hashmap_get(hashmap_map* m, int key, void** arg)
{
	int curr;
	int i;

	curr = hashmap_hash_int(m, key);

	for(i = 0; i< m->table_size; i++) {

		if(m->data[curr].key == key && m->data[curr].in_use == 1) {
			*arg = (void*) (m->data[curr].data);
			return MAP_OK;
		}

		curr = (curr + 1) % m->table_size;
	}

	*arg = NULL;

	return MAP_MISSING;
}

int hashmap_get_one(hashmap_map* m, void** arg, int remove)
{
	int i;

	if (hashmap_length(m) <= 0)
		return MAP_MISSING;

	for(i = 0; i< m->table_size; i++)
		if(m->data[i].in_use != 0) {
			*arg = (void*) (m->data[i].data);
			if (remove) {
				m->data[i].in_use = 0;
				m->size--;
			}
			return MAP_OK;
		}

	return MAP_OK;
}

int hashmap_iterate(hashmap_map* m, PFany f, void* item)
{
	int i;

	if (hashmap_length(m) <= 0)
		return MAP_MISSING;

	for(i = 0; i< m->table_size; i++)
		if(m->data[i].in_use != 0) {
			void* data = (void*) (m->data[i].data);
			int status = f(item, data);
			if (status != MAP_OK) {
				return status;
			}
		}

        return MAP_OK;
}

int hashmap_remove(hashmap_map* m, int key)
{
	int i;
	int curr;

	curr = hashmap_hash_int(m, key);

	for(i = 0; i< m->table_size; i++) {
		if(m->data[curr].key == key && m->data[curr].in_use == 1) {
			m->data[curr].in_use = 0;
			m->data[curr].data = NULL;
			m->data[curr].key = 0;

			m->size--;
			return MAP_OK;
		}
		curr = (curr + 1) % m->table_size;
	}

	return MAP_MISSING;
}

void hashmap_free(hashmap_map* m)
{
	free(m->data);
	free(m);
}

int hashmap_length(hashmap_map* m)
{
	if(m != NULL)
		return m->size;
	return 0;
}
