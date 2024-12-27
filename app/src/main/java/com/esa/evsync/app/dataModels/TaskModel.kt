package com.esa.evsync.app.dataModels
import com.google.firebase.firestore.DocumentReference
import java.util.Date

data class TaskModel(
    var id: String? = null,
    var eventRef: DocumentReference? = null,
    var name: String? = null,
    var description: String? = null,
    var priority: TaskPriority? = null,
    var deadline: Date? = null,
    var assigned: ArrayList<DocumentReference>? = null,
    var complete: Boolean? = null
)