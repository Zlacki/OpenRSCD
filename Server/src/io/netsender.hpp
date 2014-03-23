/*
 * netsender.hpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#ifndef _NETSENDER_HPP
#define _NETSENDER_HPP

#include <string>
#include <memory>

namespace RSC {
	namespace Model {
		class Player;
	}
	namespace IO {
		class NetSender {
			private:
				std::shared_ptr<Model::Player> owner;

			public:
				NetSender(std::shared_ptr<Model::Player>);
				void sendLogout();
				void sendPlayerPositions();
				void sendPlayerAppearances();
				void sendInventory();
				void sendEquipmentBonuses();
				void sendStats();
				void sendServerMessage(std::string);
				void sendSocialOptions();
				void sendClientOptions();
				void sendPlaneData();
				void sendSessionResponse();
				void sendLoginResponse(int);
				void sendPong();
		};
	}
}

#endif
