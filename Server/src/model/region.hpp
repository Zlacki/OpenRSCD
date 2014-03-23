/*
 * region.hpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#ifndef _REGION_HPP
#define _REGION_HPP

#include <list>
#include <memory>
#include "player.hpp"

namespace RSC {
	namespace Model {
		#define REGION_SIZE 32
		#define LOWER_BOUND (REGION_SIZE / 2) - 1
		#define HORIZONTAL_REGIONS 944 / REGION_SIZE
		#define VERTICAL_REGIONS 3776 / REGION_SIZE

		class Region {
			private:
				std::list<std::shared_ptr<Player>> *players;

			public:
				Region();
				std::list<std::shared_ptr<Player>> *getPlayers();
				void addPlayer(std::shared_ptr<Player>);
				void removePlayer(std::shared_ptr<Player>);
		};

		class RegionManager {
			private:
				std::shared_ptr<Region> regions[HORIZONTAL_REGIONS][VERTICAL_REGIONS];

			public:
				RegionManager();
				std::shared_ptr<Region> getRegion(int, int);
				std::shared_ptr<Region> *getViewableRegions(int, int);
				std::list<std::shared_ptr<Player>> *getViewablePlayers(std::shared_ptr<Player>);
		};
	}
}

#endif
