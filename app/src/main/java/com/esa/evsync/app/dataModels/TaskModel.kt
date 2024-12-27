package com.esa.evsync.app.dataModels
import java.util.Date

data class TaskModel(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var priority: TaskPriority? = null,
    var deadline: Date? = null,
    var assigned: ArrayList<UserModel>? = null,
    var complete: Boolean? = null
)