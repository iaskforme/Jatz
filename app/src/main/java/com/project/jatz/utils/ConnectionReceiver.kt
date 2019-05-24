package com.project.jatz.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.project.jatz.database.App

/**
 * Contains BroadCastReceiver extension for future implementation and Network checking
 */
class ConnectionReceiver: BroadcastReceiver() {

    companion object{
        var connectionReceiverListener: ConnectionReceiverListener? = null

        val isConnected: Boolean
        get(){
            val cm = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo

            return (activeNetwork != null &&activeNetwork.isConnected)
        }
    }

    /**
     * Interface sheltering function that gets the connection change
     */
    interface ConnectionReceiverListener{
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val isConnected = checkConnection(context)

        if(connectionReceiverListener != null){
            connectionReceiverListener!!.onNetworkConnectionChanged(isConnected)
        }
    }

    private fun checkConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return (activeNetwork != null &&activeNetwork.isConnected)
    }

}