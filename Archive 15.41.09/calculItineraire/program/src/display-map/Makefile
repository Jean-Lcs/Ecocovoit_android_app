# CC = compiler
# CFLAGS = compilation flags
# BUILD_DIR = (mandatory) (must be absolute) directory for object files

all: ${BUILD_DIR}display-map.o

${BUILD_DIR}display-map.o: display-map.cpp display-map.h
	${CC} ${CFLAGS} -c display-map.cpp -o ${BUILD_DIR}display-map.o

