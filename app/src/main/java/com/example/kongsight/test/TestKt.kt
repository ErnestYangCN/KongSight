@file:JvmName("TestKt")
package com.example.kongsight.test

import android.os.Handler
import android.os.Looper

// 这是一个测试延迟打印的函数，不依赖任何布局控件
fun updateTextAfterDelay() {
    Handler(Looper.getMainLooper()).postDelayed({
        println("Kotlin is ready")  // 延迟3秒后打印信息到Logcat
    }, 3000)
}
