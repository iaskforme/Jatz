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
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import com.project.jatz.R
import com.project.jatz.model.BoardItem
import com.project.jatz.presenter.BoardsAdapter
import com.project.jatz.utils.Util
import com.project.jatz.view.activities.BoardsActivity
import kotlinx.android.synthetic.main.activity_boards.*

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

        var boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())
        boardQuery.whereEqualTo("title", boardEditText.text.toString())

        boardQuery.findInBackground{list, e ->
            if(list.size > 0){
                boardEditText.setError("There is a board with this name")
            }else{
                if(boardEditText.text.toString().isEmpty()){
                    boardEditText.setError("Missing board's name")
                }else{
                    uploadBoard(boardEditText)
                    dismiss()
                }
            }
        }
    }

    /**
     * Uploads board to the data base and updates
     * @param boardEditText: EditText containing board title
     */
    private fun uploadBoard(boardEditText: EditText){

        var board = BoardItem()
        board.setTitle(boardEditText.text.toString())
        board.setUser(ParseUser.getCurrentUser())

        board.save()

        var boardQuery = ParseQuery.getQuery(BoardItem::class.java)
        boardQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser())

        try {
            var boardList = ArrayList(boardQuery.find())
            updateAdapter(boardList)

        } catch (e: ParseException) {
            Util.showToast(activity, e.message.toString())
            e.printStackTrace()
        }
    }

    /**
     * Updating adapter with the board list passed as a parameter
     * @param boardList: ArrayList containing Board Items
     */
    private fun updateAdapter(boardList: ArrayList<BoardItem>){
        activity!!.boards_recycler_view.adapter = BoardsAdapter(boardList, { boardItem: BoardItem -> BoardsActivity.clickedBoard(boardItem) })
        activity!!.boards_recycler_view.adapter!!.notifyDataSetChanged()
    }
}
