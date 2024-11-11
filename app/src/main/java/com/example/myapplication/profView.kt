package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager


import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.saveable.Saver


class profView  : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profview) // Set the layout for this activity
        enableEdgeToEdge()
    }
    private fun enableEdgeToEdge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = window.insetsController
            windowInsetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
                it.hide(WindowInsets.Type.navigationBars())
            }
        } else {
            // For devices with lower Android versions
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }
}