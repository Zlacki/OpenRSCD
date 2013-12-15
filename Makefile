include config.mk

COREOBJ = bin/main.o bin/util.o bin/list.o bin/hashmap.o

RSCOBJ = bin/rsc/player.o bin/rsc/region.o bin/rsc/inventory.o bin/rsc/item.o bin/rsc/packet.o bin/rsc/packetcreator.o bin/rsc/packethandler.o bin/rsc/rsc.o bin/rsc/object.o bin/rsc/util.o

all: mkbin options core cirnopk

mkbin:
	@mkdir -p bin/rsc

options:
	@echo cirnopk build options:
	@echo "CFLAGS  = ${CFLAGS}"
	@echo "LDFLAGS = ${LDFLAGS}"
	@echo "CC      = ${CC}"

bin/%.o: src/%.c
	@echo CC $<
	@${CC} -o $@ -c ${CFLAGS} $<

core: ${COREOBJ}
	@echo $@ finished compiling.

cirnopk: ${RSCOBJ}
	@echo CC -o $@
	@${CC} -o $@ ${COREOBJ} ${RSCOBJ} ${LDFLAGS}
	@echo $@ finished compiling.

clean:
	@echo cleaning
	@rm -f bin/*.o bin/rsc/*.o cirnopk
