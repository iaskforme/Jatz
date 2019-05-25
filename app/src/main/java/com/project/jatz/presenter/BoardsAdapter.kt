package com.project.jatz.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.jatz.R
import com.project.jatz.model.BoardItem
import com.project.jatz.view.activities.BoardsActivity

/**
 *
 * Class in charge of bridging View and Data for the Boards view
 *
 * @param list: BoardItem list
 * @param Listener: OnClickListener for Board items
 */
class BoardsAdapter(private val boardList: ArrayList<BoardItem>, val clickListener: (BoardItem) -> Unit = { boardItem : BoardItem ->BoardsActivity.clickedBoard(boardItem)}) : RecyclerView.Adapter<BoardsAdapter.BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.board_item, parent, false)

        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val currentItem: BoardItem = boardList.get(position)

        holder.title.text = currentItem.getTitle()
        holder.bind(boardList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return boardList.size
    }


    class BoardViewHolder(var rowView: View) : RecyclerView.ViewHolder(rowView){
        val title: TextView = rowView.findViewById(R.id.boarditem_textview)

        fun bind(boardItem: BoardItem, listener: (BoardItem) -> Unit) = with(rowView) {
            setOnClickListener { listener(boardItem) }
        }
    }

}