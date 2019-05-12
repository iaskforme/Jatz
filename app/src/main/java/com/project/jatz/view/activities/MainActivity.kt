package com.project.jatz.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.jatz.*
import com.project.jatz.presenter.SecondAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.widget.Toast
import com.parse.ParseUser
import com.project.jatz.view.fragments.*

class MainActivity : AppCompatActivity() {

    val fragmentAdapter = SecondAdapter(supportFragmentManager)
    val bottomNavDrawerFragment = BottomNavigationSheetFragment()

    companion object{
        var todoFragment = FragmentOne()
        var progressFragment = FragmentTwo()
        var doneFragment = FragmentThree()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_bottom_appbar)

        createTabs(fragmentAdapter)

        handleFab(main_add_button)
    }

    /**
     *
     */
    private fun handleFab(floatButton: FloatingActionButton) {

        floatButton.setOnClickListener {

            //IMPORTANTE PARA SABER QUE FRAGMENTO ESTA EN LA PANTALLA
            if(checkBoard()){

                var createNoteDialog = CreateNoteFragment()

                var bundleNote = Bundle()

                //BundleBottom
                var boardItem = bottomNavDrawerFragment.currentBoardItem
                bundleNote.putParcelable("boardItem", boardItem)
                createNoteDialog.arguments = bundleNote

                //Bundle fragment int
                var page = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.main_view_pager+ ":" + main_view_pager.getCurrentItem())

                when{
                    page is FragmentOne ->{
                        bundleNote.putInt("fragment",0)
                    }

                    page is FragmentTwo ->{
                        bundleNote.putInt("fragment",1)
                    }

                    page is FragmentThree -> {
                        bundleNote.putInt("fragment",2)
                    }
                }

                createNoteDialog.show(supportFragmentManager, createNoteDialog.tag)
            }else {
                Toast.makeText(this,"No Board selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     *
     */
    private fun createTabs(adapter: SecondAdapter){
        adapter.addFragment(todoFragment, "To Do")
        adapter.addFragment(progressFragment, "In Progress")
        adapter.addFragment(doneFragment, "Done")

        main_view_pager.adapter = adapter
        main_tab_layout.setupWithViewPager(main_view_pager)
    }

    private fun checkBoard(): Boolean{
       return bottomNavDrawerFragment.boardIsClicked
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_app_bar, menu)
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

            android.R.id.home -> {
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }

        return true
    }

}
