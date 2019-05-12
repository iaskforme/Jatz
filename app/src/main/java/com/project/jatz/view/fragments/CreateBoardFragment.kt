package com.project.jatz.view.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.project.jatz.model.BoardList
import com.project.jatz.model.NoteItem
import com.project.jatz.model.NoteList
import kotlinx.android.synthetic.main.fragment_create_board.*


/**
 * Class that inherits from DialogFragment and contains the parameters that allow the future creation of one.In this case for the creation of a board.
 */
class CreateBoardFragment: DialogFragment() {

    var boardList: BoardList = BoardList(ArrayList<BoardItem>())
    var boardItem = BoardItem("default", NoteList(ArrayList<NoteItem>(),ArrayList<NoteItem>(),ArrayList<NoteItem>()))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material)

        val rootView = inflater.inflate(R.layout.fragment_create_board, container, false)
        val saveText: TextView = rootView.findViewById(R.id.fragmentboard_save_textview)
        val cancelText: TextView = rootView.findViewById(R.id.fragmentboard_cancel_textview)
        val boardEditText: EditText = rootView.findViewById(R.id.fragmentboard_boardname_edittext)

        var bundleComing = this.arguments

        if (bundleComing != null) {
            boardList = bundleComing.getParcelable("boardList")
        }

        saveText.setOnClickListener {
            createBoard(boardEditText)
        }

        cancelText.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    fun createBoard(boardEditText: EditText){

        if (!validate(boardEditText)) {
            onCreateBoardFailed()
            return
        }

        boardItem.title = fragmentboard_boardname_edittext.text.toString()
        boardList.list.add(boardItem)

        BottomNavigationSheetFragment.recyclerView!!.adapter!!.notifyDataSetChanged()
        dismiss()
    }

    fun onCreateBoardFailed() {
        Toast.makeText(context, "Creation failed", Toast.LENGTH_LONG).show()
    }

    fun validate(boardEditText: EditText): Boolean {
        var valid = true

        if(boardEditText.text.toString().isEmpty()){
            boardEditText.setError("Fill in the field")
            valid = false
        }else{
            valid = true
        }

        return valid
    }
}
