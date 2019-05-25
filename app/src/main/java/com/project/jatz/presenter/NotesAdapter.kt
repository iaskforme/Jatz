package com.project.jatz.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.jatz.R
import com.project.jatz.model.NoteItem
import com.project.jatz.view.activities.NotesActivity

/**
 *
 * Class in charge of bridging View and Data for the Notes view
 *
 * @param list: NoteItem list
 * @param Listener: OnClickListener for Note items
 */
class NotesAdapter(val itemList: ArrayList<NoteItem>, val clickListener: (NoteItem) -> Unit = { noteItem : NoteItem-> NotesActivity.clickedNote(noteItem) }): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)

        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentItem: NoteItem = itemList.get(position)

        holder.title.text = currentItem.getTitle()
        holder.description.text = currentItem.getDescription()
        holder.bind(itemList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    /**
     * Gets the note at an specific position
     *
     * @param Position: index that determines the note position
     */
    fun getNoteAt(position: Int): NoteItem{
        return itemList.get(position)
    }

    class  NoteViewHolder (var rowView: View): RecyclerView.ViewHolder(rowView){
        var title: TextView = rowView.findViewById(R.id.noteitem_title_textview)
        var description: TextView = rowView.findViewById(R.id.noteitem_description_textview)

       fun bind (noteItem: NoteItem, listener: (NoteItem) -> Unit) = with(rowView) {
           setOnClickListener { listener(noteItem) }
       }

    }

}
