package com.project.jatz.database

import android.app.Application
import com.parse.Parse
import com.parse.ParseInstallation
import com.parse.ParseObject
import com.project.jatz.R
import com.project.jatz.model.BoardItem

/**
 * Class that contains Parse initialization
 */
class App: Application(){

    override fun onCreate() {
        super.onCreate()
        ParseObject.registerSubclass(BoardItem::class.java)
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