#include "osm-request/osm-request.h"
#include "osm-xml-parsing/osm-matrix-builder.h"
#include "path-find/path-finder.h"

#include "display-map/display-map.h"

#include "osm-request/request.h"

#include <fstream>

static void showVoisins(std::vector<osm_parsing::Voisin> * voisins, value_types::Index limit = value_types::MAX_INDEX) { 
	for(value_types::Index i=0; i< voisins->size() && i < limit; i++) {
		osm_parsing::Voisin & voisin = voisins->at(i);
		std::cout << "\t" << voisin.voisin->getId() << std::endl;
	}
}
static void showVoisinInMatrix(osm_parsing::Matrix * matrix) {
	for(value_types::Index i=0; i< matrix->getSize(); i++) {
		auto node = matrix->getNode(i);
		auto voisins = matrix->getVoisins(node);
		std::cout << i << " Node : " << node->getId() << " has " << voisins->size() << " neighbors " << std::endl;
		showVoisins(voisins, 100);
	}
}

static std::string readFile(const char * filename) {
	std::string res = "";
	std::string line;
	std::ifstream file(filename);
	if(file.is_open()) {
		while(std::getline(file, line)) {
			if(line.front() == '|')
				break;
			res += line+"\n";
		}
		file.close();
	}
	else {
		std::cerr << "Could'n read the file '" << filename << "'" << std::endl;
	}
	return res;
}

static void writeFile(const char * filename, std::string content) {
	std::ofstream mfile;
	mfile.open(filename);
	if(mfile.is_open())  {
		mfile << content << std::endl;
		mfile.close();
	}
	else {
		std::cerr << "Could'n write in the file '" << filename << "'" << std::endl;
	}
}

static void showPath(std::vector<path_find::Node> & path) {
	std::cout << "Path (size=" << path.size() << ")" << std::endl;
	for(value_types::Index i=path.size()-1; i>=0; i--) {
		osm_parsing::MatrixNode * n = path.at(i).getMatrixNode();
		std::cout << " Node " << n->getId() << std::endl;
		if(i==0) {
			break;
		}
	}
}

static value_types::Distance getMinLatitude(osm_parsing::Matrix * matrix) {
	if(matrix->getSize() < 1)
		return 0;
	value_types::Distance min = matrix->getNode(0)->getLatitude();
	for(value_types::Index i=1; i<matrix->getSize(); i++) {
		value_types::Distance lat = matrix->getNode(i)->getLatitude();
		if(lat < min) {
			min = lat;
		}
	}
	return min;
}

static value_types::Distance getMaxLatitude(osm_parsing::Matrix * matrix) {
	if(matrix->getSize() < 1)
		return 0;
	value_types::Distance max = matrix->getNode(0)->getLatitude();
	for(value_types::Index i=1; i<matrix->getSize(); i++) {
		value_types::Distance lat = matrix->getNode(i)->getLatitude();
		if(lat > max) {
			max = lat;
		}
	}
	return max;
}

static value_types::Distance getMinLongitude(osm_parsing::Matrix * matrix) {
	if(matrix->getSize() < 1)
		return 0;
	value_types::Distance min = matrix->getNode(0)->getLongitude();
	for(value_types::Index i=1; i<matrix->getSize(); i++) {
		value_types::Distance lat = matrix->getNode(i)->getLongitude();
		if(lat < min) {
			min = lat;
		}
	}
	return min;
}

static value_types::Distance getMaxLongitude(osm_parsing::Matrix * matrix) {
	if(matrix->getSize() < 1)
		return 0;
	value_types::Distance max = matrix->getNode(0)->getLongitude();
	for(value_types::Index i=1; i<matrix->getSize(); i++) {
		value_types::Distance lat = matrix->getNode(i)->getLongitude();
		if(lat > max) {
			max = lat;
		}
	}
	return max;
}

unsigned countNodeInXmlString(const char * xml, unsigned size) {
	unsigned length = 0;
	const char * cursor = xml;
	do{
		std::cout << "t = " << cursor[0] << std::endl;
		cursor = (const char*) memmem(cursor+1, size - (cursor-xml), "<node", strlen("<node"));
		length++;
	}
	while(cursor != nullptr);
	return length;
}

#define FILE_NAME_REQUEST "execution-data/query.txt"
#define FILE_NAME_REQUEST_RESPONSE "execution-data/response.xml"

int main(int argc, char *argv[]) {

	//********* QUERY OSM MAP TO OVERPASS

	std::string q = readFile(FILE_NAME_REQUEST);
//	std::string q = std::string(requeteRoute(latMin, lonMin, latMax, lonMax, rayon, highway_tag));

	std::cout << "REQUEST : \n" << q << std::endl;

	TIME_MEASURE_START(response_time)
	std::string xml = osm_request::query_xml_map(
		q,
		osm_request::DEFAULT_OSM_URL
	);
	TIME_MEASURE_END(response_time)

	int length = countNodeInXmlString(xml.data(), xml.size());

	// writeFile(FILE_NAME_REQUEST_RESPONSE, xml);
	
	//********* BUILD MAP MATRIX
	osm_parsing::MatrixBuilder * builder = 
		new osm_parsing::MatrixBuilder(xml.data());

	TIME_MEASURE_START(build_time)
		auto matrix = builder->build(length*3);
	TIME_MEASURE_END(build_time)
	
	delete builder;
	delete xml.data();

	std::cout << std::endl << "MATRIX BUILDED" << std::endl;
	showVoisinInMatrix(matrix);

	//********* PERFORM TEST PATH SEARCH *************************************************
	auto startNode = matrix->getNode(4);
	auto endNode = matrix->getNode(5);
	path_find::PathFinder pathFinder = path_find::PathFinder(
		matrix,
		startNode,
		endNode
	);

	TIME_MEASURE_START(path_time)
	auto path = pathFinder.perform();
	TIME_MEASURE_END(path_time)

	std::cout << std::endl << "PATH FOUNDED" << std::endl;
	std::cout << "Start Node " << startNode->getId() << std::endl;
	std::cout << "End Node " << endNode->getId() << std::endl;
	showPath(path);

	//********* DISPLAY MAP *************************************************
	display_map::DisplayMap * display = new display_map::DisplayMap(matrix, &path);
	value_types::Distance minLat = (long) getMinLatitude(matrix);
	value_types::Distance maxLat = (long) getMaxLatitude(matrix);
	value_types::Distance minLon = (long) getMinLongitude(matrix);
	value_types::Distance maxLon = (long) getMaxLongitude(matrix);
	std::cout
		<< " minLat=" << minLat 
		<< " maxLat=" << maxLat
		<< " minLon=" << minLon
		<< " maxLon=" << maxLon
		<< std::endl;
	display->start(
		minLon, maxLon,
		minLat, maxLat,
		1100, 600);

	delete display;
	delete matrix;

	//********* PERFORMANCE RESULTS
	std::cout << std::endl << "PERFORMANCE RESULTS" << std::endl;
	std::cout << "Response waited for "
		<< TIME_MEASURE_RESULT(response_time, performance::milliseconds)
		<< " millisseconds" << std::endl;
	std::cout << "Matrix builded in "
		<< TIME_MEASURE_RESULT(build_time, performance::milliseconds)
		<< " millisseconds" << std::endl;
	std::cout << "Path found in "
		<< TIME_MEASURE_RESULT(path_time, performance::milliseconds)
		<< " millisseconds" << std::endl;

	return 0;
}
