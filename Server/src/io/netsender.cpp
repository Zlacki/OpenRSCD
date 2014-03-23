/*
 * netsender.cpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#include <iostream>
#include "netsender.hpp"
#include "../model/collections.hpp"
#include "../model/region.hpp"
#include "../model/player.hpp"
#include "packet.hpp"

namespace RSC {
	extern std::shared_ptr<Collections> collections;
	extern std::shared_ptr<RegionManager> regionManager;

	namespace IO {
		NetSender::NetSender(std::shared_ptr<Player> player) {
			owner = player;
		}

		void NetSender::sendLogout() {
			owner->sendPacket(std::make_shared<Packet>(4));
		}

		void NetSender::sendPlayerPositions() {
			std::shared_ptr<Packet> packet = std::make_shared<Packet>(191);
			packet->writeBits(owner->getX(), 11);
			packet->writeBits(owner->getY(), 13);
			packet->writeBits(owner->getDirection(), 4);
			packet->writeBits(0, 8);

			auto list = regionManager->getViewablePlayers(owner);
			for(auto it = list->begin(); it != list->end(); ++it) {
				std::shared_ptr<Player> player = *it;
				if(owner->withinRange(player)) {
					std::cout << "‘" << owner->getUsername() << "’ recieved player position packet for ‘" << player->getUsername() << "’." << std::endl;
					packet->writeBits(player->getIndex(), 11);
					int xOffset = player->getX() - owner->getX();
					int yOffset = player->getY() - owner->getY();
					packet->writeBits(xOffset < 0 ? xOffset + 32 : xOffset, 5);
					packet->writeBits(yOffset < 0 ? yOffset + 32 : yOffset, 5);
					packet->writeBits(player->getDirection(), 4);
				}
			}

			delete list;

			owner->sendPacket(packet);
		}

		void NetSender::sendPlayerAppearances() {
			std::shared_ptr<Packet> packet = std::make_shared<Packet>(234);
			auto list = regionManager->getViewablePlayers(owner);
			packet->writeShort(list->size());

			for(auto it = list->begin(); it != list->end(); ++it) {
				std::shared_ptr<Player> player = *it;
				if(owner->withinRange(player)) {
					std::cout << "‘" << owner->getUsername() << "’ recieved player appearance packet for ‘" << player->getUsername() << "’." << std::endl;
					packet->writeShort(player->getIndex());
					packet->writeByte(5);
					packet->writeShort(-1); /* TODO: Appearance ID */
					packet->writeLong(player->getUsernameHash());
					packet->writeByte(12);
					for(int i = 0; i < 12; i++)
						packet->writeByte(player->getWornItems()[i]);
					packet->writeByte(0);
					packet->writeByte(0);
					packet->writeByte(0);
					packet->writeByte(0);
					packet->writeByte(3);
					packet->writeByte(0);
				}
			}

			delete list;

			owner->sendPacket(packet);
		}

		void NetSender::sendInventory() {
			std::shared_ptr<Packet> packet = std::make_shared<Packet>(53);
			packet->writeByte(owner->getInventory()->size());

			for(int i = 0; i < owner->getInventory()->size(); i++) {
				std::shared_ptr<Item> item = owner->getInventory()->get(i);
				packet->writeShort(item->getID() + (item->isWorn() ? 0x8000 : 0));
				if(collections->getDefinitionHandler()->getItemDefinitions()[item->getID()]->stackable) {
					if(item->getAmount() < 128)
						packet->writeByte(item->getAmount());
					else
						packet->writeInteger(item->getAmount());
				}
			}

			owner->sendPacket(packet);
		}

		void NetSender::sendEquipmentBonuses() {
			std::shared_ptr<Packet> packet = std::make_shared<Packet>(153);
			for(int i = 0; i < 5; i++)
				packet->writeByte(owner->getEquipmentBonuses()[i]);
			owner->sendPacket(packet);
		}

		void NetSender::sendPong() {
			owner->sendPacket(std::make_shared<Packet>(67));
		}

		void NetSender::sendStats() {
			std::shared_ptr<Packet> packet = std::make_shared<Packet>(156);

			for(int i = 0; i < 18; i++)
				packet->writeByte(owner->getCurrentLevel(i));
			for(int i = 0; i < 18; i++)
				packet->writeByte(owner->getCurrentLevel(i)); /* max level */
			for(int i = 0; i < 18; i++)
				packet->writeInteger(owner->getExperience(i));

			packet->writeUnsignedByte(200); /* quest points */

			owner->sendPacket(packet);
		}

		void NetSender::sendServerMessage(std::string message) {
			std::shared_ptr<Packet> packet = std::make_shared<Packet>(89);
			packet->writeBytes((char *) message.data(), 0, message.length());
			owner->sendPacket(packet);
		}

		void NetSender::sendSocialOptions() {
			std::shared_ptr<Packet> packet = std::make_shared<Packet>(51);
			for(int i = 0; i < 4; i++)
				packet->writeByte(owner->getSocialOptions()[i]);
			owner->sendPacket(packet);
		}

		void NetSender::sendClientOptions() {
			std::shared_ptr<Packet> packet = std::make_shared<Packet>(240);
			for(int i = 0; i < 3; i++)
				packet->writeByte(owner->getClientOptions()[i]);
			owner->sendPacket(packet);
		}

		void NetSender::sendPlaneData() {
			std::shared_ptr<Packet> packet = std::make_shared<Packet>(25);
			packet->writeShort(owner->getIndex());
			packet->writeShort(2304);
			packet->writeShort(1776);
			packet->writeShort(0);
			packet->writeShort(944);
			owner->sendPacket(packet);
		}

		void NetSender::sendSessionResponse() {
			int64_t val = rand();
			val <<= 56;
			val |= rand();
			val <<= 48;
			val |= rand();
			val <<= 40;
			val |= rand();
			val <<= 32;
			val |= rand();
			val <<= 24;
			val |= rand();
			val <<= 16;
			val |= rand();
			val <<= 8;
			val |= rand();
			owner->sendRawData(new char[8] {
				(char) (val >> 56),
				(char) (val >> 48),
				(char) (val >> 40),
				(char) (val >> 32),
				(char) (val >> 24),
				(char) (val >> 16),
				(char) (val >> 8),
				(char) val }
			, 8);
		}

		void NetSender::sendLoginResponse(int res) {
			owner->sendRawData(new char[1] { (char) res }, 1);
		}
	}
}
