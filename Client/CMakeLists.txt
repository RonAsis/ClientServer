
cmake_minimum_required(VERSION 3.13)   # CMake version check
project(Client)               # Create project "simple_example"
set(CMAKE_CXX_STANDARD 11)            # Enable c++11 standard

set(SOURCE_FILES src/ClientMain.cpp)            # Add main.cpp file of project root directory as source file
add_executable(Client src/ConnectionHandler.cpp
               include/ConnectionHandler.h
               src/Client.cpp include/Client.h
               src/ClientMain.cpp
        makefile)