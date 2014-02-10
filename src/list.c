/*
 * list.c
 *
 * Copyright (C) 2013,2014, Zach Knight <zach@libslack.so>
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
#include "util.h"
#include "list.h"

Node *list_insert(List *list, void *val)
{
	Node *node = safe_calloc(1, sizeof(Node));

	node->val = val;
	if(list->last) {
		list->last->next = node;
		node->prev = list->last;
		list->last = node;
	} else {
		list->first = node;
		list->last = node;
	}

	list->size++;
	return node;
}

void list_delete(List *list, Node *node)
{
	Node *next = node->next;
	Node *prev = node->prev;

	if(prev) {
		if(next) {
			prev->next = next;
			next->prev = prev;
		} else {
			prev->next = NULL;
			list->last = prev;
		}
	} else {
		if(next) {
			next->prev = NULL;
			list->first = next;
		} else {
			list->first = NULL;
			list->last = NULL;
		}
	}

	free(node);
	list->size--;

	return;
}

Node *list_search(List *list, void *val)
{
	Node* node;

	for(node = list->first; node; node = node->next)
		if(node->val == val)
			return node;

	return NULL;
}

List *list_create(void)
{
	List *list = safe_alloc(sizeof(List));
	list->first = list->last = NULL;
	list->size = 0;
	return list;
}
