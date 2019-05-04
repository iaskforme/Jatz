package com.project.jatz.view.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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

/**
 * Class that inherits from BottomSheetDialog and contains the parameters that allow the future creation of one
 */
class BottomNavigationSheetFragment: BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material)

        var boardList = ArrayList<BoardItem>()
        boardList.add(BoardItem("Título1"))
        boardList.add(BoardItem("Título2"))
        boardList.add(BoardItem("Título3"))
        boardList.add(BoardItem("Título4"))
        boardList.add(BoardItem("Título5"))


        val rootView = inflater.inflate(R.layout.fragment_bottomsheet, container, false)

        var newBoard = rootView.findViewById<RelativeLayout>(R.id.fragmentbottomsheet_newboard_layout)

        val recyclerView = rootView.findViewById(R.id.fragmentbottomsheet_items_recyclerview) as RecyclerView

        var layoutManager = LinearLayoutManager(activity)
        var boardAdapter = ThirdAdapter(boardList)

        recyclerView.adapter = boardAdapter
        recyclerView.layoutManager = layoutManager

        newBoard.setOnClickListener{ rootView

            val dialog = CreateBoardFragment()
            dialog.show(fragmentManager, dialog.tag)
        }

        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

}