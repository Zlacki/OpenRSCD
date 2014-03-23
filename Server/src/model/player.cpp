#include <iostream>
#include <boost/bind.hpp>
#include "collections.hpp"
#include "player.hpp"
#include "region.hpp"
#include "../util.hpp"

namespace RSC {
	extern std::shared_ptr<Collections> collections;
	extern std::shared_ptr<RegionManager> regionManager;
	namespace Model {
		Player::Player(IO::connection_ptr connection__, unsigned short index) : Mob(index, 0, 0), connection_(connection__) {
			username = "null";
		}

		std::string Player::getUsername() {
			return username;
		}

		void Player::setUsername(std::string username) {
			this->username = username;
		}

		long Player::getUsernameHash() {
			return usernameHash;
		}

		void Player::setUsernameHash(long usernameHash) {
			this->usernameHash = usernameHash;
		}

		void Player::setLocation(unsigned short x, unsigned short y) {
			this->x = x;
			this->y = y;
			regionManager->getRegion(x, y)->addPlayer(shared_from_this());
		}

		bool Player::withinRange(std::shared_ptr<Player> player) {
			return withinRange(player->getX(), player->getY());
		}

		bool Player::withinRange(unsigned short x, unsigned short y) {
			short xd = this->x - x;
			short yd = this->y - y;

			return xd <= 16 && xd >= -15 && yd <= 16 && yd >= -15;
		}

		bool Player::isPublicChatBlocked() {
			return socialOptions[0];
		}

		void Player::setPublicChatBlocked(bool b) {
			socialOptions[0] = b;
		}

		bool Player::isPrivateChatBlocked() {
			return socialOptions[1];
		}

		void Player::setPrivateChatBlocked(bool b) {
			socialOptions[1] = b;
		}

		bool Player::isTradeBlocked() {
			return socialOptions[2];
		}

		void Player::setTradeBlocked(bool b) {
			socialOptions[2] = b;
		}

		bool Player::isDuelBlocked() {
			return socialOptions[3];
		}

		void Player::setDuelBlocked(bool b) {
			socialOptions[3] = b;
		}

		bool *Player::getSocialOptions() {
			return socialOptions;
		}

		bool *Player::getClientOptions() {
			return clientOptions;
		}

		unsigned long *Player::getExperiences() {
			return skillExperiences;
		}

		unsigned long Player::getExperience(int index) {
			return skillExperiences[index];
		}

		void Player::setExperience(int index, unsigned long experience) {
			skillExperiences[index] = experience;
		}

		unsigned char *Player::getCurrentLevels() {
			return skillLevels;
		}

		unsigned char Player::getCurrentLevel(int index) {
			return skillLevels[index];
		}

		void Player::setCurrentLevel(int index, unsigned char level) {
			skillLevels[index] = level;
		}

		unsigned char *Player::getEquipmentBonuses() {
			return equipmentBonuses;
		}

		unsigned int *Player::getWornItems() {
			return wornItems;
		}

		void Player::updateWornItems(unsigned int index, int sprite) {
			wornItems[index] = sprite;
		}

		std::shared_ptr<NetSender> Player::getNetSender() {
			return netSender;
		}

		void Player::setNetSender(std::shared_ptr<NetSender> netSender) {
			this->netSender = netSender;
		}

		std::shared_ptr<Inventory> Player::getInventory() {
			return inventory;
		}

		void Player::setInventory(std::shared_ptr<Inventory> inventory) {
			this->inventory = inventory;
		}

		unsigned char Player::getDirection() {
			return direction;
		}

		void Player::setDirection(unsigned char direction) {
			this->direction = direction;
		}

		void Player::sendRawData(char *data, int length) {
			connection_->handle_write(data, length);
		}

		void Player::sendPacket(std::shared_ptr<Packet> packet) {
			packet->encodeForClient();
			connection_->handle_write((char *) packet->getBuffer(), packet->getLength() + 2);
		}

		bool Player::equals(std::shared_ptr<Player> player) {
			return player->getIndex() == getIndex();
		}
	}
}
