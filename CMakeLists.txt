cmake_minimum_required(VERSION 3.12)
project(Chat)

set(CMAKE_CXX_STANDARD 14)

set(CMAKE_INCLUDE_CURRENT_DIR  ON)
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTOUIC ON)

find_package(Qt5Core)
find_package(Qt5Widgets)
find_package(Qt5Network)

add_subdirectory(lib/cryptopp)

set(SOURCES
        src/ui/mainwindow.ui
        src/ui/mainwindow.cpp
        src/main.cpp src/package.cpp include/package.h src/user.cpp include/user.h src/communication.cpp include/communication.h)

add_executable(${PROJECT_NAME} ${SOURCES})
target_link_libraries(${PROJECT_NAME} Qt5::Widgets Qt5::Network)
target_link_libraries(${PROJECT_NAME} cryptopp)
