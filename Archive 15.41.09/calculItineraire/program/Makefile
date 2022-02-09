all: map.out

CC=g++
BUILD_DIR=build/
CFLAGS= -g -Wall
LDFLAGS= `pkg-config --libs libcurl` -O2 -L/usr/X11R6/lib -lm -lpthread -lX11

CURRENT_DIR= $(dir $(abspath $(lastword $(MAKEFILE_LIST))))

map.out: build_obj
	${CC} ${CFLAGS} ${BUILD_DIR}*.o -o map.out ${LDFLAGS}

build_obj:
	make -C src CC=${CC} CFLAGS=${CFLAGS} BUILD_DIR=${CURRENT_DIR}${BUILD_DIR}

clean:
	rm -rf ${BUILD_DIR}*.o map.out
