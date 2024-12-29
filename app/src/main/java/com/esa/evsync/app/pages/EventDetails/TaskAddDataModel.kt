package com.esa.evsync.app.pages.EventDetails

import android.os.Parcel
import android.os.Parcelable

data class TaskAddDataModel(
    val name: String,
    val description: String,
    val priority: String,
    val deadline: String,
    val assigned: ArrayList<String>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "", // Handle null safety
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: ArrayList() // Deserialize ArrayList<String>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(priority)
        parcel.writeString(deadline)
        parcel.writeStringList(assigned) // Serialize ArrayList<String>
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskAddDataModel> {
        override fun createFromParcel(parcel: Parcel): TaskAddDataModel {
            return TaskAddDataModel(parcel)
        }

        override fun newArray(size: Int): Array<TaskAddDataModel?> {
            return arrayOfNulls(size)
        }
    }
}
