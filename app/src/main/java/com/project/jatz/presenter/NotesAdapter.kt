package com.project.jatz.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.project.jatz.R
import com.project.jatz.model.NoteItem

class NotesAdapter(val itemList: ArrayList<NoteItem>): RecyclerView.Adapter<CustomViewHolder>(){

    private var removedItemPosition: Int = 0
    private var removedItem: NoteItem? = null

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var cellRow = layoutInflater.inflate(R.layout.note_item, parent, false)

        return CustomViewHolder(cellRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        var currentItem: NoteItem = itemList.get(position)

        holder.title.text = currentItem.getTitle()
        holder.description.text = currentItem.getDescription()
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder){
        removedItemPosition = viewHolder.adapterPosition
        removedItem = itemList[viewHolder.adapterPosition]

        itemList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)


        Snackbar.make(viewHolder.itemView,"${removedItem!!.getTitle().toString()} removed", Snackbar.LENGTH_LONG).setAction("UNDO"){
            itemList.add(removedItemPosition, removedItem!!)
            notifyItemInserted(removedItemPosition)
        }.show()
    }

}

//Create it cause the adapter needs it
class  CustomViewHolder (v: View): RecyclerView.ViewHolder(v){
    var title: TextView = v.findViewById(R.id.noteitem_title_textview)
    var description: TextView = v.findViewById(R.id.noteitem_description_textview)

}