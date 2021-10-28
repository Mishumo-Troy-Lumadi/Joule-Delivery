package com.troy.joule.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.troy.joule.JouleApp
import com.troy.joule.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = (application as JouleApp)
        val repository = app.repository

        var stay = true

        repository.getUser().observe(this){
            if(it.isNotEmpty()) {
                app.user = it.first()
            }
            Handler(mainLooper).postDelayed({
                stay = false
            },2000)

        }

        val splashScreen  = installSplashScreen()

        splashScreen.setKeepVisibleCondition {
            stay
        }

        splashScreen.setOnExitAnimationListener{
            //Log.d(Constants.TAG, "onCreate: ${app.user}")

                if(app.user != null){
                    gotoMain()
                }else{
                    gotoAuth()
                }

        }

        setContentView(R.layout.activity_splash)
    }

    private fun gotoMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun gotoAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}