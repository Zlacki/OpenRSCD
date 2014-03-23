/*
 * packethandler.cpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#include <iostream>
#include <boost/algorithm/string.hpp>
#include "packethandler.hpp"
#include "../model/collections.hpp"
#include "../util.hpp"
namespace RSC {
	extern std::shared_ptr<Collections> collections;
	namespace IO {
		static void handlePing(std::shared_ptr<Player> player, std::shared_ptr<Packet> packet) {
			player->getNetSender()->sendPong();
		}

		static void handleLogoutRequest(std::shared_ptr<Player> player, std::shared_ptr<Packet> packet) {
			player->getNetSender()->sendLogout();
		}

		static void handleLogout(std::shared_ptr<Player> player, std::shared_ptr<Packet> packet) {
			collections->removePlayer(player->getIndex());
		}

		static void handleLogin(std::shared_ptr<Player> player, std::shared_ptr<Packet> packet) {
			short version = packet->readInteger();
			if(version < VERSION) {
				std::cout << "Client version mismatch: " << version << " != " << VERSION << std::endl;
				player->getNetSender()->sendLoginResponse(5);
				return;
			}
			uint64_t userHash = packet->readLong();
			player->setUsernameHash(userHash);
			player->setUsername(hashToUsername(userHash));
			auto l = packet->readByte();
			packet->skip(1);
			std::string password = packet->readString(l);
			boost::algorithm::trim_right(password);
			for(int i = 0; i < 4; i++)
				player->getSocialOptions()[i] = 0;
			for(int i = 0; i < 3; i++)
				player->getClientOptions()[i] = 0;
			for(int i = 0; i < 18; i++)
				if(i == 3) {
					player->setExperience(i, 1154L);
					player->setCurrentLevel(i, 10);
				} else {
					player->setExperience(i, 0L);
					player->setCurrentLevel(i, 1);
				}
			player->setInventory(std::make_shared<Inventory>(player));
			player->getInventory()->add(std::make_shared<Item>(81, 1, false));
			player->getInventory()->add(std::make_shared<Item>(581, 1, false));
			player->getInventory()->add(std::make_shared<Item>(10, 20, false));
			player->updateWornItems(0, 4);
			player->updateWornItems(1, 5);
			player->updateWornItems(2, 3);
			for(int i = 3; i < 12; i++)
				player->updateWornItems(i, 0);
			for(int i = 0; i < 5; i++)
				player->getEquipmentBonuses()[i] = 1;
			player->setDirection(1);
			std::cout << "Regestering player: ‘" << player->getUsername() << "’, Password: '" << password << "';" << std::endl;
			player->getNetSender()->sendLoginResponse(0);
			player->getNetSender()->sendPlaneData();
			player->getNetSender()->sendSocialOptions();
			player->getNetSender()->sendClientOptions();
			player->getNetSender()->sendServerMessage("Welcome to OpenRSCD");
			player->getNetSender()->sendStats();
			player->getNetSender()->sendInventory();
			player->getNetSender()->sendEquipmentBonuses();
			player->setLocation(216, 453 - (rand() % 5));
			collections->sendRegionalPlayerPositionUpdate(player);
			collections->sendRegionalPlayerAppearanceUpdate(player);
		}

		static void handleSessionRequest(std::shared_ptr<Player> player, std::shared_ptr<Packet> packet) {
			packet->readByte();
			player->getNetSender()->sendSessionResponse();
		}

		static void unhandled(std::shared_ptr<Player> player, std::shared_ptr<Packet> packet) {
			std::cout << "Unhandled packet: " << (int) packet->getOpCode() << std::endl;
		}

		void (*handlePacket[256])(std::shared_ptr<Player> player, std::shared_ptr<Packet> packet) = {
			&handleLogin, /* 0 */
			&unhandled, /* 1 */
			&unhandled, /* 2 */
			&unhandled, /* 3 */
			&unhandled, /* 4 */
			&unhandled, /* 5 */
			&unhandled, /* 6 */
			&unhandled, /* 7 */
			&unhandled, /* 8 */
			&unhandled, /* 9 */
			&unhandled, /* 10 */
			&unhandled, /* 11 */
			&unhandled, /* 12 */
			&unhandled, /* 13 */
			&unhandled, /* 14 */
			&unhandled, /* 15 */
			&unhandled, /* 16 */
			&unhandled, /* 17 */
			&unhandled, /* 18 */
			&unhandled, /* 19 */
			&unhandled, /* 20 */
			&unhandled, /* 21 */
			&unhandled, /* 22 */
			&unhandled, /* 23 */
			&unhandled, /* 24 */
			&unhandled, /* 25 */
			&unhandled, /* 26 */
			&unhandled, /* 27 */
			&unhandled, /* 28 */
			&unhandled, /* 29 */
			&unhandled, /* 30 */
			&handleLogout, /* 31 */
			&handleSessionRequest, /* 32 */
			&unhandled, /* 33 */
			&unhandled, /* 34 */
			&unhandled, /* 35 */
			&unhandled, /* 36 */
			&unhandled, /* 37 */
			&unhandled, /* 38 */
			&unhandled, /* 39 */
			&unhandled, /* 40 */
			&unhandled, /* 41 */
			&unhandled, /* 42 */
			&unhandled, /* 43 */
			&unhandled, /* 44 */
			&unhandled, /* 45 */
			&unhandled, /* 46 */
			&unhandled, /* 47 */
			&unhandled, /* 48 */
			&unhandled, /* 49 */
			&unhandled, /* 50 */
			&unhandled, /* 51 */
			&unhandled, /* 52 */
			&unhandled, /* 53 */
			&unhandled, /* 54 */
			&unhandled, /* 55 */
			&unhandled, /* 56 */
			&unhandled, /* 57 */
			&unhandled, /* 58 */
			&unhandled, /* 59 */
			&unhandled, /* 60 */
			&unhandled, /* 61 */
			&unhandled, /* 62 */
			&unhandled, /* 63 */
			&unhandled, /* 64 */
			&unhandled, /* 65 */
			&unhandled, /* 66 */
			&handlePing, /* 67 */
			&unhandled, /* 68 */
			&unhandled, /* 69 */
			&unhandled, /* 70 */
			&unhandled, /* 71 */
			&unhandled, /* 72 */
			&unhandled, /* 73 */
			&unhandled, /* 74 */
			&unhandled, /* 75 */
			&unhandled, /* 76 */
			&unhandled, /* 77 */
			&unhandled, /* 78 */
			&unhandled, /* 79 */
			&unhandled, /* 80 */
			&unhandled, /* 81 */
			&unhandled, /* 82 */
			&unhandled, /* 83 */
			&unhandled, /* 84 */
			&unhandled, /* 85 */
			&unhandled, /* 86 */
			&unhandled, /* 87 */
			&unhandled, /* 88 */
			&unhandled, /* 89 */
			&unhandled, /* 90 */
			&unhandled, /* 91 */
			&unhandled, /* 92 */
			&unhandled, /* 93 */
			&unhandled, /* 94 */
			&unhandled, /* 95 */
			&unhandled, /* 96 */
			&unhandled, /* 97 */
			&unhandled, /* 98 */
			&unhandled, /* 99 */
			&unhandled, /* 100 */
			&unhandled, /* 101 */
			&handleLogoutRequest, /* 102 */
			&unhandled, /* 103 */
			&unhandled, /* 104 */
			&unhandled, /* 105 */
			&unhandled, /* 106 */
			&unhandled, /* 107 */
			&unhandled, /* 108 */
			&unhandled, /* 109 */
			&unhandled, /* 110 */
			&unhandled, /* 111 */
			&unhandled, /* 112 */
			&unhandled, /* 113 */
			&unhandled, /* 114 */
			&unhandled, /* 115 */
			&unhandled, /* 116 */
			&unhandled, /* 117 */
			&unhandled, /* 118 */
			&unhandled, /* 119 */
			&unhandled, /* 120 */
			&unhandled, /* 121 */
			&unhandled, /* 122 */
			&unhandled, /* 123 */
			&unhandled, /* 124 */
			&unhandled, /* 125 */
			&unhandled, /* 126 */
			&unhandled, /* 127 */
			&unhandled, /* 128 */
			&unhandled, /* 129 */
			&unhandled, /* 130 */
			&unhandled, /* 131 */
			&unhandled, /* 132 */
			&unhandled, /* 133 */
			&unhandled, /* 134 */
			&unhandled, /* 135 */
			&unhandled, /* 136 */
			&unhandled, /* 137 */
			&unhandled, /* 138 */
			&unhandled, /* 139 */
			&unhandled, /* 140 */
			&unhandled, /* 141 */
			&unhandled, /* 142 */
			&unhandled, /* 143 */
			&unhandled, /* 144 */
			&unhandled, /* 145 */
			&unhandled, /* 146 */
			&unhandled, /* 147 */
			&unhandled, /* 148 */
			&unhandled, /* 149 */
			&unhandled, /* 150 */
			&unhandled, /* 151 */
			&unhandled, /* 152 */
			&unhandled, /* 153 */
			&unhandled, /* 154 */
			&unhandled, /* 155 */
			&unhandled, /* 156 */
			&unhandled, /* 157 */
			&unhandled, /* 158 */
			&unhandled, /* 159 */
			&unhandled, /* 160 */
			&unhandled, /* 161 */
			&unhandled, /* 162 */
			&unhandled, /* 163 */
			&unhandled, /* 164 */
			&unhandled, /* 165 */
			&unhandled, /* 166 */
			&unhandled, /* 167 */
			&unhandled, /* 168 */
			&unhandled, /* 169 */
			&unhandled, /* 170 */
			&unhandled, /* 171 */
			&unhandled, /* 172 */
			&unhandled, /* 173 */
			&unhandled, /* 174 */
			&unhandled, /* 175 */
			&unhandled, /* 176 */
			&unhandled, /* 177 */
			&unhandled, /* 178 */
			&unhandled, /* 179 */
			&unhandled, /* 180 */
			&unhandled, /* 181 */
			&unhandled, /* 182 */
			&unhandled, /* 183 */
			&unhandled, /* 184 */
			&unhandled, /* 185 */
			&unhandled, /* 186 */
			&unhandled, /* 187 */
			&unhandled, /* 188 */
			&unhandled, /* 189 */
			&unhandled, /* 190 */
			&unhandled, /* 191 */
			&unhandled, /* 192 */
			&unhandled, /* 193 */
			&unhandled, /* 194 */
			&unhandled, /* 195 */
			&unhandled, /* 196 */
			&unhandled, /* 197 */
			&unhandled, /* 198 */
			&unhandled, /* 199 */
			&unhandled, /* 200 */
			&unhandled, /* 201 */
			&unhandled, /* 202 */
			&unhandled, /* 203 */
			&unhandled, /* 204 */
			&unhandled, /* 205 */
			&unhandled, /* 206 */
			&unhandled, /* 207 */
			&unhandled, /* 208 */
			&unhandled, /* 209 */
			&unhandled, /* 210 */
			&unhandled, /* 211 */
			&unhandled, /* 212 */
			&unhandled, /* 213 */
			&unhandled, /* 214 */
			&unhandled, /* 215 */
			&unhandled, /* 216 */
			&unhandled, /* 217 */
			&unhandled, /* 218 */
			&unhandled, /* 219 */
			&unhandled, /* 220 */
			&unhandled, /* 221 */
			&unhandled, /* 222 */
			&unhandled, /* 223 */
			&unhandled, /* 224 */
			&unhandled, /* 225 */
			&unhandled, /* 226 */
			&unhandled, /* 227 */
			&unhandled, /* 228 */
			&unhandled, /* 229 */
			&unhandled, /* 230 */
			&unhandled, /* 231 */
			&unhandled, /* 232 */
			&unhandled, /* 233 */
			&unhandled, /* 234 */
			&unhandled, /* 235 */
			&unhandled, /* 236 */
			&unhandled, /* 237 */
			&unhandled, /* 238 */
			&unhandled, /* 239 */
			&unhandled, /* 240 */
			&unhandled, /* 241 */
			&unhandled, /* 242 */
			&unhandled, /* 243 */
			&unhandled, /* 244 */
			&unhandled, /* 245 */
			&unhandled, /* 246 */
			&unhandled, /* 247 */
			&unhandled, /* 248 */
			&unhandled, /* 249 */
			&unhandled, /* 250 */
			&unhandled, /* 251 */
			&unhandled, /* 252 */
			&unhandled, /* 253 */
			&unhandled, /* 254 */
			&unhandled  /* 255 */
		};
	}
}
