package com.project.jatz
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.parse.*
import android.widget.ProgressBar


class SignUpActivity: AppCompatActivity() {

    var loginText: TextView? = null
    var signupButton: Button? = null
    var nameUser: TextInputEditText? = null
    var emailUser: TextInputEditText? = null
    var passUser: TextInputEditText? = null
    var repassUser: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        loginText = findViewById<TextView>(R.id.loginText)
        signupButton = findViewById<Button>(R.id.signupButton)
        nameUser = findViewById<TextInputEditText>(R.id.nameText)
        emailUser = findViewById<TextInputEditText>(R.id.emailText)
        passUser = findViewById<TextInputEditText>(R.id.passText)
        repassUser = findViewById<TextInputEditText>(R.id.repeatpassText)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("WpK0tvBCi90tvdNFO2t5QF0gepQOj7jNLGHmNFyY")
                .clientKey("sOZdZqL8cnZaJ4bb3bhE9RmnOSU4VQ3tkOrlRhMC")
                .server("https://parseapi.back4app.com/")
                .build()
        )

        ParseInstallation.getCurrentInstallation().saveInBackground()

        /**
         * Listener for the "Create Account" button
         */
        signupButton!!.setOnClickListener{

            val user = ParseUser()
            user.username = nameUser!!.text.toString()
            user.setPassword(passUser!!.text.toString())
            user.email = emailUser!!.text.toString()

            signingUp(user)

        }

        /**
         * Listener for the "You have an account? Login" text
         */
        loginText!!.setOnClickListener{

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }

    /**
     * Function that contains the connection with the database as a user with the singUpInBackground method once ParseUser its created
     *
     * @param ParseUser instance for singUpInBackground run
     *
     */
    fun signingUp(user: ParseUser){

        if (!validate()) {
            faieldSigningUp()
            return
        }

        var progressBar = findViewById<ProgressBar>(R.id.progressBar)

        progressBar.visibility = View.VISIBLE

        user.signUpInBackground(SignUpCallback {
            if (it == null) {
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(this, "User ${user.username} created", Toast.LENGTH_SHORT).show()
            } else {
                progressBar.visibility = View.INVISIBLE
                ParseUser.logOut()
                Toast.makeText(this, "Something went wrong... \nUser ${user.username} wasn't created", Toast.LENGTH_SHORT).show()
            }
        })


     }

    /**
     * Returns a toast if the signup fails
     */
    fun faieldSigningUp(){
        Toast.makeText(this, "Fail to signup!", Toast.LENGTH_SHORT)
    }

    /**
     * Fields validation got from signingup and pressing the "Create Account" button
     *
     * @return Boolean if every field pass the validation or not
     *
     */
    fun validate(): Boolean{
        var valid = true

        val name = nameUser!!.getText().toString()
        val email = emailUser!!.getText().toString()
        val password = passUser!!.getText().toString()
        val repassword = repassUser!!.getText().toString()

        if (name.isEmpty() || name.length < 3) {
            nameUser!!.setError("At least 3 characters")
            valid = false
        } else {

        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailUser!!.setError("Enter a valid email address")
            valid = false
        } else {
            emailUser!!.setError(null)
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            passUser!!.setError("Between 4 and 10 alphanumeric characters")
            valid = false
        } else {
            passUser!!.setError(null)
        }

        if (repassword.isEmpty() || repassword.length < 4 || repassword.length > 10 || repassword != password) {
            repassUser!!.setError("Password doesn't match")
            valid = false
        } else {
            repassUser!!.setError(null)
        }

        return valid
    }

}