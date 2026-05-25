#include <jni.h>
#include <android/log.h>
#include <thread>
#include <atomic>
#include <string>

#define LOG_TAG "PoundJNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

namespace pound {
namespace desktop_stub {
int unsupported_x86_affinity() { return 0; }
int unsupported_macos_surface() { return 0; }
int unsupported_linux_epoll_shim() { return 0; }
}

class AndroidCore {
public:
    bool init(const std::string& profile) {
        profile_ = profile;
        initialized_.store(true);
        LOGI("Core initialized with profile: %s", profile.c_str());
        return true;
    }

    bool startWorker(const std::string& gamePath) {
        if (!initialized_.load() || workerRunning_.load()) {
            return false;
        }
        workerRunning_.store(true);
        worker_ = std::thread([this, gamePath] {
            LOGI("Starting worker for path: %s", gamePath.c_str());
            desktop_stub::unsupported_x86_affinity();
            desktop_stub::unsupported_macos_surface();
            desktop_stub::unsupported_linux_epoll_shim();
            std::this_thread::sleep_for(std::chrono::milliseconds(10));
            workerRunning_.store(false);
            LOGI("Worker finished");
        });
        worker_.detach();
        return true;
    }

private:
    std::atomic<bool> initialized_{false};
    std::atomic<bool> workerRunning_{false};
    std::thread worker_;
    std::string profile_;
};

AndroidCore g_core;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_pound_emulator_NativeCore_initCore(JNIEnv* env, jobject, jstring profileJson) {
    const char* raw = env->GetStringUTFChars(profileJson, nullptr);
    std::string profile = raw ? raw : "{}";
    if (raw) env->ReleaseStringUTFChars(profileJson, raw);
    return pound::g_core.init(profile) ? JNI_TRUE : JNI_FALSE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_pound_emulator_NativeCore_startWorker(JNIEnv* env, jobject, jstring gamePath) {
    const char* raw = env->GetStringUTFChars(gamePath, nullptr);
    std::string path = raw ? raw : "";
    if (raw) env->ReleaseStringUTFChars(gamePath, raw);
    return pound::g_core.startWorker(path) ? JNI_TRUE : JNI_FALSE;
}
