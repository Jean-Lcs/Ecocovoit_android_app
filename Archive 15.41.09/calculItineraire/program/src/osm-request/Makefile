# CC = compiler
# CFLAGS = compilation flags
# BUILD_DIR = (mandatory) (must be absolute) directory for object files

all: ${BUILD_DIR}request.o ${BUILD_DIR}osm-request.o

${BUILD_DIR}osm-request.o: osm-request.cpp osm-request.h
	${CC} ${CFLAGS} -c osm-request.cpp -o ${BUILD_DIR}osm-request.o

${BUILD_DIR}request.o: request.cpp request.h
	${CC} ${CFLAGS} -c request.cpp -o ${BUILD_DIR}request.o
