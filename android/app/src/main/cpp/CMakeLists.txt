cmake_minimum_required(VERSION 3.22.1)
project(pound_android)

set(POUND_STUB_LEVEL "1" CACHE STRING "Stub isolation level for Android JNI shim")

add_library(pound_android SHARED pound_android_jni.cpp)

find_library(log-lib log)

target_compile_definitions(pound_android PRIVATE
    $<$<BOOL:${ANDROID}>:POUND_ANDROID=1>
    POUND_STUB_LEVEL=${POUND_STUB_LEVEL}
)

target_link_libraries(pound_android ${log-lib})
