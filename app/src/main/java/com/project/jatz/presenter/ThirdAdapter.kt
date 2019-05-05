package com.project.jatz.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.jatz.R
import com.project.jatz.model.BoardItem

// Binding betwen items and recyclerView
class ThirdAdapter(var itemList: ArrayList<BoardItem>): RecyclerView.Adapter<CustomViewHolderDrawer>(){

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
        holder.text1.text = currentItem.boardTitle
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder){
        removedItemPosition = viewHolder.adapterPosition
        removedItem = itemList[viewHolder.adapterPosition]

        itemList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
    }

    fun addItem(){
        itemList.add(BoardItem("Hola"))
    }

}

class CustomViewHolderDrawer(v: View): RecyclerView.ViewHolder(v){
    var text1: TextView = v.findViewById(R.id.draweritem_textview)
}