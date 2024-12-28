package com.esa.evsync.app.dataModels

import android.net.Uri
import com.google.firebase.firestore.DocumentReference

data class EventModel(
    var id: String? = null,
    var name: String? = null,
    var owner: DocumentReference? = null,
    var members: ArrayList<DocumentReference>? = null,
    var description: String? = null,
    var image: Uri? = null,
    var tasks: ArrayList<DocumentReference>? = null,
)
