/*
 * util.cpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#include <algorithm>
#include <iostream>
#include <string>
#include <boost/algorithm/string.hpp>
#include "util.hpp"

void warning(std::string msg) {
	std::cout << "WARNING: " << msg << std::endl;
}

long usernameToHash(std::string username) {
	std::string filtered;

	for(char c : username)
		if((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))
			filtered += c;
		else if(c >= 'A' && c <= 'Z')
			filtered += (char) ((c + 97) - 65);
		else
			filtered += ' ';
	boost::trim(filtered);

	long hash = 0L;
	for(char c : filtered) {
		hash *= 37L;
		if(c >= 'a' && c <= 'z')
			hash += (1 + c) - 97;
		else if(c >= '0' && c <= '9')
			hash += (27 + c) - 48;
	}

	return hash;
}

std::string hashToUsername(long hash) {
	if(hash < 0L)
		return "invalid_name";

	std::string username;
	while(hash != 0L) {
		int i = (int) (hash % 37L);
		hash /= 37L;
		if(i == 0)
			username += ' ';
		else if(i < 27)
			if(hash % 37L == 0L)
				username += (char) ((i + 65) - 1);
			else
				username += (char) ((i + 97) - 1);
		else
			username += (char) ((i + 48) - 27);
	}

	std::reverse(username.begin(), username.end());

	return username;
}
