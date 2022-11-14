#include <jni.h>
#include <string>

#include <android/log.h>

#define LOG_TAG "print_logs"
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__))
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__))
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__))
#define LOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__))


using namespace std;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_attrsense_android_ui_main_MainActivity_stringFromJNI(JNIEnv *env, jobject jobj) {
    string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

//extern "C"
//JNIEXPORT void JNICALL
//Java_com_attrsense_android_ui_main_MainActivity_test(JNIEnv *env, jobject thiz) {
//    jclass clz = env->GetObjectClass(thiz);
//    //修改变量值
//    jfieldID fieldId = env->GetFieldID(clz, "property", "I");
//    //获取值
//    jint oldValue = env->GetIntField(thiz, fieldId);
//    LOGI("打印旧值：%d", oldValue);
//    //设置新值
//    env->SetIntField(thiz, fieldId, 100);
//
//
//    //调用Java的方法
//    jmethodID methodId = env->GetMethodID(clz, "myFun", "(I)I");
//    //赋值：方式一
////    env->CallIntMethod(thiz, methodId, 999L);
//    //赋值：方式二
//    auto *params = new jvalue[1];
//    params[0].i = 999;
//    env->CallIntMethodA(thiz, methodId, params);
//    delete[] params;
//
//
//
//    //创建java对象，第一步：AllocObject；第二步：CallNonvirtualVoidMethod
//    jclass clz_date = env->FindClass("java/util/Date");
//    jobject obj_date = env->AllocObject(clz_date);
//
//
//    jmethodID method_date = env->GetMethodID(clz_date, "<init>", "()V");
//    env->CallNonvirtualVoidMethod(obj_date, clz_date, method_date);
//
//    jmethodID method_time = env->GetMethodID(clz_date, "getTime", "()J");
//    jlong value = env->CallLongMethod(obj_date, method_time);
//    LOGI("打印时间：%lld", value);
//
//
//    //字符串操作
//    jfieldID strFieldId = env->GetFieldID(clz, "strLength", "Ljava/lang/String;");
//    //获取字符串长度
//    auto strValue = static_cast<jstring>(env->GetObjectField(thiz, strFieldId));
//    jsize strSize = env->GetStringUTFLength(strValue); //utf-8
//}