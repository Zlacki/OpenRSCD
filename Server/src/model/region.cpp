/*
 * region.cpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#include <iostream>
#include "region.hpp"

namespace RSC {
	namespace Model {
		Region::Region() {
			players = new std::list<std::shared_ptr<Player>>();
		}

		std::list<std::shared_ptr<Player>> *Region::getPlayers() {
			return players;
		}

		void Region::addPlayer(std::shared_ptr<Player> player) {
			players->push_back(player);
		}

		void Region::removePlayer(std::shared_ptr<Player> player) {
			players->remove(player);
		}

		RegionManager::RegionManager() {
			for(int x = 0; x < HORIZONTAL_REGIONS; x++)
				for(int y = 0; y < VERTICAL_REGIONS; y++)
					regions[x][y] = std::make_shared<Region>();
		}

		std::shared_ptr<Region> RegionManager::getRegion(int x, int y) {
			return regions[x / REGION_SIZE][y / REGION_SIZE];
		}

		std::shared_ptr<Region> *RegionManager::getViewableRegions(int x, int y) {
			std::shared_ptr<Region> *neighbours = new std::shared_ptr<Region>[4];
			int regionX = x / REGION_SIZE;
			int regionY = y / REGION_SIZE;
			neighbours[0] = regions[regionX][regionY];

			int relX = x % REGION_SIZE;
			int relY = y % REGION_SIZE;
			if(relX <= LOWER_BOUND)
				if(relY <= LOWER_BOUND) {
					neighbours[1] = regions[regionX - 1][regionY];
					neighbours[2] = regions[regionX - 1][regionY - 1];
					neighbours[3] = regions[regionX][regionY - 1];
				} else {
					neighbours[1] = regions[regionX - 1][regionY];
					neighbours[2] = regions[regionX - 1][regionY + 1];
					neighbours[3] = regions[regionX][regionY + 1];
				}
			else
				if(relY <= LOWER_BOUND) {
					neighbours[1] = regions[regionX + 1][regionY];
					neighbours[2] = regions[regionX + 1][regionY - 1];
					neighbours[3] = regions[regionX][regionY - 1];
				} else {
					neighbours[1] = regions[regionX + 1][regionY];
					neighbours[2] = regions[regionX + 1][regionY + 1];
					neighbours[3] = regions[regionX][regionY + 1];
				}

			return neighbours;
		}

		std::list<std::shared_ptr<Player>> *RegionManager::getViewablePlayers(std::shared_ptr<Player> player) {
			std::shared_ptr<Region> *regions = getViewableRegions(player->getX(), player->getY());
			std::list<std::shared_ptr<Player>> *players = new std::list<std::shared_ptr<Player>>();

			for(int i = 0; i < 4; i++)
				for(auto it = regions[i]->getPlayers()->begin(); it != regions[i]->getPlayers()->end(); ++it)
					if(player->withinRange(*it))
						players->push_back(*it);
			players->unique();

			return players;
		}
	}
}
