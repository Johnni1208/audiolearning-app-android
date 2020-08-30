package com.audiolearning.app.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.audiolearning.app.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        requestNecessaryPermissions()
    }

    private fun requestNecessaryPermissions() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO),
                resources.getInteger(R.integer.request_code_permissions)
            )

            return
        }

        Handler().postDelayed({
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            overridePendingTransition(0, R.anim.fragment_fade_exit)
            finish()
        }, resources.getInteger(R.integer.splash_screen_time).toLong())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.all { x -> x == PackageManager.PERMISSION_GRANTED }) {
            Handler().postDelayed({
                val mainActivityIntent = Intent(this, MainActivity::class.java)
                startActivity(mainActivityIntent)
            }, 3000)
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
