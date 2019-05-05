package com.project.jatz.view.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.jatz.R
import com.project.jatz.presenter.ThirdAdapter
import com.project.jatz.model.BoardItem
import com.project.jatz.model.BoardList

/**
 * Class that inherits from BottomSheetDialog and contains the parameters that allow the future creation of one
 */
class BottomNavigationSheetFragment: BottomSheetDialogFragment(){

    companion object{
        var recyclerView: RecyclerView? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material)

        var boardList = BoardList.list
        boardList.add(BoardItem("Default Board"))

        val rootView = inflater.inflate(R.layout.fragment_bottomsheet, container, false)

        val newBoard = rootView.findViewById<RelativeLayout>(R.id.fragmentbottomsheet_newboard_layout)
        BottomNavigationSheetFragment.recyclerView = rootView.findViewById(R.id.fragmentbottomsheet_items_recyclerview) as RecyclerView

        var layoutManager = LinearLayoutManager(activity)
        var boardAdapter = ThirdAdapter(boardList)

        recyclerView!!.adapter = boardAdapter
        recyclerView!!.layoutManager = layoutManager

        newBoard.setOnClickListener {

            val dialog = CreateBoardFragment()
            dialog.show(fragmentManager, dialog.tag)

            Log.e("LIST", boardAdapter.itemCount.toString())
        }

        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

}