package com.project.jatz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomNavigationSheetFragment: BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var boardList = ArrayList<BoardItem>()
        boardList.add(BoardItem("Título1"))
        boardList.add(BoardItem("Título2"))
        boardList.add(BoardItem("Título3"))
        boardList.add(BoardItem("Título4"))


        val rootView = inflater.inflate(R.layout.fragment_bottomsheet, container, false)

        val recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView

        var layoutManager = LinearLayoutManager(activity)
        var boardAdapter = ThirdAdapter(boardList)

        recyclerView.adapter = boardAdapter
        recyclerView.layoutManager = layoutManager

        return rootView
    }
}