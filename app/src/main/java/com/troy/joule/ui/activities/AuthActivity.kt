package com.troy.joule.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.window.SplashScreenView
import com.troy.joule.R

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}