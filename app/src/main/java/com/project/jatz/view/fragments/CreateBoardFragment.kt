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
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import com.project.jatz.R
import com.project.jatz.model.BoardItem
import com.project.jatz.model.NoteItem
import com.project.jatz.presenter.BoardsAdapter
import com.project.jatz.presenter.NotesAdapter
import com.project.jatz.utils.Util
import com.project.jatz.view.activities.NotesActivity
import kotlinx.android.synthetic.main.activity_boards.*
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Class that inherits from DialogFragment and contains the parameters that allow the future creation of one.In this case for the creation of a board.
 */
class CreateBoardFragment: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material)

        val rootView = inflater.inflate(R.layout.fragment_create_board, container, false)
        val saveText: TextView = rootView.findViewById(R.id.fragmentboard_save_textview)
        val cancelText: TextView = rootView.findViewById(R.id.fragmentboard_cancel_textview)
        val boardEditText: EditText = rootView.findViewById(R.id.fragmentboard_boardname_edittext)

        saveText.setOnClickListener {
            createBoard(boardEditText)
        }

        cancelText.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    /**
     * Function that contains the creation of a new board. Big terms, inner function calls
     * @param boardEditText: EditText containing board title
     */
    private fun createBoard(boardEditText: EditText){

        val boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", boardEditText.text.toString())

        try {
            if(boardQuery.find().size > 0){
                boardEditText.setError("There is a board with this name")
            }else{
                if (boardEditText.text.toString().isEmpty()){
                    boardEditText.setError("Missing board's name")
                }else{

                    uploadBoard(boardEditText)

                    val boardsQuery = ParseQuery.getQuery(BoardItem::class.java)
                    boardsQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE)
                    boardsQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())

                    try {
                        val boardList = ArrayList(boardsQuery.find())

                        if(activity!!.boards_empty_layout.visibility == View.VISIBLE){
                            activity!!.boards_empty_layout.visibility = View.GONE
                            activity!!.boards_recycler_view.visibility = View.VISIBLE
                        }

                        updateAdapter(boardList)
                        dismiss()
                    }catch (a: ParseException){
                        a.printStackTrace()
                    }
                }
            }
        }catch (e: ParseException){
            e.printStackTrace()
        }
    }

    /**
     * Uploads board to the data base and updates
     * @param boardEditText: EditText containing board title
     */
    private fun uploadBoard(boardEditText: EditText){

        val board = BoardItem()
        board.setTitle(boardEditText.text.toString())
        board.setUser(ParseUser.getCurrentUser())

        board.save()
    }

    /**
     * Updating adapter with the board list passed as a parameter
     * @param boardList: ArrayList containing Board Items
     */
    private fun updateAdapter(boardList: ArrayList<BoardItem>){
        val layoutManager = LinearLayoutManager(activity)
        val adapter = BoardsAdapter(boardList)

        activity!!.boards_recycler_view.adapter = adapter
        activity!!.boards_recycler_view.layoutManager = layoutManager

        activity!!.boards_recycler_view.adapter!!.notifyDataSetChanged()
    }
}
