package com.project.jatz

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

// Binding betwen items and recyclerView
class ThirdAdapter(val itemList: ArrayList<BoardItem>): RecyclerView.Adapter<CustomViewHolderDrawer>(){

    private var removedItemPosition: Int = 0
    private var removedItem: BoardItem? = null

    override fun getItemCount(): Int {
        return itemList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderDrawer {
        val layoutInflater = LayoutInflater.from(parent.context)
        var cellRow = layoutInflater.inflate(R.layout.drawer_item, parent, false)

        return CustomViewHolderDrawer(cellRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolderDrawer, position: Int) {

        var currentItem: BoardItem = itemList!!.get(position)

        holder.text1.text = currentItem.textLineOne
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder){
        removedItemPosition = viewHolder.adapterPosition
        removedItem = itemList[viewHolder.adapterPosition]

        itemList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
    }

    fun addItem(){
        itemList.add(BoardItem("Hola"))
        notifyDataSetChanged()
    }
}

class CustomViewHolderDrawer(v: View): RecyclerView.ViewHolder(v){
    var text1: TextView = v.findViewById(R.id.board_title)
}