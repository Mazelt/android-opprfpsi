#include <jni.h>
#include <string>
#include <cmath>
#include <ENCRYPTO_utils/crypto/crypto.h>
//#include <ENCRYPTO_utils/connection.h>
//#include <ENCRYPTO_utils/socket.h>

#include "abycore/aby/abyparty.h"
#include "common/psi_analytics.h"
#include "common/psi_analytics_context.h"

#include <android/log.h>
#include <pthread.h>
#include <unistd.h>

static int pfd[2];
static pthread_t loggingThread;
static const char *LOG_TAG = "OPPRF COUT CERR";

static void *loggingFunction(void*) {
    ssize_t readSize;
    char buf[128];

    while((readSize = read(pfd[0], buf, sizeof buf - 1)) > 0) {
        if(buf[readSize - 1] == '\n') {
            --readSize;
        }

        buf[readSize] = 0;  // add null-terminator

        __android_log_write(ANDROID_LOG_DEBUG, LOG_TAG, buf); // Set any log level you want
    }

    return 0;
}

static int runLoggingThread() { // run this function to redirect your output to android log
    setvbuf(stdout, 0, _IOLBF, 0); // make stdout line-buffered
    setvbuf(stderr, 0, _IONBF, 0); // make stderr unbuffered

    /* create the pipe and redirect stdout and stderr */
    pipe(pfd);
    dup2(pfd[1], 1);
    dup2(pfd[1], 2);

    /* spawn the logging thread */
    if(pthread_create(&loggingThread, 0, loggingFunction, 0) == -1) {
        return -1;
    }

    pthread_detach(loggingThread);

    return 0;
}

auto get_context() {
    ENCRYPTO::PsiAnalyticsContext context;
    context.role= CLIENT; // CLIENT
    context.neles=1000u;
    context.bitlen=61u;
    context.epsilon=2.4f;
    context.address="192.168.178.66";
    context.port=7777;
    context.nthreads=1;
    context.notherpartyselems=0u;
    context.threshold=0u;
    context.nmegabins=1u;
    context.polynomialsize=0u;
    context.nfuns=2u;
    // type checks, we choose NONE for now.
    context.analytics_type = ENCRYPTO::PsiAnalyticsContext::NONE;
    //
    if (context.notherpartyselems == 0) {
        context.notherpartyselems = context.neles;
    }
    if (context.polynomialsize == 0) {
        context.polynomialsize = context.neles * context.nfuns;
    }
    context.polynomialbytelength = context.polynomialsize * sizeof(std::uint64_t);
    // this can be removed later as we only handle clients here.
    const std::size_t client_neles =
            context.role == CLIENT ? context.neles : context.notherpartyselems;
    context.nbins = client_neles * context.epsilon;
    return context;
}

int run_opprf(ENCRYPTO::PsiAnalyticsContext &context){
//    std::unique_ptr<CSocket> sock = Connect(context.address.c_str(), context.port);
//    if (sock == NULL) {
//        return 1;
//    } else {
//        return 0;
//    }
    auto gen_bitlen = static_cast<std::size_t>(std::ceil(std::log2(context.neles))) + 3;
    auto inputs = ENCRYPTO::GeneratePseudoRandomElements(context.neles, gen_bitlen);
    ENCRYPTO::run_psi_analytics(inputs, context);
    return EXIT_SUCCESS;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_opprf_1psi_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    runLoggingThread();
    auto context = get_context();
    int out = run_opprf(context);
    std::string hello = "Hello from C++ " + context.address + std::to_string(out);
    return env->NewStringUTF(hello.c_str());
}

