/*
 * collections.hpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#ifndef _COLLECTIONS_HPP
#define _COLLECTIONS_HPP

#include <map>
#include <memory>
#include "../definitions.hpp"
#include "player.hpp"

#define MAX_ENTITIES 2000

namespace RSC {
	using namespace IO;
	using namespace Model;
	class Collections {
		private:
			std::shared_ptr<DefinitionHandler> definitionHandler;
			std::map<unsigned short, std::shared_ptr<Player>> players;
			// TODO: Entity indicies
			/* todo: regions*/
		public:
			std::shared_ptr<DefinitionHandler> getDefinitionHandler();
			void setDefinitionHandler(std::shared_ptr<DefinitionHandler>);
			void addPlayer(std::shared_ptr<Player>);
			std::shared_ptr<Player> getPlayer(unsigned short);
			void removePlayer(unsigned short);
			void sendRegionalPlayerPositionUpdate(std::shared_ptr<Player> player);
			void sendRegionalPlayerAppearanceUpdate(std::shared_ptr<Player> player);
	};
}

#endif
