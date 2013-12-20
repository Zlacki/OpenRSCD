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
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ioctl.h>

void *safe_alloc(size_t n) {
	char *m = (char*)malloc(n);
	if(!m) {
		/* In the future we should try
		 * to gracefully shutdown instead
		 * of just exiting without cleaning
		 * up.
		 */
		printf("ERROR: Out of memory.\n");
		exit(1);
	}

	return m;
}

void *safe_calloc(size_t number, size_t size) {
	char *m = (char*)calloc(number, size);
	if(!m) {
		/* In the future we should try
		 * to gracefully shutdown instead
		 * of just exiting without cleaning
		 * up.
		 */
		printf("ERROR: Out of memory.\n");
		exit(1);
	}

	return m;
}

void error(char *msg) {
	char *tmp = safe_alloc(sizeof(char) * (strlen(msg) + 8));
	snprintf(tmp, strlen(msg) + 8, "ERROR: %s", msg);
	perror(tmp);
	free(tmp);
	exit(1);
	return;
}

void warning(char *msg) {
	printf("WARNING: %s\n", msg);
	return;
}

void str_prepend_char(char *s, char c) {
	memmove(s + 1, s, strlen(s) + 1);
	s[0] = c;
	return;
}

void str_trim(char *s) {

	char *s1;
	for(s1 = s; *s1; s1++)
		if(!isspace(*s1))
			break;

	char *s2;
	for(s2 = s1 + strlen(s1) - 1; s2 > s1; s2--)
		if(!isspace(*s2))
			break;

	if(s1 > s || strlen(s2) > 0)
		memmove(s, s1, s2 - s1);

	s[s2 - s1 + 1] = '\0';
	return;
}

unsigned int rand256(void) {
	static unsigned const limit = RAND_MAX;
	unsigned int result = random();
	while(result >= limit)
		result = random();

	return result % 256;
}

long rand64bits(void) {
	long result = 0UL;

	for(int count = 8; count > 0; count--)
		result = 256U * result + rand256();

	return result;
}

int socket_set_no_block(int socket) {
	int flags;
	if((flags = fcntl(socket, F_GETFL, 0)) == -1)
		flags = 0;
	return fcntl(socket, F_SETFL, flags | O_NONBLOCK);
}
