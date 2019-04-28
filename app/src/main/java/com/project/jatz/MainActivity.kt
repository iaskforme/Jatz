package com.project.jatz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_fragment_one.*

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

        // Zona de adaptadores para fragmentos

        val fragmentAdapter = SecondAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(FragmentOne(), "To Do")
        fragmentAdapter.addFragment(FragmentTwo(), "In Progress")
        fragmentAdapter.addFragment(FragmentThree(), "Done")
        viewPager.adapter = fragmentAdapter
        table.setupWithViewPager(viewPager)

    }


    private fun handleFab(floatButton: FloatingActionButton) {
        floatButton!!.setOnClickListener {

            fabTapped = !fabTapped

            if (fabTapped) {
                bottomBar!!.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                floatButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close_black_24dp))
            } else {
                bottomBar!!.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                floatButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_black_24dp))
            }


        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
