#include "osm-matrix-builder.h"

namespace osm_parsing {

MatrixBuilder::MatrixBuilder(
        const char * xmlCode) {
    this->browser = new OsmXmlBrowser(xmlCode);
    this->matrix = nullptr;
}

MatrixBuilder::MatrixBuilder(
        const char * xmlCode,
        CalculBaseDistanceFunction calculBaseDistanceFunction) {
    this->browser = new OsmXmlBrowser(xmlCode);
    this->matrix = nullptr;
    this->calculBaseDistanceFunction = calculBaseDistanceFunction;
}

MatrixBuilder::~MatrixBuilder() {
    delete this->browser;
}

void browseXmlOnNodeCallback(
    types::Node* n,
    MatrixBuilder * builder,
    Mutex *mutex
) {
    MatrixNode node(n->id, n->lon, n->lat);

    DEBUG(std::cout << "Node : " << node.getId() <<
        " lat=" << node.getLatitude() <<
        " lon=" << node.getLongitude() <<
        std::endl;)

    mutexLock(mutex);
    builder->getMatrix()->addNode(node);
    mutexUnlock(mutex);
}
void browseXmlOnWayCallback(
    types::Way* w,
    MatrixBuilder * builder,
    Mutex *mutex
) {
    DEBUG(std::cout << "Way" << std::endl;)
    auto nodes = w->nds;
    value_types::Index prevId = value_types::MAX_ID;
    for(auto currentId : nodes) {
        if(prevId == value_types::MAX_ID) {
            prevId = currentId;
            continue;
        }

        // On recupere les node pour le calcul de distance de base
        MatrixNode * prevNode, * currentNode;
        prevNode = 
            builder->getMatrix()->getNodeById(prevId);
        if(prevNode == nullptr) {
            // Le node est encore inconnue
            // TODO : Que faire ??? On passe ou on le rajoute ?
            return;
        }
        currentNode = builder->getMatrix()->getNodeById(currentId);
        if(currentNode == nullptr) {
            // Le node est encore inconnue
            // TODO : Que faire ??? On passe ou on le rajoute ?
            return;
        }

        DEBUG(
            std::cout << "\tmade of Node : " << prevNode->getId() <<
            " lat=" << prevNode->getLatitude() <<
            " lon=" << prevNode->getLongitude() <<
            std::endl;
            std::cout << "\tto Node : " << currentNode->getId() <<
            " lat=" << currentNode->getLatitude() <<
            " lon=" << currentNode->getLongitude() <<
            std::endl;
        )

        // TODO : Que faire avec les tags ?

        // On calcule la distance de base
        MatrixCase c;
        if(builder->getCalculBaseDistanceFunction() == nullptr)
            c.setDistance(builder->defaultCalculBaseDistance(
                *prevNode, *currentNode,
                w->tags));
        else
            c.setDistance(builder->getCalculBaseDistanceFunction()(
                    *prevNode, *currentNode,
                    w->tags));

        // On ajoute l'arc a la matrice
        builder->getMatrix()->setCaseByIds(
            prevId, currentId, c);

        prevId = currentId;
    }
}
void browseXmlOnRelationCallback(
    types::Relation* r,
    MatrixBuilder * builder,
    Mutex *mutex) {

}

double MatrixBuilder::defaultCalculBaseDistance(
    const MatrixNode & fromNode,
    const MatrixNode & toNode,
    std::vector<types::Tag> & tags) {

    return sqrt(
        pow(fromNode.getLatitude() - toNode.getLatitude(), 2) +
        pow(fromNode.getLongitude() - toNode.getLongitude(), 2)
    );
}

Matrix * MatrixBuilder::build(unsigned int startMatrixSize) {
    this->matrix = new Matrix(startMatrixSize);
    this->matrix->clear();

    DEBUG(std::cout << "Start Building" << std::endl;)
    // On commence à 0 nodes dans la matrice
    this->browser->browseParallel(
        (BrowseXmlOnNodeCallback) browseXmlOnNodeCallback,
        (BrowseXmlOnWayCallback) browseXmlOnWayCallback,
        (BrowseXmlOnRelationCallback) browseXmlOnRelationCallback,
        this);
    DEBUG(std::cout << "End Building" << std::endl;)

    return this->matrix;
}
};
