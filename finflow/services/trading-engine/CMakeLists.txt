cmake_minimum_required(VERSION 3.10)
project(TradingEngine)

# Set C++ standard
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Add executable
add_executable(orderbook
    src/orderbook/main.cpp
    src/orderbook/order.cpp
    src/orderbook/orderbook.cpp
)

# Include directories
target_include_directories(orderbook PRIVATE
    src/orderbook
)

# Enable warnings
if(MSVC)
    target_compile_options(orderbook PRIVATE /W4)
else()
    target_compile_options(orderbook PRIVATE -Wall -Wextra -Wpedantic)
endif()
