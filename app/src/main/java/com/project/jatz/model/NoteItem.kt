package com.project.jatz.model

import android.os.Parcel
import android.os.Parcelable

data class NoteItem(var title: String, var description: String, var comment: String): Parcelable{

    constructor(parcel: Parcel) : this(
        title = parcel.readString(),
        description = parcel.readString(),
        comment = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(comment)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoteItem> {
        override fun createFromParcel(parcel: Parcel): NoteItem {
            return NoteItem(parcel)
        }

        override fun newArray(size: Int): Array<NoteItem?> {
            return arrayOfNulls(size)
        }
    }

}
