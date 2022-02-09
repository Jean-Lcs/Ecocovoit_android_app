#include "request.h"

char* requeteRoute(long latMin, long lonMin, long latMax, long lonMax, long rayon, HighwayTag highway_tag) {
  double latMin_double = fromLongToDouble(latMin) ;
  double lonMin_double = fromLongToDouble(lonMin) ;
  double latMax_double = fromLongToDouble(latMax) ;
  double lonMax_double = fromLongToDouble(lonMax) ;
  double rayon_double = fromLongToDouble(rayon) ;
  char * requete = new char[256] ;

  switch(highway_tag) {
    case MOTORWAY :
    sprintf(requete, "((node(%f, %f, %f, %f); ) -> .b ;) ; \n way(around[.b]:%f)[highway=motorway] ;" , latMin_double, lonMin_double, latMax_double, lonMax_double, rayon_double) ;
    break ;
    case PRIMARY :
    sprintf(requete, "((node(%f, %f, %f, %f); ) -> .b ;) ; \n way(around[.b]:%f)[highway][primary] ;" , latMin_double, lonMin_double, latMax_double, lonMax_double, rayon_double) ;
    break ;
    case SECONDARY :
    sprintf(requete, "((node(%f, %f, %f, %f); ) -> .b ;) ; \n way(around[.b]:%f)[highway][secondary] ;" , latMin_double, lonMin_double, latMax_double, lonMax_double, rayon_double) ;
    break ;
    case TERTIARY :
    sprintf(requete, "((node(%f, %f, %f, %f); <; >; ) -> .b ;) ; \n way(around[.b]:%f)[highway=tertiary] ;" , latMin_double, lonMin_double, latMax_double, lonMax_double, rayon_double) ;
    break ;
    case TRUNK :
    sprintf(requete, "((node(%f, %f, %f, %f); ) -> .b ;) ; \n way(around[.b]:%f)[highway][trunk] ;" , latMin_double, lonMin_double, latMax_double, lonMax_double, rayon_double) ;
    break ;
    case ROAD :
    sprintf(requete, "((node(%f, %f, %f, %f); ) -> .b ;) ; \n way(around[.b]:%f)[highway][road] ;" , latMin_double, lonMin_double, latMax_double, lonMax_double, rayon_double) ;
    break ;
    case RESIDENTIAL :
    sprintf(requete, "((node(%f, %f, %f, %f); ) -> .b ;) ; \n way(around[.b]:%f)[highway][residential] ;" , latMin_double, lonMin_double, latMax_double, lonMax_double, rayon_double) ;
    break ;
    case UNCLASSIFIED :
    sprintf(requete, "((node(%f, %f, %f, %f); ) -> .b ;) ; \n way(around[.b]:%f)[highway][unclassified] ;" , latMin_double, lonMin_double, latMax_double, lonMax_double, rayon_double) ;
    break ;
  }
  return requete ;
}

double fromLongToDouble(long coord) {
  double coordDouble ;
  coordDouble = coord * pow(10,-4) ;
  return coordDouble ;
}

HighwayTag fromStringToTag(char * tag) {
  HighwayTag highwaytag ;
  if (strcmp(tag, "MOTORWAY") == 0) {
    highwaytag = MOTORWAY ;
  }
  if (strcmp(tag, "PRIMARY") == 0) {
    highwaytag = PRIMARY ;
  }
  if (strcmp(tag, "SECONDARY") == 0) {
    highwaytag = SECONDARY ;
  }
  if (strcmp(tag, "TERTIARY") == 0) {
    highwaytag = TERTIARY ;
  }
  if (strcmp(tag, "TRUNK") == 0) {
    highwaytag = TRUNK ;
  }
  if (strcmp(tag, "ROAD") == 0) {
    highwaytag = ROAD ;
  }
  if (strcmp(tag, "RESIDENTIAL") == 0) {
    highwaytag = RESIDENTIAL ;
  }
  if (strcmp(tag, "UNCLASSIFIED") == 0) {
    highwaytag = UNCLASSIFIED ;
  }

  return highwaytag ;
}

// int main(int argc, char *argv[]) {
//   long latMin = atol(argv[1]) ;
//   long lonMin = atol(argv[2]) ;
//   long latMax = atol(argv[3]) ;
//   long lonMax = atol(argv[4]) ;
//   long rayon = atol(argv[5]) ;
//   HighwayTag highway_tag = fromStringToTag(argv[6]) ;

//   printf("%s", requeteRoute(latMin, lonMin, latMax, lonMax, rayon, highway_tag)) ;
//   return 0 ;
// }
