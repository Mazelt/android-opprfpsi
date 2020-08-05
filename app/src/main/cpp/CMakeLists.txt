# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)
set(lib_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../libs/arm64-v8a)
set(include_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../libs/include)
include_directories(${include_DIR}
        #        ${repo_ROOT}/extern
        ${lib_DIR}/cryptoTools
        ${lib_DIR}/libOTe
        #        ${repo_ROOT}/extern/ABY/src
        ${lib_DIR}/ENCRYPTO_utils/src
        #        ${repo_ROOT}/extern/HashingTables
        ${lib_DIR}/abycore/..
        ${lib_DIR}/ot
        #        ${repo_ROOT}/extern/HashingTables/extern/fmt/include
        #        ${repo_ROOT}/extern/libOTe/libOTe/cm..
        #        ${repo_ROOT}/extern/libOTe/cryptoTools/cryptoTools/..
        #        ${build_DIR}/include
        #        ${ANDROID_NDK}/ntl/include
        #        ${ANDROID_NDK}/gmp/include
        #        ${ANDROID_NDK}/boost/include
        #        ${ANDROID_NDK}/openssl/include
        ${lib_DIR}/relic
        ${lib_DIR}/relic/low
        )

add_library(libpsi_analytics_eurocrypt19 STATIC IMPORTED)
set_target_properties(libpsi_analytics_eurocrypt19 PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libpsi_analytics_eurocrypt19.a)
add_library(libaby STATIC IMPORTED)
set_target_properties(libaby PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libaby.a)
add_library(libotextension STATIC IMPORTED)
set_target_properties(libotextension PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libotextension.a)
add_library(libencrypto_utils STATIC IMPORTED)
set_target_properties(libencrypto_utils PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libencrypto_utils.a)


add_library(libgmp SHARED IMPORTED)
set_target_properties(libgmp PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libgmp.so)
add_library(libgmpxx SHARED IMPORTED)
set_target_properties(libgmpxx PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libgmpxx.so)


add_library(libboost_filesystem SHARED IMPORTED)
set_target_properties(libboost_filesystem PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libboost_filesystem.so)
add_library(libboost_system SHARED IMPORTED)
set_target_properties(libboost_system PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libboost_system.so)
add_library(libboost_thread SHARED IMPORTED)
set_target_properties(libboost_thread PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libboost_thread.so)
add_library(libboost_chrono SHARED IMPORTED)
set_target_properties(libboost_chrono PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libboost_chrono.so)
add_library(libboost_date_time SHARED IMPORTED)
set_target_properties(libboost_date_time PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libboost_date_time.so)
add_library(libboost_atomic SHARED IMPORTED)
set_target_properties(libboost_atomic PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libboost_atomic.so)

add_library(libcrypto SHARED IMPORTED)
set_target_properties(libcrypto PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libcrypto.so)
add_library(libssl SHARED IMPORTED)
set_target_properties(libssl PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libssl.so)

add_library(librelic_s STATIC IMPORTED)
set_target_properties(librelic_s PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/librelic_s.a)

add_library(libntl STATIC IMPORTED)
set_target_properties(libntl PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libntl.a)

add_library(libHashingTables STATIC IMPORTED)
set_target_properties(libHashingTables PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libHashingTables.a)

add_library(liblibOTe STATIC IMPORTED)
set_target_properties(liblibOTe PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/liblibOTe.a)


add_library(libcryptoTools STATIC IMPORTED)
set_target_properties(libcryptoTools PROPERTIES IMPORTED_LOCATION
        ${lib_DIR}/libcryptoTools.a)



add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        native-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        libpsi_analytics_eurocrypt19
        libaby
        libotextension
        libgmp
        libboost_filesystem
        libencrypto_utils
        libboost_system
        libboost_thread
        libboost_chrono
        libboost_date_time
        libboost_atomic
        libgmp
        libgmpxx
        libcrypto
        librelic_s
        libgmp
        libgmpxx
        libntl
        libHashingTables
        libssl
        libcrypto
        liblibOTe
        libcryptoTools
        librelic_s
        libboost_system
        libboost_filesystem
        librelic_s
        )