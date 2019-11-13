package com.example.cache

import android.content.Intent
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        super.onAttachedToWindow()
        setContentView(R.layout.activity_splash)

        // Start main activity after 7.5 second delay
        Handler().postDelayed({
            this.startActivity(Intent(this, MainActivity::class.java)
                .apply { action = Intent.ACTION_VIEW })
        }, 7500)
    }
}
