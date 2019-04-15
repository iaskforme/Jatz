package com.project.jatz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.LoginFilter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.parse.*
import java.util.*


class SignUpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        var loginText = findViewById<TextView>(R.id.loginText)
        var signupButton = findViewById<Button>(R.id.signupButton)
        var nameUser = findViewById<TextInputEditText>(R.id.nameText)
        var emailUser = findViewById<TextInputEditText>(R.id.emailText)
        var passUser = findViewById<TextInputEditText>(R.id.repeatpassText)


        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("WpK0tvBCi90tvdNFO2t5QF0gepQOj7jNLGHmNFyY")
                .clientKey("sOZdZqL8cnZaJ4bb3bhE9RmnOSU4VQ3tkOrlRhMC")
                .server("https://parseapi.back4app.com/")
                .build()
        )

        ParseInstallation.getCurrentInstallation().saveInBackground()


        signupButton.setOnClickListener{

            val user = ParseUser()
            user.username = nameUser.text.toString()
            user.setPassword(passUser.text.toString())
            user.email = emailUser.text.toString()

            user.signUpInBackground(SignUpCallback {
                if(it == null){
                    Toast.makeText(this, "User ${user.username} created", Toast.LENGTH_SHORT).show()
                }else{
                    ParseUser.logOut()
                    Toast.makeText(this, "User ${user.username} wasn't created", Toast.LENGTH_SHORT).show()
                }
            })

        }

        loginText.setOnClickListener{

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }

}