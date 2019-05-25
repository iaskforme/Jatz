package com.project.jatz.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.project.jatz.*
import com.project.jatz.presenter.TabsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.parse.ParseQuery
import com.parse.ParseUser
import com.project.jatz.database.App
import com.project.jatz.model.BoardItem
import com.project.jatz.model.NoteItem
import com.project.jatz.utils.ConnectionReceiver
import com.project.jatz.utils.Util
import com.project.jatz.view.fragments.*

class NotesActivity : AppCompatActivity(), ConnectionReceiver.ConnectionReceiverListener {

    val fragmentAdapter = TabsAdapter(supportFragmentManager)
    var connectionReceiver = ConnectionReceiver()

    companion object{
        var todoFragment = FragmentToDo()
        var progressFragment = FragmentInProgress()
        var doneFragment = FragmentDone()

        var msupportFragmentManager: FragmentManager? = null

        var currentBoard: String? = ""

        var bundleEditNote = Bundle()
        var bundleCreateNote = Bundle()

        var mainPager: Int? = null

        fun clickedNote(noteItem: NoteItem){
            val editNoteDialog = EditNoteFragment()

            bundleEditNote.putString("currentBoard", currentBoard)
            bundleEditNote.putString("currentNote", "${noteItem.getTitle()}")
            bundleEditNote.putInt("currentPager", mainPager!!)

            editNoteDialog.arguments = bundleEditNote
            editNoteDialog.show(msupportFragmentManager, editNoteDialog.tag)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        baseContext.registerReceiver(connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        App.instance.setConnectionListener(this)
        NotesActivity.msupportFragmentManager = supportFragmentManager

        setSupportActionBar(main_bottom_appbar)

        getCurrentBoardIntent()

        createTabs(fragmentAdapter)

        handleFab(main_add_button)

        mainPager = main_view_pager.currentItem
    }

    override fun onPause() {
        super.onPause()
        // Unregisters reciver when another activity its prompted
        unregisterReceiver(connectionReceiver)

        baseContext.registerReceiver(connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        App.instance.setConnectionListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notes_bottom_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {

            R.id.bottom_app_home -> {
                main_view_pager.setCurrentItem(1)
            }
            R.id.bottom_app_logout -> {
                ParseUser.logOut()
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
            R.id.bottom_app_delete -> {
                main_proggress_bar.visibility = View.VISIBLE
                deleteBoard()
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
     * Handles the floating button click
     */
    private fun handleFab(floatButton: FloatingActionButton) {

        floatButton.setOnClickListener {

            val createNoteDialog = CreateNoteFragment()

            bundleCreateNote.putString("currentBoard", currentBoard)

            createNoteDialog.arguments = bundleCreateNote

            createNoteDialog.show(supportFragmentManager, createNoteDialog.tag)
        }
    }

    /**
     * Tabs creation through fragments. This won't change at any time.
     */
    private fun createTabs(adapter: TabsAdapter){
        adapter.addFragment(todoFragment, "To Do")
        adapter.addFragment(progressFragment, "In Progress")
        adapter.addFragment(doneFragment, "Done")

        sendBoard()

        main_view_pager.adapter = adapter
        main_tab_layout.setupWithViewPager(main_view_pager)

        main_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when(position){
                    0 ->{
                        bundleEditNote.putInt("currentPage", 0)
                        bundleCreateNote.putInt("currentPage",0)
                    }

                    1 ->{
                        bundleEditNote.putInt("currentPage", 1)
                        bundleCreateNote.putInt("currentPage",1)
                    }

                    2 -> {
                        bundleEditNote.putInt("currentPage", 2)
                        bundleCreateNote.putInt("currentPage",2)
                    }
                }
            }

        })
    }

    /**
     * Gets intent passed by BoardsActivity
     */
    private fun getCurrentBoardIntent(){
        val intent = intent
        currentBoard = intent.extras!!.getString("currentBoard")
    }

    /**
     * Sends the current board to every fragment created
     */
    private fun sendBoard(){
        val bundle = Bundle()
        bundle.putString("currentBoard", currentBoard)

        todoFragment.arguments = bundle
        progressFragment.arguments = bundle
        doneFragment.arguments = bundle
    }

    /**
     * Function that is called by Delete Board Menu item and deletes the board an its notes
     */
    private fun deleteBoard(){

        val boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", currentBoard)

        boardQuery.getFirstInBackground{ boardItem, e ->
            if(e == null ){

                val notesQuery = ParseQuery.getQuery(NoteItem::class.java)
                notesQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
                notesQuery.whereEqualTo("parentBoard", boardItem)

                notesQuery.findInBackground{ noteList, f ->
                    if (f == null){
                        for (x in noteList){
                            x.delete()
                        }

                        boardItem.delete()

                        val boardsQuery = ParseQuery.getQuery(BoardItem::class.java)
                        boardsQuery .whereEqualTo("createdBy", ParseUser.getCurrentUser())

                        boardsQuery.findInBackground{ boardList, g ->

                            val intent = Intent(this, BoardsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }else{
                e.printStackTrace()
            }
        }
    }
}
