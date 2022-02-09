#include "display-map.h"

namespace display_map {

int mapToScreenX(value_types::Distance x, MapToScreenSpace & converter) {
    return converter.alphaX*x+converter.betaX;
}

int mapToScreenY(value_types::Distance y, MapToScreenSpace & converter) {
    return converter.alphaY*y+converter.betaY;
}

void DisplayMap::start(
        value_types::Distance limUp, value_types::Distance limDown,
        value_types::Distance limLeft, value_types::Distance limRight,
        value_types::Distance screenWidth, value_types::Distance screenHeight,
        value_types::Distance borderX,
        value_types::Distance borderY) {
    
    cimg_library::CImg<unsigned char> block(screenWidth+2*borderX, screenHeight+2*borderY, 1, 3, 0);

    MapToScreenSpace converter = {
        screenWidth/(limRight-limLeft),
        borderX-( screenWidth/(limRight-limLeft) )*limLeft,
        screenHeight/(limDown-limUp),
        borderY-( screenHeight/(limDown-limUp) )*limUp
    };

    std::cout << "Converter :"
		<< " alphaX=" << converter.alphaX 
		<< " alphaY=" << converter.alphaY
		<< " betaX=" << converter.betaX
		<< " betaY=" << converter.betaY
		<< std::endl;

    const unsigned char colorNode[] = {160, 2, 0};
    const unsigned char colorText[] = {255, 255, 255};
    const unsigned char colorArc[] = {255, 255, 0};
    for(value_types::Index i=0; i<this->matrix->getSize(); i++) {
        auto node = this->matrix->getNode(i);
        
        // std::cout
        //     << "Point : " << node->getLatitude() << ", "
        //     << node->getLongitude() << std::endl;
        
        int x = mapToScreenX(node->getLatitude(), converter);
        int y = mapToScreenY(node->getLongitude(), converter);
        
        //std::cout << "\t" << x << ", " << y << std::endl;

        block.draw_circle(x, y, 2, colorNode);

        char * text = new char[5];
        sprintf(text, "%ld", i);
        block.draw_text(x, y, text, colorText, 0, 1.f, 10);


        auto voisins = this->matrix->getVoisins(node);
        for(osm_parsing::Voisin v : *voisins) {
            int xv = mapToScreenX(v.voisin->getLatitude(), converter);
            int yv = mapToScreenY(v.voisin->getLongitude(), converter);
            block.draw_line(x, y, xv, yv, colorArc);
        }
        delete voisins;
    }

    if(path != nullptr) {
        const unsigned char colorPath[] = {0, 200, 0};
        int xprev = INT32_MAX;
        int yprev = INT32_MAX;
        int x;
        int y;
        for(value_types::Index i=0; i<this->path->size(); i++) {
            auto node = &(this->path->at(i));
            x = node->getMatrixNode()->getLatitude();
            y = node->getMatrixNode()->getLongitude();
            //std::cout << "Point : " << x << ", " << y << std::endl;
            x = mapToScreenX(x, converter);
            y = mapToScreenY(y, converter);
            //std::cout << "\t" << x << ", " << y << std::endl;

            if(xprev != INT32_MAX && yprev != INT32_MAX) {
                block.draw_line(x, y, xprev, yprev, colorPath);
            }
            else {
                const unsigned char colorStart[] = {0, 0, 200};
                block.draw_circle(x, y, 4, colorStart);
            }

            xprev = x;
            yprev = y;
        }
        const unsigned char colorEnd[] = {0, 100, 200};
        block.draw_circle(x, y, 6, colorEnd);
    }

    cimg_library::CImgDisplay display(block);
    while(!display.is_closed()) {
        display.wait();
    }
}

};

