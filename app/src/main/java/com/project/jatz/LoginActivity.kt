package com.project.jatz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.parse.*

class LoginActivity : AppCompatActivity() {

    var loginbutton: Button? =  null
    var signupText: TextView? = null
    var useremail: TextInputEditText? = null
    var userpassword: TextInputEditText? = null
    var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginbutton =  findViewById<Button>(R.id.loginButton)
        signupText = findViewById<TextView>(R.id.signupText)
        useremail = findViewById<TextInputEditText>(R.id.emailText)
        userpassword = findViewById<TextInputEditText>(R.id.passwordText)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("WpK0tvBCi90tvdNFO2t5QF0gepQOj7jNLGHmNFyY")
                .clientKey("sOZdZqL8cnZaJ4bb3bhE9RmnOSU4VQ3tkOrlRhMC")
                .server("https://parseapi.back4app.com/")
                .build()
        )

        ParseInstallation.getCurrentInstallation().saveInBackground()

        /**
         * Listener for login button that executes loginUser function
         */
        loginbutton!!.setOnClickListener{

            loginUser()

            }

        /**
         * Listener for singup text that starts SignUpActivity
         */
        signupText!!.setOnClickListener{

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }

    /**
     *
     * Function that contains the connection with the database and logs in to access the main activity.
     * This connection its made getting the user's email and verifying in it with the one get it in database
     *
     */
    fun loginUser(){

        if (!validate()) {
            onLoginFailed()
            return
        }

        var userQuery: ParseQuery<ParseUser> = ParseUser.getQuery().whereEqualTo("email",useremail!!.text.toString())

        userQuery.findInBackground(FindCallback { users, e ->
            if (e == null){

                ParseUser.logInInBackground(users.get(0).username, userpassword!!.text.toString(), LogInCallback { user, a ->

                    if(user != null){

                        Toast.makeText(this, "Welcome ${user.username}!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        ParseUser.logOut();
                        Toast.makeText(this, a.message, Toast.LENGTH_LONG).show();
                    }

                })

            }else{
                Log.d("FAIL", e.message)
            }

        })

    }

    /**
     * Shows a toast in case the validation fails
     */
    fun onLoginFailed() {
        Toast.makeText(baseContext, "Login failed", Toast.LENGTH_LONG).show()

        loginbutton!!.setEnabled(true)
    }

    /**
     * Fields validation got from login and pressing "login" button
     *
     * @return Boolean if every field pass the validation or not
     */
    fun validate(): Boolean {
        var valid = true

        val email = useremail!!.getText().toString()
        val password = userpassword!!.getText().toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            useremail!!.setError("Enter a valid email address")
            valid = false
        } else {
            useremail!!.setError(null)
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            userpassword!!.setError("Between 4 and 10 alphanumeric characters")
            valid = false
        } else {
            userpassword!!.setError(null)
        }

        return valid
    }
}
