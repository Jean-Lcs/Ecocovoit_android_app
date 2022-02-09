#ifndef _OSM_XML_BROWSER_THREAD_
#define _OSM_XML_BROWSER_THREAD_

#include "../utils/debug-stuff.h"
#include "../utils/value-types.h"

#include <pthread.h>

#include <vector>
#include <iostream>

namespace osm_parsing {
    class BrowserThreadHandler;
    class BrowserThread;

    typedef pthread_mutex_t Mutex;
    void mutexLock(Mutex * mutex);
    void mutexUnlock(Mutex * mutex);

    typedef void (*ThreadBrowsingFunction)(
        BrowserThread & thread,
        void * data
    );

    typedef struct {
        ThreadBrowsingFunction function;
    } ThreadBrowsingFunctionHolder;

class BrowserThread {
    friend BrowserThreadHandler;

    friend void * mainThreadFunction(BrowserThread * thread);

public:
    BrowserThread(
        BrowserThreadHandler * handler,
        value_types::Index start,
        value_types::Index end,
        ThreadBrowsingFunction function,
        void * data
    );

    void step();
    bool shouldWork();

    Mutex & getMutex();

    inline char * getName() { return this->name; }

    value_types::Index start;
    value_types::Index end;
    value_types::Index cursor;
private:
    
    void work();
    
    BrowserThreadHandler * handler;
    pthread_t thread;
    ThreadBrowsingFunction function;
    void * data;

    char * name;
};

class BrowserThreadHandler {
    friend BrowserThread;

public:
    BrowserThreadHandler(
        value_types::Index rangeSpan,
        unsigned int threadCount,
        ThreadBrowsingFunction function,
        void * data
    );

    void work();

private:
    std::vector<BrowserThread> threads;
    value_types::Index currentEnd;

    value_types::Index rangeSpan;
    unsigned int threadCount;

    Mutex mutex;

    void spreadWork(
        value_types::Index rangeSpan,
        unsigned int threadCount,
        ThreadBrowsingFunction function,
        void * data
    );
};

};

#endif
