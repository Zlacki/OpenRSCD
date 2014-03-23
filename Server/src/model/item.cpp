/*
 * item.cpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#include "item.hpp"


namespace RSC {
	namespace Model {
		Item::Item(unsigned short id, unsigned long amount, bool worn) {
			this->id = id;
			this->amount = amount;
			this->worn = worn;
		}

		unsigned short Item::getID() {
			return id;
		}

		unsigned long Item::getAmount() {
			return amount;
		}

		void Item::setAmount(unsigned long amount) {
			this->amount = amount;
		}

		bool Item::isWorn() {
			return worn;
		}

		void Item::setWorn(bool worn) {
			this->worn = worn;
		}
	}
}
