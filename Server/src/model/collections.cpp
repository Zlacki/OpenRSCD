/*
 * collections.cpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#include <iostream>
#include "collections.hpp"
#include "region.hpp"
#include "../util.hpp"

namespace RSC {
	extern std::shared_ptr<RegionManager> regionManager;

	std::shared_ptr<DefinitionHandler> Collections::getDefinitionHandler() {
		return definitionHandler;
	}

	void Collections::setDefinitionHandler(std::shared_ptr<DefinitionHandler> definitionHandler) {
		this->definitionHandler = definitionHandler;
	}

	std::shared_ptr<Player> Collections::getPlayer(unsigned short index) {
		return players[index];
	}

	void Collections::addPlayer(std::shared_ptr<Player> player) {
		players[player->getIndex()] = player;
	}

	void Collections::removePlayer(unsigned short index) {
		std::shared_ptr<Player> player = getPlayer(index);
		std::cout << "Unregestering player: ‘" << player->getUsername() << "’" << std::endl;
	//	remove_client(player->getWatcher(), player->getSocket());
		sendRegionalPlayerPositionUpdate(player);
		sendRegionalPlayerAppearanceUpdate(player);
		regionManager->getRegion(player->getX(), player->getY())->removePlayer(player);
		players.erase(index);
	}

	void Collections::sendRegionalPlayerPositionUpdate(std::shared_ptr<Player> player) {
		auto list = regionManager->getViewablePlayers(player);
		for(auto it = list->begin(); it != list->end(); ++it)
			(*it)->getNetSender()->sendPlayerPositions();
	}
	void Collections::sendRegionalPlayerAppearanceUpdate(std::shared_ptr<Player> player) {
		auto list = regionManager->getViewablePlayers(player);
		for(auto it = list->begin(); it != list->end(); ++it)
			(*it)->getNetSender()->sendPlayerAppearances();
	}
}
