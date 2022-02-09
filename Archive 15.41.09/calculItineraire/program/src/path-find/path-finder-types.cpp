#include "path-finder-types.h"

namespace path_find {

Node::Node() :
    distFromStart(osm_parsing::MATRIX_DISTANCE_INFINITY),
    father(nullptr),
    matrixNode(nullptr) {}

Node::Node(
        osm_parsing::MatrixNode * matrixNode,
        value_types::Distance distFromStart = osm_parsing::MATRIX_DISTANCE_INFINITY,
        Node * father = nullptr) :
    distFromStart(distFromStart),
    father(father),
    matrixNode(matrixNode) { }

NodePriorityQueue::NodePriorityQueue() {
    this->nodes = std::vector<Node*>();
}

void NodePriorityQueue::addNode(Node * node) {
    this->nodes.push_back(node);
    std::push_heap(this->nodes.begin(), this->nodes.end(), NodeCompareLambda);
}

Node * NodePriorityQueue::getMin() {
    std::pop_heap(this->nodes.begin(), this->nodes.end(), NodeCompareLambda);
    Node * min = this->nodes.back();
    this->nodes.pop_back();
    return min;
}

void NodePriorityQueue::update() {
    std::make_heap(this->nodes.begin(), this->nodes.end(), NodeCompareLambda);
}

};