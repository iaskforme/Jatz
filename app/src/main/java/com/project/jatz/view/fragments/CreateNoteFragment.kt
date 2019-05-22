package com.project.jatz.view.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseQuery
import com.parse.ParseUser
import com.project.jatz.R
import com.project.jatz.model.BoardItem
import com.project.jatz.model.NoteItem
import com.project.jatz.presenter.NotesAdapter
import com.project.jatz.utils.Util

/**
 * Class that inherits from DialogFragment and contains the parameters that allow the future creation of one. In this case for the creation of a note.
 */
class CreateNoteFragment() : DialogFragment(){

    var index: Int? = null
    var currentBoard: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material)

        val rootView = inflater.inflate(R.layout.fragment_create_note, container, false)

        val saveText: TextView = rootView.findViewById(R.id.fragmentnote_save_textview)
        val cancelText: TextView = rootView.findViewById(R.id.fragmentnote_cancel_textview)
        val titleEditText: EditText = rootView.findViewById(R.id.fragmentnote_title_edittext)
        val descriptionEditText: EditText = rootView.findViewById(R.id.fragmentnote_description_edittext)
        val commentEditText: EditText = rootView.findViewById(R.id.fragmentnote_comment_edittext)

        if (this.arguments != null) {
            currentBoard = this.arguments!!.getString("currentBoard")
            index = arguments!!.getInt("pageState")
        }

        saveText.setOnClickListener {
            createNote(titleEditText, descriptionEditText, commentEditText, index)
        }

        cancelText.setOnClickListener {
            dismiss()
        }

        return rootView
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Create note: validation and calling fail if it fails in some way
     */
    fun createNote(title: EditText, description:EditText, comment: EditText, fragmentIndex: Int?){

        if (!validate(title, description, comment)) {
            return
        }

        //Here should be the creation or addition to the list

        when(fragmentIndex){
            0 -> {
                uploadNote(title,description, comment, "todo")
                updateToDoAdapter()
                dismiss()
            }

            1 -> {
                uploadNote(title, description, comment, "done")
                updateDoneAdapter()
                dismiss()
            }

            2 -> {
                uploadNote(title, description, comment, "inprogress")
                updateInProgressAdapter()
                dismiss()
            }
        }

    }

    private fun uploadNote(title: EditText, description:EditText, comment: EditText, state: String){

        var boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", currentBoard)

        var boardItem = boardQuery.first

        var note = NoteItem()
        note.setTitle(title.text.toString())
        note.setDescription(description.text.toString())
        note.setComment(comment.text.toString())
        note.setState(state)
        note.setUser(ParseUser.getCurrentUser())
        note.setBoard(boardItem)

        note.save()
    }

    /**
     * validation for the fields
     */
    private fun validate(title: EditText, description:EditText, comment: EditText): Boolean {
        var valid = true

        if(title.text.toString().isEmpty()){
            title.setError("Fill in the field")
            valid = false
        }else{
            title.setError(null)
        }

        if(description.text.toString().isEmpty()){
            description.setError("Fill in the field")
            valid = false
        }else{
            description.setError(null)
        }

        if(comment.text.toString().isEmpty()){
            comment.setError("Fill in the field")
            valid = false
        }else{
            comment.setError(null)
        }

        return valid
    }

    private fun updateToDoAdapter(){
        val boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", currentBoard)

        boardQuery.getFirstInBackground{boardItem, e ->
            if(boardItem != null){
                val notesQuery = ParseQuery.getQuery(NoteItem::class.java)
                notesQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
                notesQuery.whereEqualTo("parentBoard", boardItem)
                notesQuery.whereEqualTo("state","todo")

                notesQuery.findInBackground{notesList, a ->
                    if(a == null){
                        val layoutManager = LinearLayoutManager(activity)
                        val adapter = NotesAdapter(ArrayList(notesList))

                        FragmentToDo.recyclerView!!.adapter = adapter
                        FragmentToDo.recyclerView!!.layoutManager = layoutManager
                    }else{
                        Util.showToast(activity, a.message.toString())
                        a.printStackTrace()
                    }

                }
            }
        }
    }

    private fun updateInProgressAdapter(){
        val boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", currentBoard)

        boardQuery.getFirstInBackground{boardItem, e ->
            if(boardItem != null){
                val notesQuery = ParseQuery.getQuery(NoteItem::class.java)
                notesQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
                notesQuery.whereEqualTo("parentBoard", boardItem)
                notesQuery.whereEqualTo("state","inprogress")

                notesQuery.findInBackground{notesList, a ->
                    if(a == null){
                        val layoutManager = LinearLayoutManager(activity)
                        val adapter = NotesAdapter(ArrayList(notesList))

                        FragmentInProgress.recyclerView!!.adapter = adapter
                        FragmentInProgress.recyclerView!!.layoutManager = layoutManager
                    }else{
                        Util.showToast(activity, a.message.toString())
                        a.printStackTrace()
                    }

                }
            }
        }
    }

    private fun updateDoneAdapter(){
        val boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", currentBoard)

        boardQuery.getFirstInBackground{boardItem, e ->
            if(boardItem != null){
                val notesQuery = ParseQuery.getQuery(NoteItem::class.java)
                notesQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
                notesQuery.whereEqualTo("parentBoard", boardItem)
                notesQuery.whereEqualTo("state","done")

                notesQuery.findInBackground{notesList, a ->
                    if(a == null){
                        val layoutManager = LinearLayoutManager(activity)
                        val adapter = NotesAdapter(ArrayList(notesList))

                        FragmentDone.recyclerView!!.adapter = adapter
                        FragmentDone.recyclerView!!.layoutManager = layoutManager
                    }else{
                        Util.showToast(activity, a.message.toString())
                        a.printStackTrace()
                    }

                }
            }
        }
    }
}

