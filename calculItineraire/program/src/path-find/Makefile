# CC = compiler
# CFLAGS = compilation flags
# BUILD-DIR = (mandatory) (must be absolute) directory for object files

all: ${BUILD_DIR}path-finder.o ${BUILD_DIR}path-finder-types.o

${BUILD_DIR}path-finder.o: path-finder.cpp path-finder.h
	${CC} ${CFLAGS} -c path-finder.cpp -o ${BUILD_DIR}path-finder.o

${BUILD_DIR}path-finder-types.o: path-finder-types.cpp path-finder-types.h
	${CC} ${CFLAGS} -c path-finder-types.cpp -o ${BUILD_DIR}path-finder-types.o