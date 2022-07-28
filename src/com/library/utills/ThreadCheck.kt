package com.library.utills

fun checkNotAMainThread() {
    check(!Thread.currentThread().name.contains("main")) {
        "Main Thread detected"
    }
}
