#include <jni.h>
#include "org_whut_mc_server_core_util_ResolveUtil.h"
#include "frame_type.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

static jstring stoJstring(JNIEnv* env, const char* pat)
{
    jclass strClass = (*env)->FindClass(env, "Ljava/lang/String;");
    jmethodID ctorID = (*env)->GetMethodID(env, strClass, "<init>", "([BLjava/lang/String;)V");
    jbyteArray bytes = (*env)->NewByteArray(env, strlen(pat));
    (*env)->SetByteArrayRegion(env, bytes, 0, strlen(pat), (jbyte*)pat);
    jstring encoding = (*env)->NewStringUTF(env, "utf-8");
    return (jstring)(*env)->NewObject(env, strClass, ctorID, bytes, encoding);
}

static char* jstringTostring(JNIEnv* env, jstring jstr)
{
    char* rtn = NULL;
    jclass clsstring = (*env)->FindClass(env, "java/lang/String");
    jstring strencode = (*env)->NewStringUTF(env, "utf-8");
    jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr= (jbyteArray)(*env)->CallObjectMethod(env, jstr, mid, strencode);
    jsize alen = (*env)->GetArrayLength(env, barr);
    jbyte* ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
    if (alen > 0)
    {
        rtn = (char*)malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    (*env)->ReleaseByteArrayElements(env, barr, ba, 0);
    return rtn;
}

JNIEXPORT jstring JNICALL Java_org_whut_mc_server_core_util_ResolveUtil_resolveLanyanOpenBB
  (JNIEnv *env, jobject obj, jbyteArray arr)
{
    int size = (*env)->GetArrayLength(env, arr);
    byte* p = (*env)->GetByteArrayElements(env, arr, JNI_FALSE);
    p = (byte*) realloc(p, sizeof(byte) * size);
    _lanyan_open_bb* lob = resolve_lanyan_open_bb_fn(p);
    char* json = create_lanyan_open_bb_json_fn(lob);
    return stoJstring(env, json);
}