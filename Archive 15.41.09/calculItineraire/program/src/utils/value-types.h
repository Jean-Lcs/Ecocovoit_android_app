#ifndef _TYPES_
#define _TYPES_

#include <limits>

namespace value_types {
    typedef long double Distance;
    typedef unsigned long Id;
    typedef unsigned long Index;

    static const Distance MAX_DISTANCE = std::numeric_limits<Distance>::max();
    static const Id MAX_ID = std::numeric_limits<Id>::max();
    static const Index MAX_INDEX = std::numeric_limits<Index>::max();    
};

#endif
