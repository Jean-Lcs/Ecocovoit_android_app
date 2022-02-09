#include "path-finder.h"

namespace path_find {

PathFinder::PathFinder(
        osm_parsing::Matrix * matrix,
        osm_parsing::MatrixNode * start,
        osm_parsing::MatrixNode * end)
    : matrix(matrix), startNode(start), endNode(end) {
    
    this->priorityQueue = new NodePriorityQueue();
    this->nodeResults = NodeResultList();
    this->nodeResults.reserve(matrix->getSize());
}

PathFinder::~PathFinder() {
    delete this->priorityQueue;
}

void PathFinder::initialise() {
    for(value_types::Index i=0; i<this->matrix->getSize(); i++) {
        osm_parsing::MatrixNode * mNode = this->matrix->getNode(i);
        if(mNode->getId() == this->startNode->getId()) {
            this->nodeResults.emplace_back(
                mNode,
                0,
                nullptr);
            this->priorityQueue->addNode(&this->nodeResults.back());
        }
        else {
            this->nodeResults.emplace_back(
                mNode,
                osm_parsing::MATRIX_DISTANCE_INFINITY,
                nullptr);
            this->priorityQueue->addNode(&this->nodeResults.back());
        }
    }
}

void PathFinder::majDistanceAndFather(
        Node * edgeStart, Node * edgeEnd,
        osm_parsing::MatrixCase * edge) {
    
    value_types::Distance dStart = edgeStart->getDistFromStart();
    value_types::Distance dEnd = edgeEnd->getDistFromStart();
    value_types::Distance poids = edge->getDistance();
    
    if(dEnd > dStart + poids) {
        edgeEnd->setDistFromStart(dStart + poids);
        edgeEnd->setFather(edgeStart);
    }
}

static Node * getNodeForMatrixNode(
        NodeResultList & nodes,
        osm_parsing::MatrixNode * mNode) {
    for(value_types::Index i=0; i<nodes.size(); i++) {
        Node * n = &(nodes.at(i));
        if(n->getMatrixNode()->getId() == mNode->getId())
            return n;
    }
    return nullptr;
}

std::vector<Node> PathFinder::getPath() {
    std::vector<Node> path = std::vector<Node>();
    Node * current = this->nodeForEnd;
    while(current != nullptr) {
        path.push_back(*current);
        current = current->getFather();
    }
    return path;
}

static void showQueue(std::vector<Node*> & queue) {
	std::cout << "Queue (size=" << queue.size() << ")" << std::endl;
	for(Node * n : queue) {
		std::cout << "\tDist : " << n->getDistFromStart() << std::endl;
	}
}

std::vector<Node> PathFinder::perform() {
    this->initialise();
    while(!this->priorityQueue->empty()) {
        Node * min = this->priorityQueue->getMin();

        if(min->getMatrixNode()->getId() == this->endNode->getId()) {
            this->nodeForEnd = min;
        }

        auto voisins = this->matrix->getVoisins(min->getMatrixNode());
        for(osm_parsing::Voisin & voisin : *voisins) {
            Node * n = getNodeForMatrixNode(this->nodeResults, voisin.voisin);

            DEBUG(assert(n != nullptr);)

            this->majDistanceAndFather(min, n, voisin.edge);
        }
        this->priorityQueue->update();

        delete voisins;
    }

    return this->getPath();
}

};