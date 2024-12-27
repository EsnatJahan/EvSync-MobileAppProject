package com.esa.evsync.app.dataModels

import android.net.Uri

data class EventModel(
    val name: String? = null,
    val owner: String? = null,
    val members: ArrayList<String>? = null,
    val description: String? = null,
    val image: Uri? = null
)
