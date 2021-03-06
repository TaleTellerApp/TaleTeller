package com.example.taleteller.model

import android.os.Parcel
import android.os.Parcelable

data class User(val id: String?, val title: String?, val shortcut:String?,val content:String?,val owner:String?,val likes:String?, val resId: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(shortcut)
        parcel.writeString(content)
        parcel.writeString(owner)
        parcel.writeString(likes)
        parcel.writeInt(resId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}