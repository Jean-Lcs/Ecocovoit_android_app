#include "osm-matrix.h"

namespace osm_parsing {

Matrix::Matrix(value_types::Index size) {
    this->size = 0;
    this->nodes = std::vector<MatrixNode>();
    this->nodes.reserve(size);
    this->cases = std::vector<MatrixCase>();
    this->cases.reserve(size*size);

    this->nodes.insert(
        this->nodes.end(),
        size,
        MatrixNode(0, 0, 0)
    );
    this->cases.insert(
        this->cases.end(),
        size*size,
        MatrixCase(0)
    );

    std::cout << "MATRIX INIT DONE" << std::endl;

    //exit(1);

    this->allocSize = size;
}

Matrix::~Matrix() {}

// static void checkMatrixSizes(
//         std::vector<MatrixNode> nodes,
//         std::vector<MatrixCase> cases,
//         value_types::Index size) {
//     if(nodes.size() != size) {
//         std::cerr << "Nodes count don't match matrix size. nodes.size = " 
//             << nodes.size()
//             << "matrix size = " << size << std::endl;
//         DEBUG(assert(false);)
//     }
//     if(cases.size() != size*size) {
//         std::cerr << "Cases count don't match matrix size*size. cases.size = " 
//             << cases.size()
//             << "matrix size*size = " << size*size << std::endl;
//         DEBUG(assert(false);)
//     }
// }

value_types::Index Matrix::getCaseIndex(
        value_types::Index i, value_types::Index j) const {
    
    value_types::Index res;
    if(j<=i) {
        res = i*i + j;
    }
    else {
        res = (j-1)*(j-1) + (2*j+1) - i;
    }

    if(res < this->size*this->size)
        return res;
    else
        return MATRIX_NO_INDEX;
}

value_types::Index Matrix::getNodeIndex(
        value_types::Id id) const {
    for(value_types::Index i=0; i<this->size; i++) {
        if(this->nodes[i].getId() == id) {
            return i;
        }
    }
    return MATRIX_NO_INDEX;
}

MatrixCase * Matrix::getCase(
        value_types::Index i, value_types::Index j) const {
    value_types::Index index = getCaseIndex(i, j);
    if(index == MATRIX_NO_INDEX)
        return nullptr;
    return (MatrixCase *) &(this->cases[index]);
}

void Matrix::setCase(
        value_types::Index i, value_types::Index j,
        const MatrixCase & c) {
    value_types::Index index = getCaseIndex(i, j);
    if(index == MATRIX_NO_INDEX)
        return;
    this->cases[index] = c;
}

MatrixCase * Matrix::getCaseByIds(
        value_types::Id idFrom, value_types::Id idTo) const {
    value_types::Index indexFrom = getNodeIndex(idFrom);
    value_types::Index indexTo = getNodeIndex(idTo);
    if(indexFrom == MATRIX_NO_INDEX || indexTo == MATRIX_NO_INDEX)
        return nullptr;
    return (MatrixCase *) &(this->cases[getCaseIndex(indexFrom, indexTo)]);
}

void Matrix::setCaseByIds(
        value_types::Id idFrom, value_types::Id idTo,
        const MatrixCase & c) {
    value_types::Index indexFrom = getNodeIndex(idFrom);
    value_types::Index indexTo = getNodeIndex(idTo);
    if(indexFrom == MATRIX_NO_INDEX || indexTo == MATRIX_NO_INDEX)
        return;
    this->cases[getCaseIndex(indexFrom, indexTo)] = c;
    std::cout << "from : " << indexFrom << " to : " << indexTo << std::endl;
    std::cout << "Case : " << getCaseIndex(indexFrom, indexTo) << std::endl;
    // this->cases[getCaseIndex(indexFrom, indexTo)] = 100;
}

MatrixNode * Matrix::getNodeById(
        value_types::Id id) const {
    value_types::Index index = getNodeIndex(id);
    if(index == MATRIX_NO_INDEX)
        return nullptr;
    else
        return (MatrixNode *) &(this->nodes[index]);
}

MatrixNode * Matrix::getNode(
        value_types::Index i) const {
    if(i < this->size)
        return (MatrixNode*) &(this->nodes[i]);
    return nullptr;
}

void Matrix::setNode(
        value_types::Index i,
        const MatrixNode & n) {
    if(i < this->size)
        this->nodes[i] = n;
}

void Matrix::allocMore() {
    if(this->size >= this->allocSize) {
        this->allocSize += this->allocExtraSize;
        this->nodes.reserve(this->allocSize);
        this->cases.reserve(this->allocSize*this->allocSize);

        this->nodes.insert(
            this->nodes.end(),
            this->allocExtraSize,
            MatrixNode(0, 0, 0)
        );
        this->cases.insert(
            this->cases.end(),
            this->allocExtraSize*this->allocExtraSize,
            MatrixCase(0)
        );
    }
}
void Matrix::addNode(const MatrixNode & node) {
    value_types::Index indexNode = this->size;
    this->size++;
    // Ajoutons le node
    this->setNode(indexNode, node);

    allocMore();
}

std::vector<Voisin> * Matrix::getVoisins(
        MatrixNode * n) const {
    std::vector<Voisin> * res = new std::vector<Voisin>();
    value_types::Index j = getNodeIndex(n->getId());
    for(value_types::Index i = 0; i < this->size; i++) {
       MatrixCase * c = getCase(j, i);
       if(c->getDistance() < MATRIX_DISTANCE_INFINITY && c->getDistance() > 0) {
           res->emplace_back(getNode(i), c);
       }
    }

    return res;
}

void Matrix::clear() {
    memset(static_cast<void*>(this->nodes.data()), 0, this->size);
    memset(static_cast<void*>(this->cases.data()), 0, this->size*this->size);
}

void Matrix::showNodes(
        value_types::Index usedFromIndex,
        value_types::Index usedToIndex) const {
    /**Affichage des nodes que contient la matrice.*/
    for(value_types::Index i=usedFromIndex; i<usedToIndex; i++) {
        MatrixNode * node = this->getNode(i);
        std::cout << "Node " << node->getId() <<
            ": lat=" << node->getLatitude() <<
            " lon=" << node->getLongitude() <<
            std::endl;
    }
}

void Matrix::showContent() const {
    for(value_types::Index i=0; i<this->size; i++) {
        for(value_types::Index j=0; j<this->size; j++) {
            MatrixCase * c = getCase(i, j);
            std::cout << c->getDistance() << "  ";
        }
        std::cout << std::endl;
    }
}

void Matrix::showRawContent() const {
    for(value_types::Index i=0; i<this->size*this->size; i++) {
        std::cout << this->cases.data()[i].getDistance() << " ";
    }
    std::cout << std::endl;
}

}