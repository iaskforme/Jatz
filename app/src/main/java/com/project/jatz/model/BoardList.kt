package com.project.jatz.model

import android.os.Parcel
import android.os.Parcelable

data class BoardList (var list: ArrayList<BoardItem>): Parcelable{

    private constructor(parcel: Parcel): this(

        list = arrayListOf<BoardItem>().apply {
            parcel.readArrayList(BoardItem::class.java.classLoader)
        })

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeList(list)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BoardList> {
        override fun createFromParcel(parcel: Parcel): BoardList {
            return BoardList(parcel)
        }

        override fun newArray(size: Int): Array<BoardList?> {
            return arrayOfNulls(size)
        }
    }

}