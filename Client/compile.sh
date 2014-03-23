#!/bin/sh
rm -r build/*
javac -sourcepath src/ -d build src/*
