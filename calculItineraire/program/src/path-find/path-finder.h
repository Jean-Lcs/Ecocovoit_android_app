#ifndef _PATH_FINDER_
#define _PATH_FINDER_

#include "path-finder-types.h"

namespace path_find {

/**Classe effectuant l'algorithme de Dijkstra sur une matrice.*/
class PathFinder {
public:
    PathFinder(
        osm_parsing::Matrix * matrix,
        osm_parsing::MatrixNode * start,
        osm_parsing::MatrixNode * end);
    ~PathFinder();

    /**Lance l'algorithme et retourne le plus court chemin trouve.*/
    std::vector<Node> perform();

private:
    osm_parsing::Matrix * matrix;
    osm_parsing::MatrixNode * startNode;
    osm_parsing::MatrixNode * endNode;

    /**La file de priorite*/
    NodePriorityQueue * priorityQueue;
    /**L'arbre de parcours et liste de distance.*/
    NodeResultList nodeResults;

    Node * nodeForEnd = nullptr;

    void initialise();
    
    void majDistanceAndFather(
        Node * n1, Node * n2,
        osm_parsing::MatrixCase * edge);

    std::vector<Node> getPath();
};

};

#endif
