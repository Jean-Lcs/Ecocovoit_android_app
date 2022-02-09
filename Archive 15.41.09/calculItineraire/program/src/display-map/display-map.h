#ifndef _DISPLAY_MAP_
#define _DISPLAY_MAP_

#include "../vendors/cimg/CImg.h"
#include "../path-find/path-finder.h"

namespace display_map {

typedef struct {
    value_types::Distance alphaX;
    value_types::Distance betaX;
    value_types::Distance alphaY;
    value_types::Distance betaY;
} MapToScreenSpace;

int mapToScreenX(value_types::Distance x, MapToScreenSpace & converter);
int mapToScreenY(value_types::Distance y, MapToScreenSpace & converter);

class DisplayMap {
public:
    DisplayMap(
        osm_parsing::Matrix * matrix,
        path_find::NodeResultList * path) :
        matrix(matrix), path(path) { }

    void start(
        value_types::Distance limUp, value_types::Distance limDown,
        value_types::Distance limLeft, value_types::Distance limRight,
        value_types::Distance screenWidth, value_types::Distance screenHeight,
        value_types::Distance borderX = 10,
        value_types::Distance borderY = 10
    );

private:
    osm_parsing::Matrix * matrix;
    path_find::NodeResultList * path;
};

};

#endif
