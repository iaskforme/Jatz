package com.project.jatz.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.parse.ParseUser
import com.project.jatz.R
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.Thread.sleep

/**
 * First Activity to show. Tests if theres a user logged in and launch one activity thw two activites
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splash_logo_image.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_transition))

        val currentUser = ParseUser.getCurrentUser()

        var waitThread = Thread(){
            try {
                sleep(3000)
            }catch (e: InterruptedException){
                e.printStackTrace()
            }finally {

                if (currentUser != null) {
                    val mainActivityLaunch = Intent(this, MainActivity::class.java)
                    startActivity(mainActivityLaunch)
                    finish()

                } else {
                    val loginActivityLaunch = Intent(this, LoginActivity::class.java)
                    startActivity(loginActivityLaunch)
                    finish()
                }
            }
        }

        waitThread.start()
    }
}
