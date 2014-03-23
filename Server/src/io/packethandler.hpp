/*
 * packethandler.hpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#ifndef _PACKETHANDLER_HPP
#define _PACKETHANDLER_HPP

#include <memory>
#include "../model/player.hpp"
#include "packet.hpp"
namespace RSC {
	namespace IO {
		extern void (*handlePacket[256])(std::shared_ptr<Model::Player>, std::shared_ptr<IO::Packet>);
	}
}
#endif
