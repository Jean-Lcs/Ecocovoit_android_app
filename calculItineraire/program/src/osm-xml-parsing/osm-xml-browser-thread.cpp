#include "osm-xml-browser-thread.h"

namespace osm_parsing {
    void mutexLock(Mutex * mutex) {pthread_mutex_lock(mutex);}
    void mutexUnlock(Mutex * mutex) {pthread_mutex_unlock(mutex);}

BrowserThread::BrowserThread(
    BrowserThreadHandler * handler,
    value_types::Index start,
    value_types::Index end,
    ThreadBrowsingFunction function,
    void * data
) : handler(handler), start(start), end(end), function(function), data(data) {
    this->cursor = 0;
}

void BrowserThread::step() {
    this->cursor++;

    if(this->cursor >= this->end) {

        // IMPORTANT : CE BLOCK DOIT ETRE UNE ZONE PROTEGEE

        mutexLock(&(this->handler->mutex));

        // On passe a la fin des index de thread et on la met a jour
        this->start = this->handler->currentEnd;
        this->cursor = this->start;
        this->handler->currentEnd += this->handler->rangeSpan;
        this->end = this->handler->currentEnd;

        mutexUnlock(&(this->handler->mutex));
    }
}

bool BrowserThread::shouldWork() {
    if(this->cursor >= this->start && this->cursor < this->end) {
        return true;
    }
    return false;
}

Mutex & BrowserThread::getMutex() {
    return this->handler->mutex;
}

void * mainThreadFunction(BrowserThread * thread) {    
    thread->function(*thread, thread->data);
    return nullptr;
}

void BrowserThread::work() {
    pthread_create(
        &(this->thread), nullptr,
        (void*(*)(void*))mainThreadFunction, this);
}

BrowserThreadHandler::BrowserThreadHandler(
    value_types::Index rangeSpan,
    unsigned int threadCount,
    ThreadBrowsingFunction function,
    void * data
) : rangeSpan(rangeSpan), threadCount(threadCount) {
    this->threads = std::vector<BrowserThread>();
    this->threads.reserve(threadCount);

    this->mutex = PTHREAD_MUTEX_INITIALIZER;

    this->spreadWork(rangeSpan, threadCount, function, data);
}

void BrowserThreadHandler::spreadWork(
    value_types::Index rangeSpan,
    unsigned int threadCount,
    ThreadBrowsingFunction function,
    void * data
) {
    unsigned int i;
    for(i=0; i<threadCount; i++) {
        BrowserThread thread = BrowserThread(
            this,
            rangeSpan*i,
            rangeSpan*(i+1)-1,
            function,
            data
        );

        thread.name = new char[10];
        sprintf(thread.name, "TT%d", i);

        this->threads.push_back(thread);
    }
    this->currentEnd = rangeSpan*(i);
}

void BrowserThreadHandler::work() {
    for(auto & thread : this->threads) {
        thread.work();
    }

    for(auto & thread : this->threads) {
        pthread_join(thread.thread, nullptr);
    }
}

};