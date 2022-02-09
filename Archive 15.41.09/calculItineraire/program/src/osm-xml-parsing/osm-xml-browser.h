#ifndef _XML_PARSING_
#define _XML_PARSING_

#include "../utils/debug-stuff.h"
#include "../utils/value-types.h"

#include <iostream>
#include <vector>
#include <string>
#include <string.h>

// XML Parsing library
#include "../vendors/pugi/pugiconfig.hpp"
#include "../vendors/pugi/pugixml.hpp"

// Notre parallelisation personnalisee
#include "osm-xml-browser-thread.h"

namespace osm_parsing {

// START NAME_SPACE

/* Definition de la classe qui permet de parcourir un fichier xml
contenant une map osm.
*/

namespace types {
    static const unsigned int OSM_STRING_SIZE = 256;

    /**Le xml tag d'en-tete du osm xml*/
    static const std::string OSM_OBJECT_NAME_META = "meta";
    /**Le xml tag contenant tous les objets osm.*/
    static const std::string OSM_OBJECT_NAME_OSM = "osm";

    /**Le xml tag pour osm node.*/
    static const std::string OSM_OBJECT_NAME_NODE = "node";
    /**Le xml tag pour osm way.*/
    static const std::string OSM_OBJECT_NAME_WAY = "way";
    /**Le xml tag pour osm relation.*/
    static const std::string OSM_OBJECT_NAME_RELATION = "relation";
    /**Le xml tag pour osm relation's membre.*/
    static const std::string OSM_OBJECT_NAME_MEMBER = "member";
    /**Le xml tag pour osm object's tags.*/
    static const std::string OSM_OBJECT_NAME_TAG = "tag";
    /**Le xml tag pour osm way's references to osm node.*/
    static const std::string OSM_OBJECT_NAME_ND = "nd";

    /**Le xml attribut pour l'id des osm objets.*/
    static const std::string OSM_OBJECT_ATTRIB_NAME_ID = "id";
    /**Le xml attribut pour la reference d'un osm node dans un osm way. (xml tag : nd)*/
    static const std::string OSM_OBJECT_ATTRIB_NAME_REF = "ref";
    /**Le xml attribut pour le type de relation d'un 
     * osm node ou way dans une osm relation. (xml tag : member)*/
    static const std::string OSM_OBJECT_ATTRIB_NAME_TYPE = "type";
    /**Le xml attribut pour le role d'une relation d'un 
     * osm node ou way dans une osm relation. (xml tag : member)*/
    static const std::string OSM_OBJECT_ATTRIB_NAME_ROLE = "role";
    /**Le xml attribut pour la latitude d'un osm node. (xml tag : node)*/
    static const std::string OSM_OBJECT_ATTRIB_NAME_LAT = "lat";
    /**Le xml attribut pour la longitude d'un osm node. (xml tag : node)*/
    static const std::string OSM_OBJECT_ATTRIB_NAME_LON = "lon";

    /**Le xml attribut pour la cle d'un osm tag. (xml tag : tag)*/
    static const std::string OSM_TAG_ATTRIB_NAME_KEY = "k";
    /**Le xml attribut pour la valeur d'un osm tag. (xml tag : tag)*/
    static const std::string OSM_TAG_ATTRIB_NAME_VALUE = "v";

    /**cle d'un osm tag donnant le nom de son osm objet. (xml tag : tag. attribut : v)*/
    static const std::string OSM_TAG_ATTRIB_KEY_NAME = "name";

    /**cle d'un osm tag specifiant le type de route sur laquelle un vehicule
     * peut circuler. (xml tag : tag. attribut : v)*/
    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY = "highway";
        static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_MOTORWAY = "motorway";
	    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_TRUNK = "trunk";
	    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_PRIMARY = "primary";
	    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_SECONDARY = "secondary";
	    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_TERTIARY = "tertiary";
	    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_UNCLASSIFIED = "unclassified";
	    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_RESIDENTIAL = "residential";
	    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_MOTORWAY_LINK = "motorway_link";
	    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_TRUNK_LINK = "trunk_link";
	    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_PRIMARY_LINK = "primary_link";
	    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_SECONDARY_LINK = "secondary_link";
	    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_TERTIARY_LINK = "tertiary_link";
	    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_LIVING_STREET = "living_street";
	    static const std::string OSM_TAG_ATTRIB_KEY_HIGHWAY_VALUE_ROAD = "road";

    /**LES TROIS TYPE D'OBJETS OSM*/
    typedef enum {
        NODE = 1,
        WAY,
        RELATION
    } OsmXmlObjectType;

    /**TOUS LES TYPE DE TAGS QUE NOUS PRENONS EN COMPTE*/
    typedef enum {
        UNKNOWN = 0,
        NAME,
        HIGHWAY
    } TagKey;
    /**Definition of osm tag*/
    typedef struct {
        TagKey key;
        char value[OSM_STRING_SIZE];
    } Tag;

    /**TOUS LES TYPES DE RELATIONS QUE NOUS PRENONS EN COMPTE*/
    typedef enum {
        EMPTY = 0,
        FORWARD
    } RelMemberRole;
    /** Definition of osm relation member*/
    typedef struct {
        OsmXmlObjectType type;
        value_types::Id ref;
        RelMemberRole role;
    } RelMember;

    /** Definition of osm nodes*/
    typedef struct {
        value_types::Id id;
        value_types::Distance lat;
        value_types::Distance lon;
        std::vector<Tag> tags;
    } Node;

    /** Definition of osm ways*/
    typedef struct {
        value_types::Id id;
        std::vector<value_types::Id> nds;
        std::vector<Tag> tags;
    } Way;

    /** Definition of osm relations*/
    typedef struct {
        std::vector<RelMember> members;
        std::vector<Tag> tags;
    } Relation;

};

/**Type de fonction appellee pour un osm node*/
typedef void (*BrowseXmlOnNodeCallback)(
    types::Node*,
    void * userData,
    Mutex * mutex);
/**Type de fonction appellee pour un osm way*/
typedef void (*BrowseXmlOnWayCallback)(
    types::Way*,
    void * userData,
    Mutex * mutex);
/**Type de fonction appellee pour une osm relation*/
typedef void (*BrowseXmlOnRelationCallback)(
    types::Relation*,
    void * userData,
    Mutex * mutex);

typedef struct {
    BrowseXmlOnNodeCallback nodeCallback;
	BrowseXmlOnWayCallback wayCallback;
	BrowseXmlOnRelationCallback relationCallback;
} OsmCallbacksHolder;

bool containsTag(std::vector<types::Tag> tags, types::TagKey key);
bool containsTag(std::vector<types::Tag> tags, types::TagKey key, std::string value);

class OsmXmlBrowser {
public:
    OsmXmlBrowser(const char * xmlCode);

    /**Affiche le contenu du xml.*/
    void show_children();

    void browseParallel(
        BrowseXmlOnNodeCallback nodeCallback,
        BrowseXmlOnWayCallback wayCallback,
        BrowseXmlOnRelationCallback relationCallback,
        void * userData);

private:
    pugi::xml_document doc;
};

// END NAME_SPACE
};

#endif