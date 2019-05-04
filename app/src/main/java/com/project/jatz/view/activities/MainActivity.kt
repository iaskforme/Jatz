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

            val dialog = CreateNoteFragment()
            dialog.show(supportFragmentManager, dialog.tag)


            //IMPORTANTE PARA SABER QUE FRAGMENTO ESTA EN LA PANTALLA
            var page = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.main_view_pager+ ":" + main_view_pager.getCurrentItem())

            Log.v("TAG", "${page}")

            if (page is FragmentOne) {
                FragmentOne.adding(FragmentOne.adapter!!)
                Log.v("TAG", "To do page")
            }else{
                if(page is FragmentTwo) {
                    FragmentTwo.adding(FragmentTwo.adapter!!)
                    Log.v("TAG", "In progress page")
                }else{
                    if(page is FragmentThree) {
                        FragmentThree.adding(FragmentThree.adapter!!)
                        Log.v("TAG", "Done page")
                    }
                }
            }
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
