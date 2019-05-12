package com.project.jatz.view.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.project.jatz.R
import com.project.jatz.model.BoardItem
import com.project.jatz.model.NoteItem
import com.project.jatz.model.NoteList

/**
 * Class that inherits from DialogFragment and contains the parameters that allow the future creation of one. In this case for the creation of a note.
 */
class CreateNoteFragment() : DialogFragment(){

    var index: Int? = null
    var todoList = ArrayList<NoteItem>()
    var progressList = ArrayList<NoteItem>()
    var doneList = ArrayList<NoteItem>()
    var boardItem = BoardItem("default", NoteList(ArrayList<NoteItem>(),ArrayList<NoteItem>(),ArrayList<NoteItem>()))
    var noteList = NoteList(todoList, progressList, doneList)
    var bundleComing = this.arguments


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

        if (bundleComing != null) {
            boardItem = bundleComing!!.getParcelable("boardItem")
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

        index = arguments!!.getInt("fragment")
    }

    /**
     * Create note: validation and calling fail if it fails in some way
     */
    fun createNote(title: EditText, description:EditText, comment: EditText, fragmentIndex: Int?){

        if (!validate(title, description, comment)) {
            onCreateNoteFailed()
            return
        }

        //Here should be the creation or addition to the list
        when(fragmentIndex){
            0 -> {
                todoList.add(NoteItem(title.text.toString(),description.text.toString(), comment.text.toString()))
                //var bundle = Bundle()
                //bundle.putParcelableArrayList("todo",noteList.noteTodoList)
                FragmentOne.recyclerView!!.adapter!!.notifyDataSetChanged()
                dismiss()
            }

            1 -> {
                progressList.add(NoteItem(title.text.toString(),description.text.toString(), comment.text.toString()))
                FragmentTwo.recyclerView!!.adapter!!.notifyDataSetChanged()
                dismiss()
            }

            2 -> {
                doneList.add(NoteItem(title.text.toString(),description.text.toString(), comment.text.toString()))
                FragmentThree.recyclerView!!.adapter!!.notifyDataSetChanged()
                dismiss()
            }
        }

        boardItem.noteList = noteList

    }

    /**
     * just a toast
     */

    fun onCreateNoteFailed() {
        Toast.makeText(context, "Creation failed", Toast.LENGTH_LONG).show()
    }

    /**
     * validation for the fields
     */
    fun validate(title: EditText, description:EditText, comment: EditText): Boolean {
        var valid = true

        if(title.text.toString().isEmpty()){
            title.setError("Fill in the field")
            valid = false
        }else{
            valid = true
        }

        if(description.text.toString().isEmpty()){
            description.setError("Fill in the field")
            valid = false
        }else{
            valid = true
        }

        if(comment.text.toString().isEmpty()){
            comment.setError("Fill in the field")
            valid = false
        }else{
            valid = true
        }

        return valid
    }
}

