#ifndef _PERFORMANCE_
#define _PERFORMANCE_

#include <chrono>
#include <iostream>

namespace performance {

typedef std::ratio<1l, 1000000000000l> pico;
typedef std::chrono::duration<long long, pico> picoseconds;
typedef std::ratio<1l, 1000000000l> nano;
typedef std::chrono::duration<long long, nano> nanoseconds;
typedef std::ratio<1l, 1000000l> micro;
typedef std::chrono::duration<long long, micro> microseconds;
typedef std::ratio<1l, 1000l> milli;
typedef std::chrono::duration<long long, milli> milliseconds;

#define TIME_MEASURE_START(id) \
    const auto id ## start = std::chrono::high_resolution_clock::now();

#define TIME_MEASURE_END(id) \
    const auto id ## end = std::chrono::high_resolution_clock::now();

#define TIME_MEASURE_RESULT(id, unite) std::chrono::duration_cast<unite>(id ## end - id ## start).count()


/*
EXEMPLE D'UTILISATION
TIME_MEASURE_START
    ... operations in the same fonction
TIME_MEASURE_END
std::cout << TIME_MEASURE_RESULT(performance::nanoseconds) << "nano secondes" << std::endl;
*/

}

#endif
