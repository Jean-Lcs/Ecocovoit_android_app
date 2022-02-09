#include "osm-xml-browser.h"

namespace osm_parsing {

bool containsTag(
		std::vector<types::Tag> tags,
		types::TagKey key) {
	for(auto tag : tags) {
		if(tag.key == key) {
			return true;
		}
	}
	return false;
}
bool containsTag(
		std::vector<types::Tag> tags,
		types::TagKey key, std::string value) {
	for(auto tag : tags) {
		if(tag.key == key && value.compare(tag.value)) {
			return true;
		}
	}
	return false;
}

OsmXmlBrowser::OsmXmlBrowser(
		const char * xmlCode) {
	pugi::xml_parse_result result = doc.load_string(xmlCode);
	if(result == false) {
		std::cerr << "PUGI Could not load xml" << std::endl;
	}
}

/**Parcour les sous nodes xml directes du 
 * node xml doc et retourne tous ses
 * sous nodes de nom "osm".
 * à utilisé pour trouver le tag xml osm
 * qui contient tous les tags.
*/
static pugi::xml_node get_osm_xml_tag(
		pugi::xml_node & doc) {

	for(pugi::xml_node_iterator it = doc.begin();
			it!=doc.end(); it++) {
		pugi::xml_node node = *it;

		std::string name = node.name();
		if(name.compare(types::OSM_OBJECT_NAME_OSM) == 0) {
			return node;
		}
	}

	return {};
}

/**Affiche à la sortie standard tous les 
 * nodes xml contenu dans le node xml doc.
*/
static void show_children_(
		pugi::xml_node & doc,
		int * indentcount) {
	
	for(pugi::xml_node_iterator it = doc.begin();
			it!=doc.end(); it++) {
		pugi::xml_node node = *it;

		for(int i=0; i<*indentcount; i++)
			std::cout << "   ";

		std::cout << node.name();
		auto attribs = node.attributes();
		
		for(auto attr_it = attribs.begin();
				attr_it!=attribs.end(); attr_it++ ) {
			auto attr = (*attr_it);
			std::cout << " || " << attr.name() << "=" << attr.value();
		}
		std::cout << std::endl;

		(*indentcount)++;
		show_children_(node, indentcount);
		(*indentcount)--;
	}
}

void OsmXmlBrowser::show_children() {
	auto osm_xml_tag = get_osm_xml_tag(this->doc);
	int indentCount = 0;
	show_children_(osm_xml_tag, &indentCount);
}

/**Parcours le sous node xml directes objectXmlNode.
 * Tous les <tag> rencontrés sont ajouter à la
 * liste tags sous forme de Tag.
 * Utilisé pour avoir tous les tags d'un objet osm.
*/
static void fillOsmObjectTags(
		pugi::xml_node objectXmlNode,
		std::vector<types::Tag> & tags) {
	
	for(pugi::xml_node_iterator it = objectXmlNode.begin();
			it!=objectXmlNode.end(); it++) {
		pugi::xml_node node = *it;

		std::string name = node.name();
		if(name.compare(types::OSM_OBJECT_NAME_TAG) == 0) {
			// On a trouvé un tag
			types::Tag tag = {};

			auto attribs = node.attributes();
			for(auto attr_it = attribs.begin();
					attr_it!=attribs.end(); attr_it++ ) {
				// Les attributs du tag
				auto attr = (*attr_it);
				std::string attrName = attr.name();
				std::string attrValue = attr.value();

				if(attrName.compare(types::OSM_TAG_ATTRIB_NAME_KEY) == 0) {
					if(attrValue.compare(types::OSM_TAG_ATTRIB_KEY_NAME) == 0)
					// LE TAG NAME
						tag.key = types::TagKey::NAME;
					else if(attrValue.compare(types::OSM_TAG_ATTRIB_KEY_HIGHWAY) == 0)
					// LE TAG HIGHWAY
						tag.key = types::TagKey::HIGHWAY;
					else
						std::cerr << "Unknown tag key : " << attrValue << std::endl;
				}
				else if(attrName.compare(types::OSM_TAG_ATTRIB_NAME_VALUE) == 0) {
					if(tag.key != types::TagKey::UNKNOWN)
						memcpy(tag.value, attrValue.data(), attrValue.size());
				}

				else {
					std::cerr << "Unknown attribut for tag : " << attrName << std::endl;
				}
			}

			tags.push_back(tag);
		}
	}
}

/**Parcour les sous node xml directes du node xmlWay.
 * pour chaque node xml de nom nd, ajoute la valeur de 
 * son attribut ref à la liste nds.
 * Utilisé pour avoir la liste ordonnées des noeuds 
 * contenu dans 
*/
static void fillOsmWayNdRef(pugi::xml_node xmlWay, std::vector<value_types::Id> & nds) {
	for(pugi::xml_node_iterator it = xmlWay.begin(); it!=xmlWay.end(); it++) {
		pugi::xml_node node = *it;

		std::string name = node.name();
		if(name.compare(types::OSM_OBJECT_NAME_ND) == 0) {
			// On a trouvé un nd.
			value_types::Id ndRef = 0;

			auto attribs = node.attributes();
			for(auto attr_it = attribs.begin(); attr_it!=attribs.end(); attr_it++ ) {
				// Les attributs du nd.
				auto attr = (*attr_it);
				std::string attrName = attr.name();
				std::string attrValue = attr.value();

				DEBUG(std::cout << "\t" << attrName << " = " << attrValue << std::endl;)

				if(attrName.compare(types::OSM_OBJECT_ATTRIB_NAME_REF) == 0) {
					ndRef = std::stol(attrValue);
				}
				else {
					std::cerr << "Unknown attribut for nd : " << attrName << std::endl;
				}
			}

			nds.push_back(ndRef);
		}
	}
}

/** le node xml <node> est un osm node.
 * Cette fonction recupere les attributs du osm node,
 * Cree le osm node associe,
 * puis lis les sous node xml de <node> qui sont :
 * - les tags de l'osm node,
 * recupere ces tags et les ajoute a l'osm node
 * puis appel nodeCallback pour cette osm node,
 * avec <userData> en dernier parametre, 
*/
static void browserForNode(
	pugi::xml_node & node,
	BrowseXmlOnNodeCallback nodeCallback,
	void * userData,
	Mutex * mutex
) {
	types::Node osmNode;
	auto attribs = node.attributes();
	for(auto attr_it = attribs.begin(); attr_it!=attribs.end(); attr_it++ ) {
		// Les attributs du node
		auto attr = (*attr_it);
		std::string attrName = attr.name();
		std::string attrValue = attr.value();

		if(attrName.compare(types::OSM_OBJECT_ATTRIB_NAME_ID) == 0) {
			osmNode.id = std::stol(attrValue);
		}
		else if(attrName.compare(types::OSM_OBJECT_ATTRIB_NAME_LAT) == 0) {
			// osmNode.lat = std::stold(attrValue);
			size_t d;
			long l1 = std::stoi(attrValue, &d);
			long l2 = std::stoi(attrValue.data()+d+1);
			osmNode.lat = l1*10000000 + l2;
		}
		else if(attrName.compare(types::OSM_OBJECT_ATTRIB_NAME_LON) == 0) {
			// osmNode.lon = std::stold(attrValue);
			size_t d;
			long l1 = std::stoi(attrValue, &d);
			long l2 = std::stoi(attrValue.data()+d+1);
			osmNode.lon = l1*10000000 + l2;
		}
		else {
			std::cerr << "Unknown attribut for node : " << attrName << std::endl;
		}
	}

	// Les tags du node
	fillOsmObjectTags(node, osmNode.tags);

	nodeCallback(&osmNode, userData, mutex);
}

/** le node xml <node> est un osm way.
 * Cette fonction recupere les attributs du osm way,
 * Cree le osm node associe,
 * puis lis les sous node xml de <node> qui sont :
 * - les references de osm node
 * - les tags de l'osm way,
 * recupere ces references et tags et les ajoute a l'osm way.
 * puis appel nodeCallback pour cette osm node,
 * avec <userData> en dernier parametre, 
*/
static void browserForWay(
	pugi::xml_node & node,
	BrowseXmlOnWayCallback wayCallback,
	void * userData,
	Mutex * mutex
) {
	types::Way osmWay;
	auto attribs = node.attributes();
	for(auto attr_it = attribs.begin(); attr_it!=attribs.end(); attr_it++ ) {
		// Les attributs du way
		auto attr = (*attr_it);
		std::string attrName = attr.name();
		std::string attrValue = attr.value();

		if(attrName.compare(types::OSM_OBJECT_ATTRIB_NAME_ID) == 0) {
			osmWay.id = std::stol(attrValue);
		}
		else {
			std::cerr << "Unknown attribut for way : " << attrName << std::endl;
		}
	}

	// Les references de nodes
	fillOsmWayNdRef(node, osmWay.nds);

	// Les tags du node
	fillOsmObjectTags(node, osmWay.tags);

	// Un petit filtre
	// if(!containsTag(osmWay.tags, types::TagKey::HIGHWAY)) {
	// 	continue;
	// }

	wayCallback(&osmWay, userData, mutex); 
}

static void browseXmlNode(
	pugi::xml_node & node,
	BrowseXmlOnNodeCallback nodeCallback,
    BrowseXmlOnWayCallback wayCallback,
    BrowseXmlOnRelationCallback relationCallback,
	void * userData,
	Mutex * mutex
) {
	std::string name = node.name();
	if(name.compare(types::OSM_OBJECT_NAME_NODE) == 0) {
		browserForNode(node, nodeCallback, userData, mutex);
	}
	else if(name.compare(types::OSM_OBJECT_NAME_WAY) == 0) {
		browserForWay(node, wayCallback, userData, mutex);
	}
	else if(name.compare(types::OSM_OBJECT_NAME_RELATION) == 0) {
		// TODO
	}
	else {

	}
}

void browseParallelFunction(
	BrowserThread & thread,
    void * args[]
) {
	pugi::xml_node doc = *((pugi::xml_node *) args[0]);
	OsmCallbacksHolder* ch = (OsmCallbacksHolder*) args[1];
	BrowseXmlOnNodeCallback nodeCallback = ch->nodeCallback;
	BrowseXmlOnWayCallback wayCallback = ch->wayCallback;
	BrowseXmlOnRelationCallback relationCallback = ch->relationCallback;
	void * userData = args[2];

	for(pugi::xml_node_iterator it = doc.begin(); it!=doc.end(); it++) {
		if(thread.shouldWork()) {
			
			std::cout << thread.getName() << " WORK " 
				<< " Start : " << thread.start
				<< " end : " << thread.end
				<< " cursor : " << thread.cursor << std::endl;

			pugi::xml_node node = *it;

			browseXmlNode(
				node,
				nodeCallback, wayCallback, relationCallback,
				userData,
				&thread.getMutex());
		}
		thread.step();
	}
}

void OsmXmlBrowser::browseParallel(
	BrowseXmlOnNodeCallback nodeCallback,
	BrowseXmlOnWayCallback wayCallback,
	BrowseXmlOnRelationCallback relationCallback, void * userData
) {
	auto osm_xml_tag = get_osm_xml_tag(this->doc);
	OsmCallbacksHolder callbacksHolder = {
		nodeCallback,
		wayCallback,
		relationCallback
	};
	void * args[] = {
		&osm_xml_tag,
		&callbacksHolder,
		userData,
	};
	BrowserThreadHandler threadHandler = BrowserThreadHandler(
		100,
		8,
		(ThreadBrowsingFunction)browseParallelFunction,
		args
	);
	threadHandler.work();

}

};