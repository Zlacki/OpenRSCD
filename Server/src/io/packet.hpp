/*
 * packet.hpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#ifndef _PACKET_HPP
#define _PACKET_HPP

#include <string>
namespace RSC {
	namespace IO {
		class Packet {
			private:
				char *buffer;
				unsigned char opCode;
				unsigned short offset;
				unsigned short length;
				unsigned int bitPosition;

			public:
				Packet(unsigned char);
				~Packet();
				unsigned char getOpCode();
				void setOpCode(unsigned char);
				char *getBuffer();
				void setBuffer(char*);
				unsigned short getLength();
				void setLength(unsigned short);
				char readByte();
				unsigned char readUnsignedByte();
				unsigned short readShort();
				unsigned int readInteger();
				unsigned long readLong();
				std::string readString();
				std::string readString(int);
				void writeUnsignedByte(unsigned char);
				void writeByte(char);
				void writeBytes(char*, int, int);
				void writeShort(short);
				void writeInteger(int);
				void writeOffsettedInteger(int);
				void writeLong(long);
				void writeString(std::string);
				void writeBits(unsigned int, unsigned int);
				void skip(int);
				void encodeForClient();
		};
	}
}

#endif
