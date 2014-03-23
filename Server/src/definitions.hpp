/*
 * definitions.hpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#ifndef _DEFINITIONS_HPP
#define _DEFINITIONS_HPP

#include <string>
#include <memory>

namespace RSC {
	struct ObjectDefinition {
		std::string name;
		std::string command;
		std::string secondaryCommand;
		unsigned short type; /* Direction? */
		unsigned short width;
		unsigned short height;
	};

	struct DoorDefinition {
		std::string name;
		std::string command;
		std::string secondaryCommand;
		unsigned short type; /* Direction? */
		bool opened;
	};

	struct NPCDefinition {
		std::string name;
		std::string command;
		unsigned short hitpoints;
		unsigned short attack;
		unsigned short defense;
		unsigned short strength;
		bool attackable;
		short defaultDrop;
	};

	struct ItemDefinition {
		std::string name;
		std::string command;
		bool female;
		bool members;
		bool stackable;
		bool untradable;
		bool wearable;
		unsigned short sprite;
		unsigned char armorBonus;
		unsigned short basePrice;
		unsigned char magicBonus;
		unsigned char prayerBonus;
		unsigned char requiredLevel;
		char requiredSkill;
		unsigned char weaponAim;
		unsigned char weaponPower;
		unsigned short wearableID;
		short wearableSlot;
	};

	struct SpellDefinition {
		unsigned short requiredLevel;
		unsigned short type;
		unsigned short runeCount;
		unsigned short **requiredRunes;
		unsigned short experience;
		std::string name;
	};

	struct PrayerDefinition {
		unsigned short requiredLevel;
		unsigned short drainRate;
		std::string name;
	};

	class DefinitionHandler {
		private:
			std::shared_ptr<ObjectDefinition> *objectDefinitions;
			std::shared_ptr<DoorDefinition> *doorDefinitions;
			std::shared_ptr<NPCDefinition> *npcDefinitions;
			std::shared_ptr<ItemDefinition> *itemDefinitions;
			std::shared_ptr<SpellDefinition> *spellDefinitions;
			std::shared_ptr<PrayerDefinition> *prayerDefinitions;

		public:
			std::shared_ptr<ObjectDefinition> *getObjectDefinitions();
			std::shared_ptr<DoorDefinition> *getDoorDefinitions();
			std::shared_ptr<NPCDefinition> *getNPCDefinitions();
			std::shared_ptr<ItemDefinition> *getItemDefinitions();
			std::shared_ptr<SpellDefinition> *getSpellDefinitions();
			std::shared_ptr<PrayerDefinition> *getPrayerDefinitions();
			void load();
			void loadObjectDefinitions();
			void loadDoorDefinitions();
			void loadNPCDefinitions();
			void loadItemDefinitions();
			void loadPrayerDefinitions();
			void loadSpellDefinitions();
	};
}
#endif
