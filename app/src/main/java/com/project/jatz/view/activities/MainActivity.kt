package com.project.jatz.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.jatz.*
import com.project.jatz.presenter.SecondAdapter
import com.project.jatz.view.fragments.BottomNavigationSheetFragment
import com.project.jatz.view.fragments.FragmentOne
import com.project.jatz.view.fragments.FragmentThree
import com.project.jatz.view.fragments.FragmentTwo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var fabTapped: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter = SecondAdapter(supportFragmentManager)

        setSupportActionBar(main_bottom_appbar)

        createTabs(fragmentAdapter)

        handleFab(main_add_button)


    }

    /**
     *
     */
    private fun handleFab(floatButton: FloatingActionButton) {

        floatButton.setOnClickListener {

            fabTapped = !fabTapped

            if (fabTapped) {
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

                main_bottom_appbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                floatButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close_black_24dp))
            } else {
                main_bottom_appbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                floatButton.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_add_black_24dp))
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
                Toast.makeText(this,"Home item is clicked!", Toast.LENGTH_SHORT).show()
            }
            R.id.bottom_app_options -> {
                Toast.makeText(this,"Settings item is clicked!", Toast.LENGTH_SHORT).show()
            }

            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationSheetFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }

        return true
    }


}
