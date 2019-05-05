package com.project.jatz.view.fragments

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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

/**
 * Class that inherits from DialogFragment and contains the parameters that allow the future creation of one.In this case for the creation of a board.
 */
class CreateBoardFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material)

        val rootView = inflater.inflate(R.layout.fragment_create_board, container, false)
        val saveText: TextView = rootView.findViewById(R.id.fragmentboard_save_textview)
        val cancelText: TextView = rootView.findViewById(R.id.fragmentboard_cancel_textview)
        val boardEditText: EditText = rootView.findViewById(R.id.fragmentboard_boardname_edittext)


        saveText.setOnClickListener {rootView
            createBoard(boardEditText)
        }

        cancelText.setOnClickListener {rootView
            dismiss()
        }

        return rootView
    }

    fun createBoard(boardEditText: EditText){

        if (!validate(boardEditText)) {
            onCreateBoardFailed()
            return
        }

        BoardList.list.add(BoardItem("${boardEditText.text.toString()}"))
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
