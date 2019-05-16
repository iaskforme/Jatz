package com.project.jatz.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.FindCallback
import com.parse.ParseQuery
import com.parse.ParseUser
import com.project.jatz.R
import com.project.jatz.model.BoardItem
import com.project.jatz.presenter.BoardAdapter
import com.project.jatz.utils.Util
import kotlinx.android.synthetic.main.activity_boards.*

class BoardsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boards)

        setSupportActionBar(boards_top_appbar)

        setAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.boards_bottom_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {

            R.id.top_app_add -> {
                Util.showToast(this, "Añadir")
            }
            R.id.top_app_logout -> {
                ParseUser.logOut()
                var loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
        }

        return true
    }

    fun setAdapter(){

        var boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())

        boardQuery.findInBackground(FindCallback<BoardItem> { boardList, e ->

            if(e == null){
                if(boardList.count() > 0){
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = BoardAdapter(boardList)
                }else{
                    Util.showToast(this,"No notes")
                }
            }else{
                Util.showToast(this, e.message.toString())
            }
        })

    }
}
