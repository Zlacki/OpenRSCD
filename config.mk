PREFIX=/usr/local

INCS = -I. -I/usr/include -I/usr/local/include
LIBS = -L/usr/lib -L/usr/local/lib -ljson-c -lev -lpthread

CFLAGS = -std=c99 -Wall -pedantic -Os -D_BSD_SOURCE -D_POSIX_SOURCE ${INCS}
LDFLAGS = -g ${LIBS}

CC = gcc
