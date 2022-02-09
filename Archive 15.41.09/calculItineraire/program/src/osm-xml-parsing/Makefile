# CC = compiler
# CFLAGS = compilation flags
# BUILD_DIR = (mandatory) (must be absolute) directory for object files

all: ${BUILD_DIR}osm-xml-browser.o ${BUILD_DIR}osm-xml-browser-thread.o ${BUILD_DIR}osm-matrix.o ${BUILD_DIR}osm-matrix-builder.o

${BUILD_DIR}osm-xml-browser.o: osm-xml-browser.cpp osm-xml-browser.h
	${CC} ${CFLAGS} -c osm-xml-browser.cpp -o ${BUILD_DIR}osm-xml-browser.o

${BUILD_DIR}osm-xml-browser-thread.o: osm-xml-browser-thread.cpp osm-xml-browser-thread.h
	${CC} ${CFLAGS} -c osm-xml-browser-thread.cpp -o ${BUILD_DIR}osm-xml-browser-thread.o

${BUILD_DIR}osm-matrix.o: osm-matrix.cpp osm-matrix.h
	${CC} ${CFLAGS} -c osm-matrix.cpp -o ${BUILD_DIR}osm-matrix.o

${BUILD_DIR}osm-matrix-builder.o: osm-matrix-builder.cpp osm-matrix-builder.h
	${CC} ${CFLAGS} -c osm-matrix-builder.cpp -o ${BUILD_DIR}osm-matrix-builder.o
