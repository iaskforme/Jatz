package com.project.jatz.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.project.jatz.R
import com.project.jatz.model.NoteItem

// Binding betwen items and recyclerView
class MainAdapter(val itemList: ArrayList<NoteItem>): RecyclerView.Adapter<CustomViewHolder>(){

    private var removedItemPosition: Int = 0
    private var removedItem: NoteItem? = null

    override fun getItemCount(): Int {
        return itemList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var cellRow = layoutInflater.inflate(R.layout.note_item, parent, false)

        return CustomViewHolder(cellRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        var currentItem: NoteItem = itemList!!.get(position)

        holder.text1.text = currentItem.textLineOne
        holder.text2.text = currentItem.textLineTwo
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder){
        removedItemPosition = viewHolder.adapterPosition
        removedItem = itemList[viewHolder.adapterPosition]

        itemList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)

        Snackbar.make(viewHolder.itemView,"${removedItem!!.textLineOne.toString()} removed", Snackbar.LENGTH_LONG).setAction("UNDO"){
            itemList.add(removedItemPosition, removedItem!!)
            notifyItemInserted(removedItemPosition)
        }.show()
    }

    fun addItem(){
        itemList.add(NoteItem("Hola", "Holita"))
        notifyDataSetChanged()
    }
}

//Create it cause the adapter needs it
class  CustomViewHolder (v: View): RecyclerView.ViewHolder(v){
    var text1: TextView = v.findViewById(R.id.lineOne)
    var text2: TextView = v.findViewById(R.id.lineTwo)

}