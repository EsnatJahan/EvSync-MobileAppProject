package com.esa.evsync.app.dataModels

import android.net.Uri

data class GroupModel(
    val name: String?,
    val members: ArrayList<String>?,
    val description: String?,
    val image: Uri?
)
