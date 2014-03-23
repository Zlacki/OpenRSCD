/*
 * inventory.cpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#include <climits>
#include "inventory.hpp"
#include "collections.hpp"

namespace RSC {
	extern std::shared_ptr<Collections> collections;
	namespace Model {
		Inventory::Inventory(std::shared_ptr<Player> player) {
			owner = player;
		}

		std::list<std::shared_ptr<Item>> Inventory::getItems() {
			return items;
		}

		void Inventory::add(std::shared_ptr<Item> item) {
			if(item->getAmount() <= 0)
				return;

			if(collections->getDefinitionHandler()->getItemDefinitions()[item->getID()]->stackable)
				for(auto it = items.begin(); it != items.end(); ++it) {
					std::shared_ptr<Item> i = *it;
					if(i->getID() == item->getID()) {
						unsigned long total = i->getAmount() + item->getAmount();
						if(total < ULONG_MAX)
							i->setAmount(total);
						else
							owner->getNetSender()->sendServerMessage("You don't have enough room to hold this item.");

						return;
					}
				}

			if(full()) {
				owner->getNetSender()->sendServerMessage("TODO: Drop added inv items if inv is full.");
				return;
			}

			items.push_back(item);
		}

		void Inventory::remove(std::shared_ptr<Item> item) {
			for(auto it = items.end(); it != items.begin(); --it) {
				std::shared_ptr<Item> i = *it;
				if(item->getID() == i->getID()) {
					if(collections->getDefinitionHandler()->getItemDefinitions()[item->getID()]->stackable && item->getAmount() < i->getAmount())
						i->setAmount(i->getAmount() - item->getAmount());
					else {
						if(i->isWorn()) {
							i->setWorn(false);
							if(owner.get() != NULL) {
								owner->getNetSender()->sendEquipmentBonuses();
								owner->updateWornItems(collections->getDefinitionHandler()->getItemDefinitions()[item->getID()]->wearableSlot,
										collections->getDefinitionHandler()->getItemDefinitions()[item->getID()]->sprite);
							}
						}
						items.erase(it);
					}
				}
			}
		}

		void Inventory::remove(unsigned int index) {
			std::shared_ptr<Item> item = get(index);
			if(item.get() == NULL)
				return;

			remove(item);
		}

		unsigned char Inventory::getFirstIndexByID(unsigned short id) {
			unsigned char index = 0;
			for(auto it = items.begin(); it != items.end(); ++it) {
				std::shared_ptr<Item> i = *it;
				if(i->getID() == id)
					return index;
				index++;
			}

			return 255;
		}

		unsigned long Inventory::countID(unsigned short id) {
			unsigned long total = 0;
			for(auto it = items.begin(); it != items.end(); ++it) {
				std::shared_ptr<Item> i = *it;
				if(i->getID() == id)
					total += i->getAmount();
			}

			return total;
		}

		bool Inventory::full() {
			return items.size() >= 30;
		}

		bool Inventory::contains(std::shared_ptr<Item> item) {
			for(auto it = items.begin(); it != items.end(); ++it) {
				std::shared_ptr<Item> i = *it;
				if(i->getID() == item->getID())
					return true;
			}

			return false;
		}

		std::shared_ptr<Item> Inventory::get(std::shared_ptr<Item> item) {
			for(auto it = items.begin(); it != items.end(); ++it) {
				std::shared_ptr<Item> i = *it;
				if(i->getID() == item->getID())
					return i;
			}

			return NULL;
		}

		std::shared_ptr<Item> Inventory::get(unsigned int index) {
			if(index >= items.size())
				return NULL;

			auto it = items.begin();
			for(unsigned int i = 0; i < index; i++)
				it++;

			return *it;
		}

		unsigned char Inventory::size() {
			return items.size();
		}

		unsigned char Inventory::getRequiredSlots(std::list<std::shared_ptr<Item>> list) {
			unsigned char requiredSlots = 0;
			for(auto it = list.begin(); it != list.end(); ++it) {
				std::shared_ptr<Item> i = *it;
				if(collections->getDefinitionHandler()->getItemDefinitions()[i->getID()]->stackable && contains(i))
					continue;

				requiredSlots++;
			}

			return requiredSlots;
		}

		unsigned char Inventory::getRequiredSlots(std::shared_ptr<Item> item) {
			return ((collections->getDefinitionHandler()->getItemDefinitions()[item->getID()]->stackable && contains(item)) ? 0 : 1);
		}

		bool Inventory::canHold(std::shared_ptr<Item> item) {
			return (30 - items.size()) >= getRequiredSlots(item);
		}

		bool Inventory::canHold(std::list<std::shared_ptr<Item>> list) {
			return (30 - items.size()) >= getRequiredSlots(list);
		}
	}
}
