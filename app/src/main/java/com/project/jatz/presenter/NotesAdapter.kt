package com.project.jatz.presenter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.project.jatz.R
import com.project.jatz.model.NoteItem
import com.project.jatz.view.activities.NotesActivity

/**
 *
 */
class NotesAdapter(val itemList: ArrayList<NoteItem>, val clickListener: (NoteItem) -> Unit = { noteItem : NoteItem-> NotesActivity.clickedNote(noteItem) }): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>(){

    private var removedItemPosition: Int = 0
    private var removedItem: NoteItem? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)

        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        var currentItem: NoteItem = itemList.get(position)

        holder.title.text = currentItem.getTitle()
        holder.description.text = currentItem.getDescription()
        holder.bind(itemList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
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

    class  NoteViewHolder (var rowView: View): RecyclerView.ViewHolder(rowView){
        var title: TextView = rowView.findViewById(R.id.noteitem_title_textview)
        var description: TextView = rowView.findViewById(R.id.noteitem_description_textview)

       fun bind (noteItem: NoteItem, listener: (NoteItem) -> Unit) = with(rowView) {
           setOnClickListener { listener(noteItem) }
       }

    }

}
