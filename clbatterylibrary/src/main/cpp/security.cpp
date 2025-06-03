#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "SecurityUtil"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

// 使用简单的异或加密来存储字符串
const char* xorDecrypt(const char* encrypted, const char* key) {
    static char decrypted[256];
    int keyLen = strlen(key);
    int i;
    for(i = 0; encrypted[i] != '\0'; i++) {
        decrypted[i] = encrypted[i] ^ key[i % keyLen];
    }
    decrypted[i] = '\0';
    
    // 打印解密结果
    char hexResult[512] = {0};
    for(int j = 0; j < i; j++) {
        sprintf(hexResult + j*2, "%02x", (unsigned char)decrypted[j]);
    }
    LOGI("Decrypted result (hex): %s", hexResult);
    
    return decrypted;
}

// 存储原始URL字符串
const char* configUrl = "http://manager.futumos.com/prod-api/common/sysconfig";
const char* submitUrl = "http://manager.futumos.com/prod-api/common/receive";

// 存储原始AES密钥和IV（直接存储，不再加密）
const char* aesKey = "9fce8c1c76f1f23b2b5f7647467a9d9b";
const char* aesIv = "1f2a4b7bba9071d9adf88e6c9e74602f";

extern "C" {
    JNIEXPORT jstring JNICALL
    Java_com_extra_clbatterylibrary_utils_SecurityUtil_getNativeKey(JNIEnv* env, jobject) {
        return env->NewStringUTF(aesKey);
    }

    JNIEXPORT jstring JNICALL
    Java_com_extra_clbatterylibrary_utils_SecurityUtil_getNativeIv(JNIEnv* env, jobject) {
        return env->NewStringUTF(aesIv);
    }

    JNIEXPORT jstring JNICALL
    Java_com_extra_clbatterylibrary_utils_SecurityUtil_getNativeConfigUrl(JNIEnv* env, jobject) {
        return env->NewStringUTF(configUrl);
    }

    JNIEXPORT jstring JNICALL
    Java_com_extra_clbatterylibrary_utils_SecurityUtil_getNativeSubmitUrl(JNIEnv* env, jobject) {
        return env->NewStringUTF(submitUrl);
    }
} 