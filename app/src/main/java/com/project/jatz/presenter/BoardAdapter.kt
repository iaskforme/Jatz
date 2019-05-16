package com.project.jatz.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.jatz.R
import com.project.jatz.model.BoardItem
import kotlinx.android.synthetic.main.board_item.view.*

class BoardAdapter(private val boardList: List<BoardItem>) : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.board_item, parent, false)

        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val boardItem = boardList[position]

        holder.rowView.boarditem_textview.text = boardItem.getTitle()
    }

    override fun getItemCount(): Int {
        return boardList.size
    }


    class BoardViewHolder(var rowView: View) : RecyclerView.ViewHolder(rowView)

}