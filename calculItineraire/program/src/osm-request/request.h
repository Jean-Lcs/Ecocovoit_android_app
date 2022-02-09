#ifndef _REQUEST_
#define _REQUEST_

#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <cmath>

typedef enum HighwayTag{
  MOTORWAY,
  PRIMARY,
  SECONDARY,
  TERTIARY,
  TRUNK,
  ROAD,
  RESIDENTIAL,
  UNCLASSIFIED
} HighwayTag;

char* requeteRoute(long latMin, long lonMin, long latMax, long lonMax, long rayon, HighwayTag highway_tag);
double fromLongToDouble(long coord) ;
HighwayTag fromStringToTag(char * tag) ;

#endif
