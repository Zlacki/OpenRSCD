/*
 * hashset.c
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

HashSet* hashset_create() {
	HashSet *set = safe_calloc(1, sizeof(HashSet));

	set->nbits = 3;
	set->capacity = 1 << set->nbits;
	set->mask = set->capacity - 1;
	set->items = safe_calloc(set->capacity, sizeof(size_t));
	set->nitems = 0;

	return set;
}

void hashset_destroy(HashSet *set) {
	if(set)
		free(set->items);

	free(set);
}

void hashset_insert(HashSet *set, void *item) {
	size_t value = (size_t)item;
	size_t ii;

	if(value == 0 || value == 1)
		return;

	ii = set->mask & (PRIME_1 * value);

	while(set->items[ii] != 0 && set->items[ii] != 1)
		if (set->items[ii] != value)
			ii = set->mask & (ii + PRIME_2);

	set->nitems++;
	set->items[ii] = value;
	return 1;
}
