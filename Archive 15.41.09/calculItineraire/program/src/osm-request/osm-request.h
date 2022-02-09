#ifndef _OSM_REQUEST_
#define _OSM_REQUEST_

#include "../utils/debug-stuff.h"

// For HTTPS 
#include <curl/curl.h>
#include <string>
#include <string.h>
#include <iostream>

namespace osm_request {

    static const std::string DEFAULT_OSM_URL = "https://z.overpass-api.de/api/interpreter";
    static const std::string DEFAULT_OSM_REQUEST = "node(48.8534,2.3487,48.8539,2.3492)[highway=primary]; <; >; out;";

/**Lance une requete Overpass et retourne le xml de la reponse.*/
std::string query_xml_map(std::string osm_request, std::string osm_url);

};

#endif