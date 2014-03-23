/*
 * util.hpp
 *
 * Copyright (C) 2011-2012,  Alex Petrovich <alex@libslack.so>
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

#ifndef _UTIL_HPP
#define _UTIL_HPP

#include <string>

#define VERSION 208

void warning(std::string);
long usernameToHash(std::string);
std::string hashToUsername(long);

#endif
