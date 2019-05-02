package com.project.jatz.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.jatz.R
import com.project.jatz.model.NoteItem
import com.project.jatz.presenter.MainAdapter


class FragmentThree : Fragment() {

    companion object {
        var adapter: MainAdapter? = null

        fun adding(adapter: MainAdapter){
            adapter.addItem()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var notesList = ArrayList<NoteItem>()
        notesList.add(NoteItem("Título1", "Subtitulo"))
        notesList.add(NoteItem("Título2", "Subtitulo"))
        notesList.add(NoteItem("Título3", "Subtitulo"))
        notesList.add(NoteItem("Título4", "Subtitulo"))


        val rootView = inflater.inflate(R.layout.fragment_fragment_one, container, false)

        val recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView

        var layoutManager = LinearLayoutManager(activity)
        adapter = MainAdapter(notesList)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                adapter!!.removeItem(viewHolder)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
        }

        var itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // Inflate the layout for this fragment
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
