#ifndef _PLAYER_HPP
#define _PLAYER_HPP

#include <memory>
#include <string>
#include "inventory.hpp"
#include "mob.hpp"
#include "../io/packet.hpp"
#include "../io/netsender.hpp"
#include "../io/connection.hpp"

namespace RSC {
	using namespace IO;
	namespace Model {
		class Player : public Mob, public std::enable_shared_from_this<Player> {
        private:
                std::string username;
                long usernameHash;
                bool socialOptions[4];
                bool clientOptions[3];
                unsigned char skillLevels[18];
                unsigned long skillExperiences[18];
                unsigned char equipmentBonuses[5];
                unsigned int wornItems[12];
                unsigned char direction;
                std::shared_ptr<Inventory> inventory;
                std::shared_ptr<NetSender> netSender;
                connection_ptr connection_;

        public:
                Player(connection_ptr, unsigned short);
                std::string getUsername();
                void setUsername(std::string);
                long getUsernameHash();
                void setUsernameHash(long);
                void sendRawData(char*, int);
                void sendPacket(std::shared_ptr<Packet>);
                void setLocation(unsigned short, unsigned short);
                bool withinRange(std::shared_ptr<Player>);
                bool withinRange(unsigned short, unsigned short);
                bool isPublicChatBlocked();
                void setPublicChatBlocked(bool);
                bool isPrivateChatBlocked();
                void setPrivateChatBlocked(bool);
                bool isTradeBlocked();
                void setTradeBlocked(bool);
                bool isDuelBlocked();
                void setDuelBlocked(bool);
                bool *getClientOptions();
                bool *getSocialOptions();
                unsigned long *getExperiences();
                unsigned long getExperience(int);
                void setExperience(int, unsigned long);
                unsigned char *getCurrentLevels();
                unsigned char getCurrentLevel(int);
                void setCurrentLevel(int, unsigned char);
                std::shared_ptr<NetSender> getNetSender();
                void setNetSender(std::shared_ptr<NetSender>);
                std::shared_ptr<Inventory> getInventory();
                void setInventory(std::shared_ptr<Inventory>);
                unsigned char *getEquipmentBonuses();
                unsigned int *getWornItems();
                void updateWornItems(unsigned int, int);
                unsigned char getDirection();
                void setDirection(unsigned char);
                bool equals(std::shared_ptr<Player>);
		};
	}
}

#endif
