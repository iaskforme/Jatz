package com.project.jatz.model

import android.os.Parcel
import android.os.Parcelable

data class NoteList(var todoList: ArrayList<NoteItem>, var progressList: ArrayList<NoteItem>, var doneList: ArrayList<NoteItem>): Parcelable{

    constructor(parcel: Parcel) : this(
        todoList = arrayListOf<NoteItem>().apply {
            parcel.readArrayList(NoteItem::class.java.classLoader)
        },
        progressList = arrayListOf<NoteItem>().apply {
            parcel.readArrayList(NoteItem::class.java.classLoader)
        },
        doneList = arrayListOf<NoteItem>().apply {
            parcel.readArrayList(NoteItem::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoteList> {
        override fun createFromParcel(parcel: Parcel): NoteList {
            return NoteList(parcel)
        }

        override fun newArray(size: Int): Array<NoteList?> {
            return arrayOfNulls(size)
        }
    }

}
