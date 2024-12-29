package com.esa.evsync.app.pages.MemberSearch

import android.os.Parcel
import android.os.Parcelable

data class MemberResultModel(
    val id: String,
    val name: String,
    val email: String,
    val profileImageUrl: String?,
    var isSelected: Boolean
)