package com.esa.evsync.app.pages.EventList

import android.os.Parcel
import android.os.Parcelable

data class NewEventInfo(
    val name: String,
    val description: String,
    val image: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewEventInfo> {
        override fun createFromParcel(parcel: Parcel): NewEventInfo {
            return NewEventInfo(parcel)
        }

        override fun newArray(size: Int): Array<NewEventInfo?> {
            return arrayOfNulls(size)
        }
    }
}
