#ifndef _PATH_FINDER_TYPES_
#define _PATH_FINDER_TYPES_

#include "../osm-xml-parsing/osm-matrix.h"

#include <functional>
#include <queue>
#include <vector>

namespace path_find {

/**Representation d'un node de graphe 
 * pour le resultat de l'algorithme de Dijkstra.
 * Il contient la distance au point de depart du node et
 * le pere du node dans l'arbre de parcours.
 * Il Contient aussi une reference au node de matrice
 * pour qu'on puisse acceder au donnees de la matrice.*/
class Node {
public:
    Node();
    Node(
        osm_parsing::MatrixNode * matrixNode,
        value_types::Distance distFromStart,
        Node * father
    );

    inline value_types::Distance getDistFromStart() { return this->distFromStart; }
    inline Node * getFather() { return this->father; }
    inline osm_parsing::MatrixNode * getMatrixNode() { return this->matrixNode; }

    inline void setDistFromStart(value_types::Distance dist) { this->distFromStart = dist; }
    inline void setFather(Node * f) { if(this != f) this->father = f; }

private:
    value_types::Distance distFromStart;
    Node * father;

    osm_parsing::MatrixNode * matrixNode;
};

    /**Fonction de comparaison de node pour la file de priorite
     * de l'algorithme de Dijkstra.*/
    auto NodeCompareLambda = 
        [](
            Node * n1,
            Node * n2
        ) {
            return n1->getDistFromStart() >= n2->getDistFromStart();
        };

/**Implementation de la file de priorite
 * pour l'algorithme de Dijkstra.*/
class NodePriorityQueue {
public:
    NodePriorityQueue();

    void addNode(Node * node);
    Node * getMin();
    void update();
    inline bool empty() { return this->nodes.empty(); }
    inline std::vector<Node*> & getNodes() { return this->nodes; }

private:
    std::vector<Node*> nodes;
}; 

    typedef std::vector<Node> NodeResultList;

};

#endif
