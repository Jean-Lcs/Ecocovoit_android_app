#ifndef _DEBUG_STUFF_
#define _DEBUG_STUFF_

#include <cassert>

/** true pour executer les morceaux de
 * code de debug et false pour ne pas le faire.
*/
#define COMPILE_MSG false

#define DEBUG(code) \
    if(COMPILE_MSG) { \
        code \
    }

#endif