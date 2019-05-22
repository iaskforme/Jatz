package com.project.jatz.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.project.jatz.*
import com.project.jatz.presenter.TabsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.parse.ParseUser
import com.project.jatz.view.fragments.*

class NotesActivity : AppCompatActivity() {

    val fragmentAdapter = TabsAdapter(supportFragmentManager)
    var currentBoard: String = ""

    companion object{
        var todoFragment = FragmentToDo()
        var progressFragment = FragmentDone()
        var doneFragment = FragmentInProgress()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_bottom_appbar)

        getCurrentBoardIntent()

        createTabs(fragmentAdapter)

        handleFab(main_add_button)
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
                var loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
        }

        return true
    }

    /**
     *
     */

    private fun handleFab(floatButton: FloatingActionButton) {

        floatButton.setOnClickListener {

            var createNoteDialog = CreateNoteFragment()

            var bundle = Bundle()
            bundle.putString("currentBoard", currentBoard)

            createNoteDialog.arguments = bundle

            //Bundle fragment int
            var page = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.main_view_pager+ ":" + main_view_pager.getCurrentItem())
            when{
                page is FragmentToDo ->{
                    bundle.putInt("pageState",0)
                }

                page is FragmentDone ->{
                    bundle.putInt("pageState",1)
                }

                page is FragmentInProgress -> {
                    bundle.putInt("pageState",2)
                }
            }

            createNoteDialog.show(supportFragmentManager, createNoteDialog.tag)
        }
    }

    /**
     * Tabs creation troguh fragments. This won't change at any time.
     */
    private fun createTabs(adapter: TabsAdapter){
        adapter.addFragment(todoFragment, "To Do")
        adapter.addFragment(progressFragment, "In Progress")
        adapter.addFragment(doneFragment, "Done")

        sendBoard()

        main_view_pager.adapter = adapter
        main_tab_layout.setupWithViewPager(main_view_pager)
    }

    /**
     * Gets intent passed by BoardsActivity
     */
    private fun getCurrentBoardIntent(){
        var intent = intent
        currentBoard = intent.extras.getString("currentBoard")
    }

    /**
     * Sends the current board to every fragment created
     */
    private fun sendBoard(){
        var bundle = Bundle()
        bundle.putString("currentBoard", currentBoard)

        todoFragment.arguments = bundle
        progressFragment.arguments = bundle
        doneFragment.arguments = bundle

    }
}
