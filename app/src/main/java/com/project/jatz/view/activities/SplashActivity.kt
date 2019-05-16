package com.project.jatz.view.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
                sleep(2000)
            }catch (e: InterruptedException){
                e.printStackTrace()
            }finally {

                if (currentUser != null && isNetworkAvailable()) {
                    launchBoards()

                } else {
                    launchLogin()
                }
            }
        }

        waitThread.start()
    }

    fun launchBoards(){
        val boardsActivityLaunch = Intent(this, BoardsActivity::class.java)
        startActivity(boardsActivityLaunch)
        finish()
    }

    fun launchLogin(){
        val loginActivityLaunch = Intent(this, LoginActivity::class.java)
        startActivity(loginActivityLaunch)
        finish()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

}
