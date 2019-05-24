package com.project.jatz.view.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ParseException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseQuery
import com.parse.ParseUser
import com.project.jatz.R
import com.project.jatz.database.App
import com.project.jatz.model.BoardItem
import com.project.jatz.presenter.BoardsAdapter
import com.project.jatz.utils.ConnectionReceiver
import com.project.jatz.utils.Util
import com.project.jatz.view.fragments.CreateBoardFragment
import kotlinx.android.synthetic.main.activity_boards.*

class BoardsActivity : AppCompatActivity(), ConnectionReceiver.ConnectionReceiverListener{

    var connectionReceiver = ConnectionReceiver()

    companion object{
        var contexto: Context? = null

        fun clickedBoard(boardItem: BoardItem){
            val intent = Intent(contexto, NotesActivity::class.java)
            intent.putExtra("currentBoard", "${boardItem.getTitle()}")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            contexto!!.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boards)

        baseContext.registerReceiver(connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        App.instance.setConnectionListener(this)

        boards_empty_layout.visibility = View.GONE
        contexto = applicationContext

        setSupportActionBar(boards_top_appbar)

        setAdapter()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(connectionReceiver)

        baseContext.registerReceiver(connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        App.instance.setConnectionListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.boards_bottom_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.top_app_add -> {
                createBoard()
            }
            R.id.top_app_logout -> {
                ParseUser.logOut()
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
        }

        return true
    }

    /**
     * Checking and handling network connectivity through a BroadcastReceiver
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if(!isConnected) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Util.showToast(this, "No connection available!")
            finish()
        }
    }

    /**
     * Setting adapter for the RecyclerView and getting objects from a query
     */
    private fun setAdapter(){

        val boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

        boardQuery.findInBackground{ boardList, e ->
            if(e == null){
                if(boardList.count() > 0){
                    boards_recycler_view.layoutManager = LinearLayoutManager(this)
                    boards_recycler_view.adapter = BoardsAdapter(ArrayList(boardList))
                }else{
                    boards_recycler_view.visibility = View.GONE
                    boards_empty_layout.visibility = View.VISIBLE
                }
            }else{
                Util.showToast(this, "${e.message}")
            }
        }

    }

    /**
     * Launches dialog fragment to create the board
     */
    private fun createBoard(){
        val dialogCreateBoard =  CreateBoardFragment()
        dialogCreateBoard.show(supportFragmentManager, dialogCreateBoard.tag)
    }
}
