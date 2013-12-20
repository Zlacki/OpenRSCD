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

#ifndef UTIL_H
#define UTIL_H
#define MAX_ENTITIES 10000
#include <ev.h>

int clients[MAX_ENTITIES];
void client_destroy(struct ev_loop *, struct ev_io *);

void *safe_alloc(size_t);
void *safe_calloc(size_t, size_t);
void error(char*);
void warning(char*);
void str_trim(char*);
void str_prepend(char*, const char*);
void str_prepend_char(char*, char);
long rand64bits(void);
int socket_set_no_block(int);

#endif
