package com.project.jatz.view.activities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.*
import com.project.jatz.R
import com.project.jatz.database.App
import com.project.jatz.utils.ConnectionReceiver
import kotlinx.android.synthetic.main.activity_login.*

/**
 * This class contains the activity used for loginin by users already created
 */
class LoginActivity : AppCompatActivity(), ConnectionReceiver.ConnectionReceiverListener  {

    var connectionReceiver = ConnectionReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        baseContext.registerReceiver(connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        App.instance.setConnectionListener(this)

        /**
         * Listener for login button that executes loginUser function
         */
        login_button.setOnClickListener{
            if (isNetworkAvailable()){
                loginUser()
            }else{
                Toast.makeText(this,"No connection available!", Toast.LENGTH_LONG).show()
            }
        }

        /**
         * Listener for singup text that starts SignUpActivity
         */
        login_signup_text.setOnClickListener{

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
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
              login_button.isEnabled = false
              login_button.text = "NO CONNECTION"
        }else{
            login_button.text = "LOGIN"
            login_button.isEnabled = true
        }
    }

    /**
     *
     * Function that contains the connection with the database and logs in to access the main activity.
     * This connection its made getting the user's email and verifying in it with the one get it in database
     *
     */
    private fun loginUser(){

        if (!validate()) {
            return
        }

        var userEmailQuery: ParseQuery<ParseUser> = ParseUser.getQuery().whereEqualTo("email",login_email_edittext.text.toString())

        userEmailQuery.findInBackground(FindCallback { users, e ->

            if (e == null && users.size > 0){
                ParseUser.logInInBackground(users.get(0).username, login_password_edittext.text.toString(), LogInCallback { user, a ->

                    if(user != null){
                        Toast.makeText(this, "Welcome ${user.get("name")}!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, BoardsActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        ParseUser.logOut();
                        Toast.makeText(this, a.message, Toast.LENGTH_LONG).show();
                    }

                })

            }else{
                login_email_edittext.setError("Email adress does not mach any user")
            }

        })

    }

    /**
     * Fields validation got from login and pressing "login" button
     *
     * @return Boolean if every field pass the validation or not
     */
    private fun validate(): Boolean {
        var valid = true

        val email = login_email_edittext.text.toString()
        val password = login_password_edittext.text.toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            login_email_edittext.setError("Enter a valid email address")
            valid = false
        } else {
            login_email_edittext.setError(null)
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            login_password_edittext.setError("Between 4 and 10 alphanumeric characters")
            valid = false
        } else {
            login_password_edittext.setError(null)
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
