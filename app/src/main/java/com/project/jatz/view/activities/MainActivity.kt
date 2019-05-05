package com.project.jatz.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.jatz.*
import com.project.jatz.presenter.SecondAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.parse.ParseUser
import com.project.jatz.view.fragments.*


class MainActivity : AppCompatActivity() {

    val fragmentAdapter = SecondAdapter(supportFragmentManager)

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
            val createNoteDialog = CreateNoteFragment()


            var page = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.main_view_pager+ ":" + main_view_pager.getCurrentItem())
            var bundle = Bundle()
            createNoteDialog.arguments = bundle

            when{
                page is FragmentOne ->{
                    bundle.putInt("fragment",0)
                }

                page is FragmentTwo ->{
                    bundle.putInt("fragment",1)
                }

                page is FragmentThree -> {
                    bundle.putInt("fragment",2)
                }
            }

            createNoteDialog.show(supportFragmentManager, createNoteDialog.tag)

        }
    }

    /**
     *
     */
    private fun createTabs(adapter: SecondAdapter){

        adapter.addFragment(FragmentOne(), "To Do")
        adapter.addFragment(FragmentTwo(), "In Progress")
        adapter.addFragment(FragmentThree(), "Done")

        main_view_pager.adapter = adapter
        main_tab_layout.setupWithViewPager(main_view_pager)
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
                val bottomNavDrawerFragment = BottomNavigationSheetFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }

        return true
    }




}
