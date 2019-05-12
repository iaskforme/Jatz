package com.project.jatz.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.jatz.R
import com.project.jatz.presenter.ThirdAdapter
import com.project.jatz.model.BoardItem
import com.project.jatz.model.BoardList
import com.project.jatz.model.NoteItem
import com.project.jatz.model.NoteList

/**
 * Class that inherits from BottomSheetDialog and contains the parameters that allow the future creation of one
 */
class BottomNavigationSheetFragment: BottomSheetDialogFragment(){

    var boardList: BoardList = BoardList(ArrayList<BoardItem>())
    var dialogFragment =  CreateBoardFragment()
    var boardIsClicked: Boolean = false
    var bundleGoing = Bundle()
    var boardAdapter: ThirdAdapter? = null
    var currentBoardItem: BoardItem? = null

    companion object{
        var recyclerView: RecyclerView? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_bottomsheet, container, false)
        val newBoard = rootView.findViewById<RelativeLayout>(R.id.fragmentbottomsheet_newboard_layout)
        BottomNavigationSheetFragment.recyclerView = rootView.findViewById(R.id.fragmentbottomsheet_items_recyclerview) as RecyclerView

        val layoutManager = LinearLayoutManager(activity)
        boardAdapter = ThirdAdapter(boardList.list, {boardItem : BoardItem -> boardItemClicked(boardItem)})

        recyclerView!!.adapter = boardAdapter
        recyclerView!!.layoutManager = layoutManager


        newBoard.setOnClickListener {
            dialogFragment.show(fragmentManager, dialogFragment.tag)
            dialogFragment = CreateBoardFragment()
            dialogFragment.arguments = bundleGoing
        }

        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)

        bundleGoing.putParcelable("boardList", boardList)
        dialogFragment.arguments = bundleGoing
    }

    fun boardItemClicked(boardItem: BoardItem){
        boardIsClicked = true
        currentBoardItem = boardItem
    }

}