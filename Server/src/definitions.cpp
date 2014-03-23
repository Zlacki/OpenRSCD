/*
 * definitions.cpp
 *
 * Copyright (C) 2014,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#include <iostream>
#include <fstream>
#include <string>
#include <streambuf>
#include <jsoncpp/json/json.h>
#include "definitions.hpp"

namespace RSC {
	std::shared_ptr<ObjectDefinition> *DefinitionHandler::getObjectDefinitions() {
		return objectDefinitions;
	}

	std::shared_ptr<DoorDefinition> *DefinitionHandler::getDoorDefinitions() {
		return doorDefinitions;
	}

	std::shared_ptr<NPCDefinition> *DefinitionHandler::getNPCDefinitions() {
		return npcDefinitions;
	}

	std::shared_ptr<ItemDefinition> *DefinitionHandler::getItemDefinitions() {
		return itemDefinitions;
	}

	std::shared_ptr<SpellDefinition> *DefinitionHandler::getSpellDefinitions() {
		return spellDefinitions;
	}

	std::shared_ptr<PrayerDefinition> *DefinitionHandler::getPrayerDefinitions() {
		return prayerDefinitions;
	}

	void DefinitionHandler::load() {
		loadObjectDefinitions();
		loadDoorDefinitions();
		loadNPCDefinitions();
		loadItemDefinitions();
	}

	void DefinitionHandler::loadObjectDefinitions() {
		std::ifstream file("./data/defs/objects.json");
		std::string data;

		file.seekg(0, std::ios::end);
		data.reserve(file.tellg());
		file.seekg(0, std::ios::beg);
		data.assign((std::istreambuf_iterator<char>(file)), std::istreambuf_iterator<char>());

		Json::Value root;
		Json::Reader reader;
		if(!reader.parse(data, root)) {
			std::cout << "Error: Failed to parse object definitions" << std::endl;
			exit(EXIT_FAILURE);
		}

		root = root["Objects"];
		objectDefinitions = new std::shared_ptr<ObjectDefinition>[root.size()];
		for(unsigned int i = 0; i < root.size(); i++) {
			const Json::Value currentDefinition = root[i];
			std::shared_ptr<ObjectDefinition> definition = std::make_shared<ObjectDefinition>();
			definition->name = currentDefinition["name"].asString();
			definition->command = currentDefinition["command"].asString();
			definition->secondaryCommand = currentDefinition["secondaryCommand"].asString();
			definition->type = (unsigned short) currentDefinition["type"].asInt();
			definition->width = (unsigned short) currentDefinition["width"].asInt();
			definition->height = (unsigned short) currentDefinition["height"].asInt();
			objectDefinitions[i] = definition;
		}
	}

	void DefinitionHandler::loadDoorDefinitions() {
		std::ifstream file("./data/defs/doors.json");
		std::string data;

		file.seekg(0, std::ios::end);
		data.reserve(file.tellg());
		file.seekg(0, std::ios::beg);
		data.assign((std::istreambuf_iterator<char>(file)), std::istreambuf_iterator<char>());

		Json::Value root;
		Json::Reader reader;
		if(!reader.parse(data, root)) {
			std::cout << "Error: Failed to parse door definitions" << std::endl;
			exit(EXIT_FAILURE);
		}

		root = root["Doors"];
		doorDefinitions = new std::shared_ptr<DoorDefinition>[root.size()];
		for(unsigned int i = 0; i < root.size(); i++) {
			const Json::Value currentDefinition = root[i];
			std::shared_ptr<DoorDefinition> definition = std::make_shared<DoorDefinition>();
			definition->name = currentDefinition["name"].asString();
			definition->command = currentDefinition["command"].asString();
			definition->secondaryCommand = currentDefinition["secondaryCommand"].asString();
			definition->type = (unsigned short) currentDefinition["type"].asInt();
			definition->opened = currentDefinition["opened"].asBool();
			doorDefinitions[i] = definition;
		}
	}

	void DefinitionHandler::loadNPCDefinitions() {
		std::ifstream file("./data/defs/npcs.json");
		std::string data;

		file.seekg(0, std::ios::end);
		data.reserve(file.tellg());
		file.seekg(0, std::ios::beg);
		data.assign((std::istreambuf_iterator<char>(file)), std::istreambuf_iterator<char>());

		Json::Value root;
		Json::Reader reader;
		if(!reader.parse(data, root)) {
			std::cout << "Error: Failed to parse NPC definitions" << std::endl;
			exit(EXIT_FAILURE);
		}

		root = root["NPCs"];
		npcDefinitions = new std::shared_ptr<NPCDefinition>[root.size()];
		for(unsigned int i = 0; i < root.size(); i++) {
			const Json::Value currentDefinition = root[i];
			std::shared_ptr<NPCDefinition> definition = std::make_shared<NPCDefinition>();
			definition->name = currentDefinition["name"].asString();
			definition->command = currentDefinition["command"].asString();
			definition->hitpoints = (unsigned short) currentDefinition["hitpoints"].asInt();
			definition->attack = (unsigned short) currentDefinition["attack"].asInt();
			definition->defense = (unsigned short) currentDefinition["defense"].asInt();
			definition->strength = (unsigned short) currentDefinition["strength"].asInt();
			definition->attackable = currentDefinition["attackable"].asBool();
			definition->defaultDrop = (short) currentDefinition["defaultDrop"].asInt();
			npcDefinitions[i] = definition;
		}
	}

	void DefinitionHandler::loadItemDefinitions() {
		std::ifstream file("./data/defs/items.json");
		std::string data;

		file.seekg(0, std::ios::end);
		data.reserve(file.tellg());
		file.seekg(0, std::ios::beg);
		data.assign((std::istreambuf_iterator<char>(file)), std::istreambuf_iterator<char>());

		Json::Value root;
		Json::Reader reader;
		if(!reader.parse(data, root)) {
			std::cout << "Error: Failed to parse item definitions" << std::endl;
			exit(EXIT_FAILURE);
		}

		root = root["Items"];
		itemDefinitions = new std::shared_ptr<ItemDefinition>[root.size()];
		for(unsigned int i = 0; i < root.size(); i++) {
			const Json::Value currentDefinition = root[i];
			std::shared_ptr<ItemDefinition> definition = std::make_shared<ItemDefinition>();
			definition->name = currentDefinition["name"].asString();
			definition->command = currentDefinition["command"].asString();
			definition->female = currentDefinition["female"].asBool();
			definition->members = currentDefinition["members"].asBool();
			definition->stackable = currentDefinition["stackable"].asBool();
			definition->untradable = currentDefinition["untradable"].asBool();
			definition->wearable = currentDefinition["wearable"].asBool();
			definition->sprite = (unsigned short) currentDefinition["sprite"].asInt();
			definition->armorBonus = (unsigned char) currentDefinition["armor_bonus"].asInt();
			definition->basePrice = (unsigned short) currentDefinition["base_price"].asInt();
			definition->magicBonus = (unsigned char) currentDefinition["magic_bonus"].asInt();
			definition->prayerBonus = (unsigned char) currentDefinition["prayer_bonus"].asInt();
			definition->requiredLevel = (unsigned char) currentDefinition["required_level"].asInt();
			definition->requiredSkill = (char) currentDefinition["required_skill"].asInt();
			definition->weaponAim = (unsigned char) currentDefinition["weapon_aim"].asInt();
			definition->weaponPower = (unsigned char) currentDefinition["weapon_power"].asInt();
			definition->wearableID = (unsigned short) currentDefinition["wearable_id"].asInt();
			definition->wearableSlot = (short) currentDefinition["wearable_slot"].asInt();
			itemDefinitions[i] = definition;
		}
	}
}
