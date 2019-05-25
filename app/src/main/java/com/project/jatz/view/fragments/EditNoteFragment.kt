package com.project.jatz.view.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import com.project.jatz.R
import com.project.jatz.model.BoardItem
import com.project.jatz.model.NoteItem
import com.project.jatz.presenter.NotesAdapter
import com.project.jatz.utils.Util

/**
 * Class that inherits from DialogFragment and contains the parameters that allow the edition of the note clicked.
 */
class EditNoteFragment: DialogFragment(){

    var currentNote: String? = ""
    var currentBoard: String? = ""
    var currentPage: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material)

        val rootView = inflater.inflate(R.layout.fragment_edit_note, container, false)

        val saveText: TextView = rootView.findViewById(R.id.fragmenteditnote_save_textview)
        val cancelText: TextView = rootView.findViewById(R.id.fragmenteditnote_cancel_textview)
        val deleteText: TextView = rootView.findViewById(R.id.fragmenteditnote_delete_textview)
        val titleEditText: EditText = rootView.findViewById(R.id.fragmenteditnote_title_edittext)
        val descriptionEditText: EditText = rootView.findViewById(R.id.fragmenteditnote_description_edittext)
        val commentEditText: EditText = rootView.findViewById(R.id.fragmenteditnote_comment_edittext)

        if (this.arguments != null) {
            currentNote = this.arguments!!.getString("currentNote")
            currentBoard = this.arguments!!.getString("currentBoard")
            currentPage = this.arguments!!.getInt("currentPage")
        }

        loadNoteData(titleEditText, descriptionEditText, commentEditText)

        saveText.setOnClickListener {
            saveteNote(titleEditText, descriptionEditText, commentEditText)

        }

        cancelText.setOnClickListener {
            dismiss()
        }

        deleteText.setOnClickListener {
            deleteNote(titleEditText, descriptionEditText, commentEditText)
        }

        return rootView
    }

    /**
     * Loading data of the
     *
     * @param title: EditText
     * @param description: EditText
     * @param comment: EditText
     */
    private fun loadNoteData(title: EditText, description: EditText, comment: EditText){
        val noteQuery = ParseQuery.getQuery(NoteItem::class.java)
        noteQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        noteQuery.whereEqualTo("title", currentNote)

        noteQuery.getFirstInBackground{ noteItem, e ->
            if(noteItem != null){
                title.setText("${noteItem.getTitle()}")
                description.setText("${noteItem.getDescription()}")
                comment.setText("${noteItem.getComment()}")
            }
        }
    }

    /**
     * Saving note with new parameters and updating adapters
     *
     * @param title: EditText
     * @param description: EditText
     * @param comment: EditText
     */
    private fun saveteNote(title: EditText, description: EditText, comment: EditText){

        val boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", currentBoard)

        boardQuery.getFirstInBackground{boardItem, e ->
            val noteQuery = ParseQuery.getQuery(NoteItem::class.java)
            noteQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
            noteQuery.whereEqualTo("parentBoard", boardItem)
            noteQuery.whereEqualTo("title", title.text.toString())

            noteQuery.getFirstInBackground{ noteItem, a ->
                if (noteItem != null){
                    title.setError("There is a note with this name")
                }else{
                    if(title.text.toString().isEmpty()){
                        title.setError("Fill in the field")
                    }else{
                        title.setError(null)

                        if(description.text.toString().isEmpty()){
                            description.setError("Fill in the field")
                        }else{
                            description.setError(null)

                            if(comment.text.toString().isEmpty()){
                                comment.setError("Fill in the field")
                            }else{
                                comment.setError(null)

                                when(currentPage){
                                    0 -> {
                                        uploadNote(title,description, comment)
                                        updateToDoAdapter()
                                        dismiss()
                                    }

                                    1 -> {
                                        uploadNote(title, description, comment)
                                        updateInProgressAdapter()
                                        dismiss()
                                    }

                                    2 -> {
                                        uploadNote(title, description, comment)
                                        updateDoneAdapter()
                                        dismiss()
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    /**
     * Deleting note and updating adapters
     *
     * @param title: EditText
     * @param description: EditText
     * @param comment: EditText
     */
    private fun deleteNote(title: EditText, description: EditText, comment: EditText) {

        val boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", currentBoard)

        boardQuery.getFirstInBackground { boardItem, e ->
            val noteQuery = ParseQuery.getQuery(NoteItem::class.java)
            noteQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
            noteQuery.whereEqualTo("parentBoard", boardItem)
            noteQuery.whereEqualTo("title", title.text.toString())

            noteQuery.getFirstInBackground { noteItem, a ->

                noteItem.deleteInBackground {
                    when(currentPage) {
                        0 -> {
                            updateToDoAdapter()
                            dismiss()
                        }

                        1 -> {
                            updateInProgressAdapter()
                            dismiss()
                        }

                        2 -> {
                            updateDoneAdapter()
                            dismiss()
                        }
                    }
                }
            }
        }
    }

    /**
     * Uploading note to the database. This must be called after getting he parameters from the view
     *
     * @param title: EditText
     * @param description: EditText
     * @param comment: EditText
     */
    private fun uploadNote(title: EditText, description: EditText, comment: EditText){

        val noteQuery = ParseQuery.getQuery(NoteItem::class.java)
        noteQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        noteQuery.whereEqualTo("title",currentNote)

        try {
            val noteItem = noteQuery.first
            noteItem.setTitle("${title.text}")
            noteItem.setDescription("${description.text}")
            noteItem.setComment("${comment.text}")
            noteItem.save()

        }catch (e: ParseException){
            e.printStackTrace()
        }
    }

    /**
     * Updating ToDo page adapter with the new items
     */
    private fun updateToDoAdapter(){
        val boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", currentBoard)
        boardQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

        boardQuery.getFirstInBackground{boardItem, e ->
            if(boardItem != null){
                val notesQuery = ParseQuery.getQuery(NoteItem::class.java)
                notesQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
                notesQuery.whereEqualTo("parentBoard", boardItem)
                notesQuery.whereEqualTo("state","todo")
                notesQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

                notesQuery.findInBackground{notesList, a ->
                    if(a == null){
                        val layoutManager = LinearLayoutManager(activity)
                        val adapter = NotesAdapter(ArrayList(notesList))

                        FragmentToDo.recyclerView!!.adapter = adapter
                        FragmentToDo.recyclerView!!.layoutManager = layoutManager

                        FragmentToDo.recyclerView!!.adapter!!.notifyDataSetChanged()

                    }else{
                        Util.showToast(activity, a.message.toString())
                        a.printStackTrace()
                    }

                }
            }
        }
    }

    /**
     * Updating InProgress page adapter with the new items
     */
    private fun updateInProgressAdapter(){
        val boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", currentBoard)
        boardQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

        boardQuery.getFirstInBackground{boardItem, e ->
            if(boardItem != null){
                val notesQuery = ParseQuery.getQuery(NoteItem::class.java)
                notesQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
                notesQuery.whereEqualTo("parentBoard", boardItem)
                notesQuery.whereEqualTo("state","inprogress")
                notesQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)

                notesQuery.findInBackground{notesList, a ->
                    if(a == null){
                        val layoutManager = LinearLayoutManager(activity)
                        val adapter = NotesAdapter(ArrayList(notesList))

                        FragmentInProgress.recyclerView!!.adapter = adapter
                        FragmentInProgress.recyclerView!!.layoutManager = layoutManager

                        FragmentInProgress.recyclerView!!.adapter!!.notifyDataSetChanged()
                    }else{
                        Util.showToast(activity, a.message.toString())
                        a.printStackTrace()
                    }

                }
            }
        }
    }

    /**
     * Updating Done page adapter with the new items
     */
    private fun updateDoneAdapter(){
        val boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", currentBoard)
        boardQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK)

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

                        FragmentDone.recyclerView!!.adapter!!.notifyDataSetChanged()
                    }else{
                        Util.showToast(activity, a.message.toString())
                        a.printStackTrace()
                    }

                }
            }
        }
    }
}

