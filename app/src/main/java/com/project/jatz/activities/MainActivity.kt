package com.project.jatz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.parse.ParseUser
import com.project.jatz.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var floatButton: FloatingActionButton? = null
    var bottomBar: BottomAppBar? = null
    var fabTapped: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomBar = findViewById(R.id.bottomAppBar)
        floatButton = findViewById(R.id.addButton)

        setSupportActionBar(bottomAppBar)

        handleFab(floatButton!!)


        // Zona de adaptadores para fragmentos ()
        val fragmentAdapter = SecondAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(FragmentOne(), "To Do")
        fragmentAdapter.addFragment(FragmentTwo(), "In Progress")
        fragmentAdapter.addFragment(FragmentThree(), "Done")
        viewPager.adapter = fragmentAdapter
        table.setupWithViewPager(viewPager)

        //COSAS


    }

    //Boton add and exit handler
    private fun handleFab(floatButton: FloatingActionButton) {
        floatButton!!.setOnClickListener {

            fabTapped = !fabTapped

            if (fabTapped) {
                FragmentOne.adding(FragmentOne.adapter!!)

                //IMPORTANTE PARA SABER QUE FRAGMENTO ESTA EN LA PANTALLA
                var page = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + viewPager.getCurrentItem())

                if (page is FragmentOne) {
                    Log.v("TAG", "To do page")
                }else{
                    if(page is FragmentTwo) {
                        Log.v("TAG", "In progress page")
                    }else{
                        if(page is FragmentThree) {
                            Log.v("TAG", "Done page")
                        }
                    }
                }

                bottomBar!!.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                floatButton.setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.ic_close_black_24dp
                ))
            } else {
                bottomBar!!.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                floatButton.setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.ic_add_black_24dp
                ))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.bottom_app_home -> Toast.makeText(this,"Home item is clicked!", Toast.LENGTH_SHORT).show()
            R.id.bottom_app_options -> Toast.makeText(this,"Settings item is clicked!", Toast.LENGTH_SHORT).show()

            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationSheetFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }

        return true
    }


}
