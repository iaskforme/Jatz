package com.project.jatz.model

import android.os.Parcel
import android.os.Parcelable

data class BoardItem(var title: String, var noteList: NoteList): Parcelable{


    private constructor(parcel: Parcel) : this(
        title = parcel.readString(),
        noteList = parcel.readParcelable<NoteList>(NoteList::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BoardItem> {
        override fun createFromParcel(parcel: Parcel): BoardItem {
            return BoardItem(parcel)
        }

        override fun newArray(size: Int): Array<BoardItem?> {
            return arrayOfNulls(size)
        }
    }

}