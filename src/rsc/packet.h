/*
 * packet.h
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

#ifndef RSC_PACKET_H
#define RSC_PACKET_H

#include <stdint.h>

typedef struct {
	unsigned char *buffer;
	unsigned char id;
	unsigned short offset;
	unsigned short length;
	unsigned int bit_position;
} Packet;

Packet *packet_create(unsigned char);
void packet_destroy(Packet*);
unsigned char packet_read_byte(Packet*);
unsigned short packet_read_short(Packet*);
unsigned int packet_read_int(Packet*);
uint64_t packet_read_long(Packet *p);
char *packet_read_string(Packet*);
void packet_add_byte(Packet*, unsigned char);
void packet_add_short(Packet*, unsigned short);
void packet_add_integer(Packet*, unsigned int);
void packet_add_long(Packet*, unsigned long);
void packet_add_bits(Packet*, unsigned int, unsigned int);

#endif
