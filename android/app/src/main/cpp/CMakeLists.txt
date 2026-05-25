cmake_minimum_required(VERSION 3.22.1)

project("pound_android")

# Path to the original Pound source
set(POUND_ROOT ../../../../../)

add_library(pound_android SHARED
        pound_android_jni.cpp
        ${POUND_ROOT}/src/gpu/sm86/decoder.c
        ${POUND_ROOT}/src/gpu/sm86/generated/opcodes.c
)

target_include_directories(pound_android PRIVATE
        ${POUND_ROOT}/src/
)

find_library(log-lib log)

target_link_libraries(pound_android
        ${log-lib}
)
