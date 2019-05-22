package com.project.jatz.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.jatz.R
import com.project.jatz.model.BoardItem
import kotlinx.android.synthetic.main.board_item.view.*

class BoardsAdapter(private val boardList: List<BoardItem>, val clickListener: (BoardItem) -> Unit) : RecyclerView.Adapter<BoardsAdapter.BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.board_item, parent, false)

        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val boardItem = boardList[position]

        //Binding the title
        holder.rowView.boarditem_textview.text = boardItem.getTitle()
        //Binding the clickListener
        holder.bind(boardList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return boardList.size
    }


    class BoardViewHolder(var rowView: View) : RecyclerView.ViewHolder(rowView){

        fun bind(boardItem: BoardItem, clickListener: (BoardItem) -> Unit) {
            rowView.setOnClickListener { clickListener(boardItem) }
        }
    }

}