/*
 * packet.c
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
#include "packet.h"
#include "util.h"

const int BITMASKS[] = {
	0, 0x1, 0x3, 0x7,
	0xf, 0x1f, 0x3f, 0x7f,
	0xff, 0x1ff, 0x3ff, 0x7ff,
	0xfff, 0x1fff, 0x3fff, 0x7fff,
	0xffff, 0x1ffff, 0x3ffff, 0x7ffff,
	0xfffff, 0x1fffff, 0x3fffff, 0x7fffff,
	0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff,
	0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff,
	-1
};

Packet *packet_create(unsigned char id) {
	Packet *p = safe_alloc(sizeof(Packet));
	p->id = id;
	p->offset = 0;
	p->buffer = safe_calloc(5000, sizeof(char));
	p->bit_position = 0;
	return p;
}

void packet_destroy(Packet *packet) {
	free(packet->buffer);
	free(packet);
	return;
}

unsigned char packet_read_byte(Packet *p) {
	return p->buffer[p->offset++];
}

unsigned short packet_read_short(Packet *p) {
	return (short) ((short) ((packet_read_byte(p) & 0xFF) << 8) | (short) (packet_read_byte(p) & 0xFF));
}

unsigned int packet_read_int(Packet *p) {
	return ((packet_read_byte(p) & 0xFF) << 24) | ((packet_read_byte(p) & 0xFF) << 16) | ((packet_read_byte(p) & 0xFF) << 8) | (packet_read_byte(p) & 0xFF);
}

char *packet_read_string(Packet *p) {
	int len = packet_read_byte(p) & 0xFF;
	char *buffer = safe_alloc(len + 1);
	for(int i = 0; i < len; i++)
		buffer[i] = packet_read_byte(p);
	buffer[len] = '\0';
	return buffer;
}

void packet_add_bits(Packet *p, unsigned int val, unsigned int num) {
	int byte_position = (p->bit_position >> 3);
	int bit_offset = 8 - (p->bit_position & 7);
	p->bit_position += num;
	p->offset = (p->bit_position + 7) / 8;

	for(; num > bit_offset; bit_offset = 8) {
		p->buffer[byte_position] &= ~BITMASKS[bit_offset];
		p->buffer[byte_position++] |= (val >> (num - bit_offset)) & BITMASKS[bit_offset];

		num -= bit_offset;
	}
	if(num == bit_offset) {
		p->buffer[byte_position] &= ~BITMASKS[bit_offset];
		p->buffer[byte_position] |= val & BITMASKS[bit_offset];
	} else {
		p->buffer[byte_position] &= ~(BITMASKS[num] << (bit_offset - num));
		p->buffer[byte_position] |= (val & BITMASKS[num]) << (bit_offset - num);
	}
	return;
}

void packet_add_byte(Packet *p, unsigned char c) {
	p->buffer[p->offset++] = (c & 0xFF);
	return;
}

void packet_add_short(Packet *p, unsigned short val) {
	packet_add_byte(p, (char) (val >> 8));
	packet_add_byte(p, (char) val);
	return;
}

void packet_add_integer(Packet *p, unsigned int val) {
	packet_add_byte(p, (char) (val >> 24));
	packet_add_byte(p, (char) (val >> 16));
	packet_add_byte(p, (char) (val >> 8));
	packet_add_byte(p, (char) val);
	return;
}

void packet_add_long(Packet *p, unsigned long l) {
	for(int i = 56; i >= 0; i -= 8)
		packet_add_byte(p, (char) (l >> i));
	return;
}
