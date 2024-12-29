package com.esa.evsync.app.pages.UserPicker

data class UserPickerDataModel(
    val id: String,
    val name: String,
    val email: String,
    val profileImageUrl: String?,
    var isSelected: Boolean
)