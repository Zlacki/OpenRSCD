/*
 * packet.cpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#include <cstring>
#include "packet.hpp"
#include "../util.hpp"

namespace RSC {
	namespace IO {
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

		Packet::Packet(unsigned char opCode) {
			this->opCode = opCode;
			offset = 0;
			bitPosition = 0;
			buffer = new char[10000];
		}

		Packet::~Packet() {
			delete[] buffer;
		}

		unsigned char Packet::getOpCode() {
			return opCode;
		}

		void Packet::setOpCode(unsigned char opCode) {
			this->opCode = opCode;
		}

		char *Packet::getBuffer() {
			return buffer;
		}

		void Packet::setBuffer(char *data) {
			delete[] buffer;
			buffer = data;
		}

		unsigned short Packet::getLength() {
			return length;
		}

		void Packet::setLength(unsigned short length) {
			this->length = length;
		}

		char Packet::readByte() {
			if(++offset > length)
				warning("Buffer read out of bounds!");

			return buffer[offset];
		}

		unsigned char Packet::readUnsignedByte() {
			return readByte() & 0xFF;
		}

		unsigned short Packet::readShort() {
			return (readUnsignedByte() << 8) + readUnsignedByte();
		}

		unsigned int Packet::readInteger() {
			return (readUnsignedByte() << 24) + (readUnsignedByte() << 16) + (readUnsignedByte() << 8) +  readUnsignedByte();
		}

		unsigned long Packet::readLong() {
			return (((long) readInteger() & 0xFFFFFFFFL) << 32) + ((long) readInteger() & 0xFFFFFFFFL);
		}

		std::string Packet::readString() {
			return readString(length - offset);
		}

		std::string Packet::readString(int length) {
			char *s = (char*)buffer + offset;
			offset += length;
			return std::string(s, length);
		}

		void Packet::writeUnsignedByte(unsigned char val) {
			buffer[offset++] = val & 0xFF;
		}

		void Packet::writeByte(char val) {
			buffer[offset++] = val;
		}

		void Packet::writeBytes(char *data, int offset, int length) {
			for(int i = 0; i < length; i++)
				writeByte(data[i + offset]);
		}

		void Packet::writeShort(short val) {
			writeByte(val >> 8);
			writeByte(val);
		}

		void Packet::writeInteger(int val) {
			writeByte(val >> 24);
			writeByte(val >> 16);
			writeByte(val >> 8);
			writeByte(val);
		}

		void Packet::writeOffsettedInteger(int val) {
			writeByte((val >> 24) + 128);
			writeByte(val >> 16);
			writeByte(val >> 8);
			writeByte(val);
		}

		void Packet::writeLong(long val) {
			writeByte(val >> 56);
			writeByte(val >> 48);
			writeByte(val >> 40);
			writeByte(val >> 32);
			writeByte(val >> 24);
			writeByte(val >> 16);
			writeByte(val >> 8);
			writeByte(val);
		}

		void Packet::skip(int length) {
			this->offset += length;
		}

		void Packet::writeString(std::string str) {
			writeBytes((char *)str.data(), 0, str.length());
		}

		void Packet::writeBits(unsigned int val, unsigned int num) {
			unsigned int bytePosition = (bitPosition >> 3);
			unsigned int bitOffset = 8 - (bitPosition & 7);
			bitPosition += num;
			offset = (bitPosition + 7) / 8;

			for(; num > bitOffset; bitOffset = 8) {
				buffer[bytePosition] &= ~BITMASKS[bitOffset];
				buffer[bytePosition++] |= (val >> (num - bitOffset)) & BITMASKS[bitOffset];

				num -= bitOffset;
			}

			if(num == bitOffset) {
				buffer[bytePosition] &= ~BITMASKS[bitOffset];
				buffer[bytePosition] |= val & BITMASKS[bitOffset];
			} else {
				buffer[bytePosition] &= ~(BITMASKS[num] << (bitOffset - num));
				buffer[bytePosition] |= (val & BITMASKS[num]) << (bitOffset - num);
			}
		}

		void Packet::encodeForClient() {
			length = offset + 1;
			memmove(buffer + 3, buffer, length);
			buffer[0] = (char) (length >> 8);
			buffer[1] = (char) length;
			buffer[2] = opCode;
		}
	}
}
