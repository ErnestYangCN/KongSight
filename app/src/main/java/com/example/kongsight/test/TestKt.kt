@file:JvmName("TestKt")  // 放在文件最开头，import之前
package com.example.kongsight.test

import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kongsight.R

fun updateTextAfterDelay(activity: AppCompatActivity) {
    val textView = activity.findViewById<TextView>(R.id.hello_text)
    Handler(Looper.getMainLooper()).postDelayed({
        textView.text = "Kotlin is ready"
    }, 3000)
}