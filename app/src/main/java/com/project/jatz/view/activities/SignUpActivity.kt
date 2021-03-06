package com.project.jatz.view.activities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.*
import com.project.jatz.R
import com.project.jatz.database.App
import com.project.jatz.utils.ConnectionReceiver
import kotlinx.android.synthetic.main.activity_signup.*

/**
 * This class contains the activity used for singup by new users or creating new accounts
 */
class SignUpActivity: AppCompatActivity(), ConnectionReceiver.ConnectionReceiverListener {

    var connectionReceiver = ConnectionReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        baseContext.registerReceiver(connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        App.instance.setConnectionListener(this)

        /**
         * Listener for the "Create Account" button
         */
        signup_button.setOnClickListener{

            if (isNetworkAvailable()){

                val user = ParseUser()
                user.username = signup_nick_edittext.text.toString()
                user.email = signup_email_edittext.text.toString()
                user.setPassword(signup_password_edittext.text.toString())
                user.put("name", signup_name_edittext.text.toString())
                user.put("surname", signup_surname_edittext.text.toString())

                signingUp(user)

            }else{
                Toast.makeText(this,"No connection available!", Toast.LENGTH_LONG).show()
            }


        }

        /**
         * Listener for the "You have an account? Login" text
         */
        signup_login_text.setOnClickListener{

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
        }
    }

    override fun onPause() {
        super.onPause()
        // Unregisters reciver when another activity its prompted
        unregisterReceiver(connectionReceiver)

        baseContext.registerReceiver(connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        App.instance.setConnectionListener(this)
    }

    /**
     * Checking and handling network connectivity through a BroadcastReceiver
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if(!isConnected){
            signup_button.isEnabled = false
            signup_button.text = "NO CONNECTION"
        }else{
            signup_button.text = "CREATE ACCOUNT"
            signup_button.isEnabled = true
        }
    }

    /**
     * Function that contains the connection with the database as a user with the singUpInBackground method once ParseUser its created
     *
     * @param ParseUser instance for singUpInBackground run
     *
     */
    private fun signingUp(user: ParseUser){

        if (!validate()) {
            return
        }

        signup_proggress_bar.visibility = View.VISIBLE

        user.signUpInBackground(SignUpCallback {
            if (it == null) {
                signup_proggress_bar.visibility = View.INVISIBLE
                Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
                startActivity(intent)
                finish()


            } else {
                signup_proggress_bar.visibility = View.INVISIBLE
                ParseUser.logOut()
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show();
            }
        })


     }


    /**
     * Fields validation got from signingup and pressing the "Create Account" button
     *
     * @return Boolean if every field pass the validation or not
     *
     */
    private fun validate(): Boolean{
        var valid = true

        val name = signup_name_edittext.text.toString()
        val surname = signup_surname_edittext.text.toString()
        val username = signup_nick_edittext.text.toString()
        val email = signup_email_edittext.text.toString()
        val password = signup_password_edittext.text.toString()
        val repassword = signup_repeat_password_edittext.text.toString()

        if (name.isEmpty() || name.length < 3) {
            signup_name_edittext.setError("At least 5 characters")
            valid = false
        } else {
            signup_name_edittext.setError(null)
        }

        if (surname.isEmpty() || name.length < 3) {
            signup_surname_edittext.setError("At least 5 characters")
            valid = false
        } else {
            signup_name_edittext.setError(null)
        }

        if (username.isEmpty() || name.length < 3) {
            signup_nick_edittext.setError("At least 3 characters")
            valid = false
        } else {
            signup_nick_edittext.setError(null)
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signup_email_edittext!!.setError("Enter a valid email address")
            valid = false
        } else {
            signup_email_edittext!!.setError(null)
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            signup_password_edittext!!.setError("Between 4 and 10 alphanumeric characters")
            valid = false
        } else {
            signup_password_edittext!!.setError(null)
        }

        if (repassword != password) {
            signup_repeat_password_edittext!!.setError("Password doesn't match")
            valid = false
        } else {
            signup_repeat_password_edittext!!.setError(null)
        }

        return valid
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

}