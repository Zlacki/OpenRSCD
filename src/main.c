/*
 * main.c
 *
 * Copyright (C) 2011,2012,2013, Zach Knight <zach@libslack.so>
 *
 * Permission to use, copy, modify, and/or distribute this software
 * for any purpose with or without fee is hereby granted, provided
 * that the above copyright notice and this permission notice appear
 * in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL
 * WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL
 * THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 * FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF
 * CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT
 * OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <time.h>
#include <unistd.h>
#include <netinet/tcp.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <ev.h>
#include "module.h"
#include "util.h"

void client_destroy(struct ev_loop *loop, struct ev_io *watcher) {
	clients[watcher->fd] = -1;
	ev_io_stop(loop, watcher);
	free(watcher);
}

void process_read(struct ev_loop *loop, struct ev_io *watcher, int revents) {
	if(EV_ERROR & revents) {
		warning("Recieved abnormal event; dropping.");
		clients[watcher->fd] = -1;
		ev_io_stop(loop, watcher);
		free(watcher);
		return;
	}

	char buffer[2];
	int r = recv(watcher->fd, buffer, 2, 0);
	if(r <= 0) {
		warning("Player socket died abnormally.");
		client_destroy(loop, watcher);
		return;
	}

	for(int i = 0; i < MAX_CLIENTS; i++)
		if(clients[i] == watcher->fd)
			on_read(i, ((short) ((buffer[0] & 0xFF) << 8) | (short) (buffer[1] & 0xFF)));

	return;
}

void process_accept(struct ev_loop *loop, struct ev_io *watcher, int revents) {
	if(EV_ERROR & revents) {
		warning("Recieved abnormal event; dropping.");
		return;
	}

	int socket;
	struct sockaddr_in client_addr;
	socklen_t addr_len = sizeof(client_addr);
	if((socket = accept(watcher->fd, (struct sockaddr*)&client_addr, &addr_len)) < 0) {
		warning("Recieved abnormal client socket; dropping.");
		return;
	}

	socket_set_no_block(socket);

	struct ev_io *read_watcher = (struct ev_io*)safe_alloc(sizeof(struct ev_io));
	for(int i = 0; i < MAX_CLIENTS; i++)
		if(clients[i] == -1) {
			on_accept(loop, read_watcher, socket, i);
			clients[i] = socket;
			ev_io_init(read_watcher, process_read, socket, EV_READ);
			ev_io_start(loop, read_watcher);
			break;
		}

	return;
}

int main(void) {
	puts("Core initializing...");
	signal(SIGPIPE, SIG_IGN);

	printf("Initializing client array...");
	memset((char*) &clients, -1, MAX_CLIENTS + 1);
	puts("done.");

	printf("Seeding random number generator...");
	srand(time(NULL));
	puts("done");

	printf("I/O initializing...");
	int server_socket = socket(AF_INET, SOCK_STREAM, 0);
	int r = 1;
	setsockopt(server_socket, SOL_SOCKET, SO_REUSEADDR, &r, sizeof(r));
	setsockopt(server_socket, IPPROTO_TCP, TCP_NODELAY, &r, sizeof(r));
	socket_set_no_block(server_socket);
	if(server_socket < 0)
		error("Socket error; non-recoverable.");
	struct sockaddr_in server_addr;
	memset((char*) &server_addr, 0, sizeof(server_addr));
	server_addr.sin_family = AF_INET;
	server_addr.sin_addr.s_addr = INADDR_ANY;
	server_addr.sin_port = htons(43594);
	if(bind(server_socket, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0)
		error("Bind error; non-recoverable.");
	if(listen(server_socket, 2) < 0)
		error("Listen error; non-recoverable.");
	struct ev_io *watcher = safe_alloc(sizeof(struct ev_io));;
	ev_io_init(watcher, process_accept, server_socket, EV_READ);
	struct ev_loop *loop = ev_default_loop(0);
	ev_io_start(loop, watcher);
	printf("done\n");


	printf("Loading module: ");
	load();

	puts("Core initialized.");

	for(;;) ev_loop(loop, 0);

	return 0;
}
