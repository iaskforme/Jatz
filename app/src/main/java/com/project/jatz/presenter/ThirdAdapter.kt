package com.project.jatz.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.jatz.R
import com.project.jatz.model.BoardItem

// Binding betwen items and recyclerView
class ThirdAdapter(var itemList: ArrayList<BoardItem>, val clickListener: (BoardItem) -> Unit): RecyclerView.Adapter<CustomViewHolderDrawer>(){

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderDrawer {
        val layoutInflater = LayoutInflater.from(parent.context)
        var cellRow = layoutInflater.inflate(R.layout.drawer_item, parent, false)

        return CustomViewHolderDrawer(cellRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolderDrawer, position: Int) {
        var currentItem: BoardItem = itemList.get(position)
        holder.itemText.text = currentItem.title

        holder.bind(itemList[position], clickListener)
    }

}

class CustomViewHolderDrawer(var v: View): RecyclerView.ViewHolder(v){
    var itemLayout: RelativeLayout = v.findViewById(R.id.draweritem_layout)
    var itemText: TextView = v.findViewById(R.id.draweritem_textview)

    fun bind(boardItem: BoardItem, clickListener: (BoardItem) -> Unit) {
        v.setOnClickListener { clickListener(boardItem) }
    }
}





