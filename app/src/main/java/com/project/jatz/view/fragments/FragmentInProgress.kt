package com.project.jatz.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import com.project.jatz.R
import com.project.jatz.model.BoardItem
import com.project.jatz.model.NoteItem
import com.project.jatz.presenter.NotesAdapter
import com.project.jatz.utils.Util


class FragmentInProgress : Fragment() {

    var currentBoard: String = ""

    companion object {
        var recyclerView: RecyclerView? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_notes, container, false)
        FragmentInProgress.recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView

        if (this.arguments != null){
            currentBoard = this.arguments!!.getString("currentBoard")
        }

        //Query  getting the parentBoard
        var boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        var boardIsInCache = boardQuery.hasCachedResult()
        boardQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK)

        if(!boardIsInCache){
            boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
            boardQuery.whereEqualTo("title", currentBoard)

            var boardList = ArrayList(boardQuery.find())

            //Query getting notes
            var notesQuery = ParseQuery.getQuery(NoteItem::class.java)
            notesQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

            notesQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
            notesQuery.whereEqualTo("parentBoard", boardList[0])
            notesQuery.whereEqualTo("state","inprogress")

            try{
                var notesList = ArrayList(notesQuery.find())

                var layoutManager = LinearLayoutManager(activity)
                var adapter = NotesAdapter(notesList)

                recyclerView!!.adapter = adapter
                recyclerView!!.layoutManager = layoutManager

            }catch (e: ParseException){
                Util.showToast(activity, e.message.toString())
                e.printStackTrace()
            }
        }

        // ItemTouchHelper with onSwiped method to move trough tabs/states
        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                //adapter!!.removeItem(viewHolder)
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

        return rootView
    }

}
