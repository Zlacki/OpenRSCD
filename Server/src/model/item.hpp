/*
 * item.hpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#ifndef _ITEM_HPP
#define _ITEM_HPP

namespace RSC {
	namespace Model {
		class Item {
			private:
				unsigned short id;
				unsigned long amount;
				bool worn;

			public:
				Item(unsigned short, unsigned long, bool);
				unsigned short getID();
				unsigned long getAmount();
				void setAmount(unsigned long);
				bool isWorn();
				void setWorn(bool);
		};
	}
}

#endif
