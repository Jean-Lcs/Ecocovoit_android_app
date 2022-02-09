#ifndef _OSM_MATRIX_
#define _OSM_MATRIX_

#include "../utils/debug-stuff.h"
#include "../utils/value-types.h"

#include <iostream>
#include <string.h>
#include <vector>

#include <pthread.h>

/*
arcs : poids, 
noeuds : id-osm
*/
namespace osm_parsing {

    class Matrix;

    /**La valeur pour un indexe sans valeur.*/
    static const value_types::Index MATRIX_NO_INDEX = value_types::MAX_INDEX;
    /**La valeur de l'infini.
     * Dans la matrice, les nodes qui ne sont pas voisins sont distant
     * de l'infini.*/
    static const value_types::Distance MATRIX_DISTANCE_INFINITY = value_types::MAX_DISTANCE;

/**Represente un noeud dans la matrice.
 * Contient toutes les donnees sur un node
 * necessaire a une recherche de chemin.
*/
class MatrixNode {
public:
    MatrixNode()
    : id(0), lon(0.0), lat(0.0) { }
    MatrixNode(value_types::Id id, double lon, double lat) 
    : id(id), lon(lon), lat(lat) { }

    inline value_types::Id getId() const { return this->id; }
    inline value_types::Distance getLongitude() const { return this->lon; }
    inline value_types::Distance getLatitude() const { return this->lat; }

    inline void setLongitude(value_types::Distance lon) { this->lon = lon; }
    inline void setLatitude(value_types::Distance lat) { this->lat = lat; }

private:
    value_types::Id id;
    value_types::Distance lon;
    value_types::Distance lat;
};

/**Represente une case de la matrice.
 * Donc un arc, du graphe represente par la matrice,
 * Qui vas d'un node vers un autre.
*/
class MatrixCase {
public:
    MatrixCase() 
    : distance(0.0) {}
    MatrixCase(value_types::Distance distance) 
    : distance(distance) {}

    /**Retourne la distance (ou poids) de l'arc represente par cette case.*/
    inline value_types::Distance getDistance() const { return this->distance; }

    /**Fixe la distance (ou poids) de l'arc represente par cette case.*/
    inline void setDistance(value_types::Distance distance) { this->distance = distance; }

private:
    value_types::Distance distance;
};

/**Represente une relation de voisinage entre un node donne
 * et un autre.*/
class Voisin {
public:
    Voisin(
        MatrixNode * voisin,
        MatrixCase * edge) : voisin(voisin), edge(edge){}

    /**Le node voisin.*/
    MatrixNode * voisin;
    /**L'arc.*/
    MatrixCase * edge;
};

/**Represente une matrice dont les labels de lignes sont des MatrixNode
 * et les cases correspondant à une ligne et une colone sont des MatrixCases.*/
class Matrix {
public:
    Matrix(value_types::Index size);
    ~Matrix();

    MatrixCase * getCase(value_types::Index i, value_types::Index j) const;
    void setCase(value_types::Index i, value_types::Index j, const MatrixCase & c);
    MatrixCase * getCaseByIds(value_types::Id idFrom, value_types::Id idTo) const;
    void setCaseByIds(value_types::Id idFrom, value_types::Id idTo, const MatrixCase & c);

    MatrixNode * getNodeById(value_types::Id id) const;
    void setNode(value_types::Index i, const MatrixNode & n);
    MatrixNode * getNode(value_types::Index i) const;

    void addNode(const MatrixNode & node);

    std::vector<Voisin> * getVoisins(MatrixNode * n) const;

    inline value_types::Index getSize() const { return this->size; }

    void clear();

    void showNodes(value_types::Index usedFromIndex, value_types::Index usedToIndex) const;
    void showContent() const;
    void showRawContent() const;

private:
    value_types::Index size;
    std::vector<MatrixNode> nodes;
    std::vector<MatrixCase> cases;

    value_types::Index allocSize;
    value_types::Index allocExtraSize = 10000;
    void allocMore();

    value_types::Index getCaseIndex(value_types::Index i, value_types::Index j) const;
    value_types::Index getNodeIndex(value_types::Id id) const;
};

};

#endif