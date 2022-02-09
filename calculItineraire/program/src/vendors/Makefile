# CC = compiler
# CFLAGS = compilation flags
# BUILD_DIR = (mandatory) (must be absolute) directory for object files

all: ${BUILD_DIR}pugi.o

${BUILD_DIR}pugi.o: pugi/pugixml.cpp
	${CC} ${CFLAGS} -c pugi/pugixml.cpp -o ${BUILD_DIR}pugi.o
