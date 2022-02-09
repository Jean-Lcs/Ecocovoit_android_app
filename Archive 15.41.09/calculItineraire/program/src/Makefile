# CC = (mandatory) compiler
# CFLAGS = compilation flags
# BUILD_DIR = (mandatory) (must be absolute) directory for object files

all: ${BUILD_DIR}main.o ${BUILD_DIR}display-map ${BUILD_DIR}osm-request ${BUILD_DIR}osm-xml-parsing ${BUILD_DIR}path-find ${BUILD_DIR}vendors

${BUILD_DIR}main.o:
	${CC} -c main.cpp -o ${BUILD_DIR}main.o

${BUILD_DIR}display-map:
	make -C display-map CC=${CC} CFLAGS=${CFLAGS} BUILD_DIR=${BUILD_DIR}

${BUILD_DIR}osm-request:
	make -C osm-request CC=${CC} CFLAGS=${CFLAGS} BUILD_DIR=${BUILD_DIR}

${BUILD_DIR}osm-xml-parsing:
	make -C osm-xml-parsing CC=${CC} CFLAGS=${CFLAGS} BUILD_DIR=${BUILD_DIR}

${BUILD_DIR}path-find:
	make -C path-find CC=${CC} CFLAGS=${CFLAGS} BUILD_DIR=${BUILD_DIR}

${BUILD_DIR}vendors:
	make -C vendors CC=${CC} CFLAGS=${CFLAGS} BUILD_DIR=${BUILD_DIR}
