package com.project.jatz

import android.app.Application
import com.parse.Parse
import com.parse.ParseInstallation

/**
 * Class that contains Parse initialization
 */
class App(): Application(){

    override fun onCreate() {
        super.onCreate()
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_app_id))
                .clientKey(getString(R.string.parse_client_key))
                .server(getString(R.string.parse_server_url))
                .build()
        )

        ParseInstallation.getCurrentInstallation().saveInBackground()
    }
}