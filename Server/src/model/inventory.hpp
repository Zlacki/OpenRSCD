/*
 * inventory.hpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#ifndef _INVENTORY_HPP
#define _INVENTORY_HPP

#include <memory>
#include <list>
#include "item.hpp"

namespace RSC {
	namespace Model {
		class Player;
		class Inventory {
			private:
				std::shared_ptr<Player> owner;
				std::list<std::shared_ptr<Item>> items;

			public:
				Inventory(std::shared_ptr<Player>);
				std::list<std::shared_ptr<Item>> getItems();
				void add(std::shared_ptr<Item>);
				void remove(std::shared_ptr<Item>);
				void remove(unsigned int);
				unsigned char getFirstIndexByID(unsigned short);
				unsigned long countID(unsigned short);
				bool full();
				bool contains(std::shared_ptr<Item>);
				std::shared_ptr<Item> get(std::shared_ptr<Item>);
				std::shared_ptr<Item> get(unsigned int);
				unsigned char size();
				unsigned char getRequiredSlots(std::list<std::shared_ptr<Item>>);
				unsigned char getRequiredSlots(std::shared_ptr<Item>);
				bool canHold(std::shared_ptr<Item>);
				bool canHold(std::list<std::shared_ptr<Item>>);
		};
	}
}

#endif
