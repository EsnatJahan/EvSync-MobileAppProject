package com.esa.evsync.app.dataModels

import android.net.Uri

data class EventModel(
    var id: String? = null,
    var name: String? = null,
    var owner: String? = null,
    var members: ArrayList<String>? = null,
    var description: String? = null,
    var image: Uri? = null,
    var tasks: ArrayList<TaskModel>? = null,
)
