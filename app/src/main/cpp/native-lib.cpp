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
#include <common/psi_analytics_context.h>

static int pfd[2];
static pthread_t loggingThread;
static const char *LOG_TAG = "OPPRF COUT CERR";
// later create a class object to better manage psi runs
ENCRYPTO::PsiAnalyticsContext context;

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

static int runLoggingThread( JNIEnv* env,
                             jobject /* this */) { // run this function to redirect your output to android log
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

void setContext(JNIEnv* env,
                        jobject /* this */,
                        jint neles,
                        jint oneles,
                        jint bitlen,
                        jfloat epsilon,
                        jstring ipaddr,
                        jint port,
                        jint nthreads,
                        jint threshold,
                        jint megabins,
                        jint polys,
                        jint nfuns,
                        jint psi_type){
    context.role= CLIENT; // CLIENT
    context.neles=(unsigned int)neles;
    context.bitlen=(unsigned int)bitlen;
    context.epsilon=(float)epsilon;
    const char * address = env->GetStringUTFChars(ipaddr, 0);
    context.address=address;
    env->ReleaseStringUTFChars(ipaddr,address);
    context.port=(int)port;
    context.nthreads=(int)nthreads;
    context.notherpartyselems=(unsigned int)oneles;
    context.threshold=(unsigned int)threshold;
    context.nmegabins=(unsigned int)megabins;
    context.polynomialsize=(unsigned int)polys;
    context.nfuns=(unsigned int)nfuns;
    // type checks, we choose NONE for now.
    if ((int)psi_type == 0) {
        context.analytics_type = ENCRYPTO::PsiAnalyticsContext::NONE;
    } else if ((int)psi_type == 1) {
        context.analytics_type = ENCRYPTO::PsiAnalyticsContext::THRESHOLD;
    } else if ((int)psi_type == 2) {
        context.analytics_type = ENCRYPTO::PsiAnalyticsContext::SUM;
    } else if ((int)psi_type == 3) {
        context.analytics_type = ENCRYPTO::PsiAnalyticsContext::SUM_IF_GT_THRESHOLD;
    } else {
        std::string error_msg(std::string("Unknown function type: " + std::to_string((int)psi_type)));
        throw std::runtime_error(error_msg.c_str());
    }
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
    std::cout << "Saved context with " +
        std::to_string(context.neles) + " " +
        std::to_string(context.notherpartyselems) + " " +
        std::to_string(context.bitlen) + " " +
        std::to_string(context.epsilon) + " " +
        context.address + " " +
        std::to_string(context.port) + " " +
        std::to_string(context.nthreads) + " " +
        std::to_string(context.threshold) + " " +
        std::to_string(context.nmegabins) + " " +
        std::to_string(context.polynomialsize) + " " +
        std::to_string(context.analytics_type) << std::endl;
}

int run_opprf(ENCRYPTO::PsiAnalyticsContext &context){
    auto gen_bitlen = static_cast<std::size_t>(std::ceil(std::log2(context.neles))) + 3;
    auto inputs = ENCRYPTO::GeneratePseudoRandomElements(context.neles, gen_bitlen);
    auto output = ENCRYPTO::run_psi_analytics(inputs, context);
    return output;
}

double get_some_context(JNIEnv* env,
                     jobject /* this */) {
    return context.timings.aby_online;
}

extern "C" JNIEXPORT jstring JNICALL
nativeRun(
        JNIEnv* env,
        jobject /* this */) {
    int out = run_opprf(context);
    PrintTimings(context);
    std::string hello = "Hello from C++ " + context.address + std::to_string(out);
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    // Find your class. JNI_OnLoad is called from the correct class loader context for this to work.
    jclass c = env->FindClass("com/example/opprf_psi/MainActivity");
    if (c == nullptr) return JNI_ERR;

    // Register your class' native methods.
    static const JNINativeMethod methods[] = {
            {"nativeRun", "()Ljava/lang/String;", reinterpret_cast<void*>(nativeRun)},
            {"nativeLogging", "()I", reinterpret_cast<void*>(runLoggingThread)},
            {"nativeSetContext", "(IIIFLjava/lang/String;IIIIIII)V", reinterpret_cast<void*>(setContext)},
            {"nativeGetSomeContext", "()D", reinterpret_cast<void*>(get_some_context)}
    };
    int rc = env->RegisterNatives(c, methods, sizeof(methods)/sizeof(JNINativeMethod));
    if (rc != JNI_OK) return rc;

    return JNI_VERSION_1_6;
}

