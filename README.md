# Android OPPRF-PSI Project

This android project implements the ACT-App prototype that was evaluated in the paper [Circuit-based PSI for Covid-19 Risk Scoring](https://www.computer.org/csdl/proceedings-article/ipccc/2021/09679360/1AjTpCJji6c) by Reichert, Pazelt and Scheuermann [PDF](https://eprint.iacr.org/2021/1475).
OPPRF-PSI pre-built library and header files are required to build the
app. 

The code has some remains from a NDK-boilerplate project from Android
Studio. Also the gen-libs dir is to be ignored.

## APP

MainActivity in 
`app/src/main/java/com/example/opprf_psi/MainActivity.java`
activity_main layout in 
`app/src/main/res/layout/activity_main.xml`
native-lib in 
`app/src/main/cpp/native-lib.cpp`
pre-built lib files in 
`app/src/main/libs/`

The MainActivity loads the activity_main layout, the native library and some
dependencies. OnClickListeners are created that run the library code in a
thread. JNI native methods are declared to set parameters, execute the protocol,
get logs and retrieve outputs.

The activity_main layout consists of nested linear layouts with scrollable
input fields for all necessary protocol parameters. One text input field can be
used to encode all parameters in just one input string.
The log output is a scrollable text view with a run button below.

The native-lib.cpp file is the equivalent of the `psi_analytics_example.cpp` to
execute the protocol with a context of parameters. It implements all the native
methods that can be called from the MainActivity via JNI. It imports the opprf-psi
library and all its dependencies. The gradle build uses cmake and the
`CMakeLists.txt` file to build the native-lib with Android NDK. Here all
necessary build variables are set, all dependencies are linked as libraries. The
libraries have to be pre-built in the `app/scr/main/libs/` sub-directories.
Those libraries are built in the
[opprfpsi](https://github.com/Mazelt/OPPRF-PSI)
 repository.

Inside the lib directories, all pre-build libs have to be placed in a
sub-directory named as the arm64-v8 abi (At least for the One Plus 5 as target).
Header files are placed into the include dir. 

AndroidManifest.xml: important user permissions

* android.permission.INTERNET
* android.permission.ACCESS_NETWORK_STATE


## Build
To build make sure the OPPRF-PSI library files are placed in app/src/main/libs
in the correct arm-abi directory.
Install the Android NDK tools in Android Studio and make sure cmake is available
to Android Studio.
Configure your SDK version, abiFilters in app/build.gradle
Run the gralde compilation (preferably with the Android Studio IDE).

A pre-built apk is committed as `app/build/outputs/apk/debug/app-debug.apk`.


## Run
Open the App, configure all parameters to match the ones the server binary is
started with. Configure the IP of the server.
Press Run. The app will print out all logs and display the result in the Box at
the top (if the functionality outputs them.)
