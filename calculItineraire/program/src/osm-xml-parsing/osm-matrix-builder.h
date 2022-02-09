#ifndef _OSM_MATRIX_BUILDER_
#define _OSM_MATRIX_BUILDER_

#include "../utils/debug-stuff.h"
#include "../utils/performances.h"

#include <math.h>
#include <iostream>

#include "osm-xml-browser.h"
#include "osm-matrix.h"

namespace osm_parsing {

    static const long DEFAULT_MATRIX_SIZE = 1000;

typedef double (*CalculBaseDistanceFunction)(
    const MatrixNode & fromNode,
    const MatrixNode & toNode,
    std::vector<types::Tag> & tags);

/** Définition de la classe permettant de construire une osm matrice
à partir d'un contenu xml. 
*/
class MatrixBuilder {

    /**Les fonctions amies qui modifierons les membres 
     * de classes pour construire la matrice.*/

    /**Fonction appelee quand le browser rencontre une node.*/
    friend void browseXmlOnNodeCallback(
        types::Node* n, MatrixBuilder * builder, Mutex *mutex);
    /**Fonction appelee quand le browser rencontre un way.*/
    friend void browseXmlOnWayCallback(
        types::Way* w, MatrixBuilder * builder, Mutex *mutex);
    /**Fonction appelee quand le browser rencontre une relation.*/
    friend void browseXmlOnRelationCallback(
        types::Relation* r, MatrixBuilder * builder, Mutex *mutex);

public:
    MatrixBuilder(const char * xmlCode);
    MatrixBuilder(const char * xmlCode,
        CalculBaseDistanceFunction calculBaseDistanceFunction);
    ~MatrixBuilder();

    /**Lance l'interprétation du xml et la création de la matrice.
     * Retourne la matrice.*/
    Matrix * build(unsigned int startMatrixSize);

    /**Retourne la matrice. Vaut nullptr avant l'appel de this->build()*/
    inline Matrix * getMatrix() const { return this->matrix; }

    /**Retourne le pointeur vers la fonction de calcul de distance 
     * entre deux nodes consecutifs d'un way.*/
    inline CalculBaseDistanceFunction getCalculBaseDistanceFunction() const
        { return this->calculBaseDistanceFunction; }
    /**Change le pointeur vers la fonction de calcul de distance 
     * entre deux nodes consecutifs d'un way.*/
    inline void setCalculBaseDistanceFunction(CalculBaseDistanceFunction function)
        { this->calculBaseDistanceFunction = function; }

    /**Retourne le nombre de nodes contenu dans la matrice.
     * Vaut 0 avant l'appel de this->build().*/
    inline value_types::Index getNodeCount() const { return this->matrix->getSize(); }

private:
    OsmXmlBrowser * browser;
    Matrix * matrix;

    /**La fonction de calcul de distance.*/
    CalculBaseDistanceFunction calculBaseDistanceFunction = nullptr;    
    /**La fonction de calcul de distance par défaut.*/
    double defaultCalculBaseDistance(
        const MatrixNode & fromNode,
        const MatrixNode & toNode,
        std::vector<types::Tag> & tags);
};

};

#endif
