package com.example.myapplication // Update with your actual package name

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view for the splash screen
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, AdminViewStudentOne::class.java)) // Update MainActivity with your main activity name
            finish()
        }, 3000)
        // Set a delay before starting the main activity
        Handler(Looper.getMainLooper()).postDelayed({
            // Start the main activity
            startActivity(Intent(this, MainActivity::class.java)) // Update MainActivity with your main activity name
            finish() // Finish the splash activity
        }, 3000) // 3000 milliseconds delay (3 seconds)
    }
}
