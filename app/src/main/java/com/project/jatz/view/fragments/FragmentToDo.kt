package com.project.jatz.view.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseQuery
import com.parse.ParseUser
import com.project.jatz.R
import com.project.jatz.model.BoardItem
import com.project.jatz.model.NoteItem
import com.project.jatz.presenter.NotesAdapter
import com.project.jatz.utils.Util

class FragmentToDo : Fragment() {

    var currentBoard: String? = ""
    var progressBar: ProgressBar? = null

    companion object {
        var recyclerView: RecyclerView? = null
        var adapter: NotesAdapter? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_notes, container, false)
        FragmentToDo.recyclerView = rootView.findViewById(R.id.recyclerView)
        progressBar = activity!!.findViewById(R.id.main_proggress_bar)

        if (this.arguments != null){
            currentBoard = this.arguments!!.getString("currentBoard")
        }

        loadData()

        onSwipeNote()


        return rootView
    }

    private fun loadData(){

        val boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        val boardIsInCache = boardQuery.hasCachedResult()
        boardQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", currentBoard)

        if (!boardIsInCache) {
            val boardItem = boardQuery.first

            val notesQuery = ParseQuery.getQuery(NoteItem::class.java)
            notesQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

            notesQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
            notesQuery.whereEqualTo("parentBoard", boardItem)
            notesQuery.whereEqualTo("state", "todo")

            try {
                val notesList = ArrayList(notesQuery.find())

                val layoutManager = LinearLayoutManager(activity)
                adapter = NotesAdapter(notesList)

                recyclerView!!.adapter = adapter
                recyclerView!!.layoutManager = layoutManager

            } catch (e: com.parse.ParseException) {
                Util.showToast(activity, e.message.toString())
                e.printStackTrace()
            }
        }
    }

    /**
     * Controls swipes of notes in this fragment
     */
    private fun onSwipeNote(){
        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                changeNoteState(viewHolder)
            }

            override fun onMove(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder): Boolean {
                return false
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun changeNoteState(viewHolder: RecyclerView.ViewHolder) {

        val title = adapter!!.getNoteAt(viewHolder.adapterPosition).getTitle().toString()

        val boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", currentBoard)

        boardQuery.getFirstInBackground { boardItem, e ->
            progressBar!!.visibility = View.VISIBLE

            val noteQuery = ParseQuery.getQuery(NoteItem::class.java)
            noteQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
            noteQuery.whereEqualTo("parentBoard", boardItem)
            noteQuery.whereEqualTo("title", title)

            noteQuery.getFirstInBackground { noteItem, a ->
                noteItem.setState("inprogress")
                noteItem.save()

                val notesQueryTODO = ParseQuery.getQuery(NoteItem::class.java)
                notesQueryTODO.whereEqualTo("createdBy", ParseUser.getCurrentUser())
                notesQueryTODO.whereEqualTo("parentBoard", boardItem)
                notesQueryTODO.whereEqualTo("state", "todo")
                notesQueryTODO.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

                val notesListTODO = ArrayList(notesQueryTODO.find())
                val layoutManagerTODO = LinearLayoutManager(activity)

                //Update static adapter
                adapter = NotesAdapter(notesListTODO)
                recyclerView!!.adapter = adapter
                recyclerView!!.layoutManager = layoutManagerTODO

                recyclerView!!.adapter!!.notifyDataSetChanged()

                progressBar!!.visibility = View.INVISIBLE

                val notesQueryINPROGRESS= ParseQuery.getQuery(NoteItem::class.java)
                notesQueryINPROGRESS.whereEqualTo("createdBy", ParseUser.getCurrentUser())
                notesQueryINPROGRESS.whereEqualTo("parentBoard", boardItem)
                notesQueryINPROGRESS.whereEqualTo("state", "inprogress")
                notesQueryINPROGRESS.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

                notesQueryINPROGRESS.findInBackground { notesListINPROGRESS, e ->
                    if (e == null) {
                        val adapterINPROGRESS = NotesAdapter(ArrayList(notesListINPROGRESS))
                        val layoutManagerINPROGRESS = LinearLayoutManager(activity)

                        FragmentInProgress.adapter = adapterINPROGRESS
                        FragmentInProgress.recyclerView!!.adapter = adapterINPROGRESS
                        FragmentInProgress.recyclerView!!.layoutManager = layoutManagerINPROGRESS

                        FragmentInProgress.recyclerView!!.adapter!!.notifyDataSetChanged()
                    }else {
                        Util.showToast(activity, e.message.toString())
                        e.printStackTrace()
                    }
                }

            }
        }
     }
}
