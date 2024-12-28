package com.esa.evsync.app.pages.MemberSearch

import android.os.Parcel
import android.os.Parcelable

data class MemberResultModel(
    val id: String,
    val name: String,
    val email: String,
    val profileImageUrl: String?,
    var isSelected: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(profileImageUrl)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MemberResultModel> {
        override fun createFromParcel(parcel: Parcel): MemberResultModel {
            return MemberResultModel(parcel)
        }

        override fun newArray(size: Int): Array<MemberResultModel?> {
            return arrayOfNulls(size)
        }
    }
}
