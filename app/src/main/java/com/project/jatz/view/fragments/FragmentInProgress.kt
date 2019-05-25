package com.project.jatz.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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

    var currentBoard: String? = ""
    var progressBar: ProgressBar? = null

    companion object {
        var recyclerView: RecyclerView? = null
        var adapter: NotesAdapter? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_notes, container, false)
        FragmentInProgress.recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView
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

            //Query getting notes
            val notesQuery = ParseQuery.getQuery(NoteItem::class.java)
            notesQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

            notesQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
            notesQuery.whereEqualTo("parentBoard", boardItem)
            notesQuery.whereEqualTo("state", "inprogress")

            try {
                val notesList = ArrayList(notesQuery.find())

                val layoutManager = LinearLayoutManager(activity)
                adapter = NotesAdapter(notesList)

                recyclerView!!.adapter = adapter
                recyclerView!!.layoutManager = layoutManager

            } catch (e: ParseException) {
                Util.showToast(activity, e.message.toString())
                e.printStackTrace()
            }
        }
    }

    /**
     * Controls swipes of notes in this fragment
     */
    private fun onSwipeNote(){

        val itemTouchHelperCallbackRight = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                val currentNote = adapter!!.getNoteAt(viewHolder.adapterPosition).getTitle().toString()
                changeNoteState(viewHolder, "done")
            }

            override fun onMove(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder): Boolean {
                return false
            }
        }

        val itemTouchHelperRight = ItemTouchHelper(itemTouchHelperCallbackRight)
        itemTouchHelperRight.attachToRecyclerView(recyclerView)

        val itemTouchHelperCallbackLeft = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                val currentNote = adapter!!.getNoteAt(viewHolder.adapterPosition).getTitle().toString()
                changeNoteState(viewHolder, "todo")
            }

            override fun onMove(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder): Boolean {
                return false
            }
        }

        val itemTouchHelperLeft = ItemTouchHelper(itemTouchHelperCallbackLeft)
        itemTouchHelperLeft.attachToRecyclerView(recyclerView)
    }

    private fun changeNoteState(viewHolder: RecyclerView.ViewHolder, state: String) {

        if(state == "done"){
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
                    noteItem.setState("done")
                    noteItem.save()

                    val notesQueryINPROGRESS = ParseQuery.getQuery(NoteItem::class.java)
                    notesQueryINPROGRESS.whereEqualTo("createdBy", ParseUser.getCurrentUser())
                    notesQueryINPROGRESS.whereEqualTo("parentBoard", boardItem)
                    notesQueryINPROGRESS.whereEqualTo("state", "inprogress")
                    notesQueryINPROGRESS.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

                    val notesListINPROGRESS = ArrayList(notesQueryINPROGRESS.find())
                    val layoutManagerINPROGRESS = LinearLayoutManager(activity)

                    //Update static adapter
                    adapter = NotesAdapter(notesListINPROGRESS)
                    recyclerView!!.adapter = adapter
                    recyclerView!!.layoutManager = layoutManagerINPROGRESS

                    recyclerView!!.adapter!!.notifyDataSetChanged()

                    progressBar!!.visibility = View.INVISIBLE

                    val notesQueryDONE= ParseQuery.getQuery(NoteItem::class.java)
                    notesQueryDONE.whereEqualTo("createdBy", ParseUser.getCurrentUser())
                    notesQueryDONE.whereEqualTo("parentBoard", boardItem)
                    notesQueryDONE.whereEqualTo("state", state)
                    notesQueryDONE.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

                    notesQueryDONE.findInBackground { notesListDONE, e ->
                        if (e == null) {
                            val adapterDONE = NotesAdapter(ArrayList(notesListDONE))
                            val layoutManagerDONE = LinearLayoutManager(activity)

                            FragmentDone.adapter = adapterDONE
                            FragmentDone.recyclerView!!.adapter = adapterDONE
                            FragmentDone.recyclerView!!.layoutManager = layoutManagerDONE

                            FragmentDone.recyclerView!!.adapter!!.notifyDataSetChanged()
                        }else {
                            Util.showToast(activity, e.message.toString())
                            e.printStackTrace()
                        }
                    }

                }
            }

        }else {
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
                    noteItem.setState("todo")
                    noteItem.save()

                    val notesQueryINPROGRESS = ParseQuery.getQuery(NoteItem::class.java)
                    notesQueryINPROGRESS.whereEqualTo("createdBy", ParseUser.getCurrentUser())
                    notesQueryINPROGRESS.whereEqualTo("parentBoard", boardItem)
                    notesQueryINPROGRESS.whereEqualTo("state", "inprogress")
                    notesQueryINPROGRESS.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

                    val notesListINPROGRESS = ArrayList(notesQueryINPROGRESS.find())
                    val layoutManagerINPROGRESS = LinearLayoutManager(activity)

                    //Update static adapter
                    adapter = NotesAdapter(notesListINPROGRESS)
                    recyclerView!!.adapter = adapter
                    recyclerView!!.layoutManager = layoutManagerINPROGRESS

                    recyclerView!!.adapter!!.notifyDataSetChanged()

                    progressBar!!.visibility = View.INVISIBLE

                    val notesQueryTODO = ParseQuery.getQuery(NoteItem::class.java)
                    notesQueryTODO.whereEqualTo("createdBy", ParseUser.getCurrentUser())
                    notesQueryTODO.whereEqualTo("parentBoard", boardItem)
                    notesQueryTODO.whereEqualTo("state", state)
                    notesQueryTODO.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

                    notesQueryTODO.findInBackground { noteListTODO, e ->
                        if (e == null) {
                            val adapterTODO = NotesAdapter(ArrayList(noteListTODO))
                            val layoutManagerTODO = LinearLayoutManager(activity)

                            FragmentToDo.adapter = adapterTODO
                            FragmentToDo.recyclerView!!.adapter = adapterTODO
                            FragmentToDo.recyclerView!!.layoutManager = layoutManagerTODO

                            FragmentToDo.recyclerView!!.adapter!!.notifyDataSetChanged()
                        } else {
                            Util.showToast(activity, e.message.toString())
                            e.printStackTrace()
                        }
                    }

                }
            }
        }
    }

}
