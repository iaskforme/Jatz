package com.project.jatz.database

import android.app.Application
import com.parse.Parse
import com.parse.ParseInstallation
import com.parse.ParseObject
import com.project.jatz.R
import com.project.jatz.model.BoardItem
import com.project.jatz.model.NoteItem
import com.project.jatz.utils.ConnectionReceiver


/**
 * Class that contains Parse initialization
 */
class App: Application(){

    companion object{
        @get:Synchronized
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        ParseObject.registerSubclass(BoardItem::class.java)
        ParseObject.registerSubclass(NoteItem::class.java)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_app_id))
                .clientKey(getString(R.string.parse_client_key))
                .server(getString(R.string.parse_server_url))
                .build()
        )

        instance = this

        ParseInstallation.getCurrentInstallation().saveInBackground()
    }

    fun setConnectionListener(listener: ConnectionReceiver.ConnectionReceiverListener){
        ConnectionReceiver.connectionReceiverListener = listener
    }
}