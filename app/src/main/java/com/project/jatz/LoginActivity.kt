package com.project.jatz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.parse.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var loginbutton =  findViewById<Button>(R.id.loginButton)
        var signupText = findViewById<TextView>(R.id.signupText)
        var useremail = findViewById<TextInputEditText>(R.id.emailText)
        var userpassword = findViewById<TextInputEditText>(R.id.passwordText)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("WpK0tvBCi90tvdNFO2t5QF0gepQOj7jNLGHmNFyY")
                .clientKey("sOZdZqL8cnZaJ4bb3bhE9RmnOSU4VQ3tkOrlRhMC")
                .server("https://parseapi.back4app.com/")
                .build()
        )

        ParseInstallation.getCurrentInstallation().saveInBackground()

        loginbutton.setOnClickListener{



            ParseUser.logInInBackground("Jordi", userpassword.text.toString(), LogInCallback { user, e ->

                if (e == null) {
                    Toast.makeText(this,"SI", Toast.LENGTH_SHORT).show()
                } else {
                    ParseUser.logOut()
                    Toast.makeText(this,"NO", Toast.LENGTH_LONG).show()
                }

            })

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

            }


        signupText.setOnClickListener{

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }


    }
}
