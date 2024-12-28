package com.esa.evsync.app.utils

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference

// add the document reference for current user in udder current user document so we
// don't need to fiddle around all the time

val FirebaseUser.documentReference: DocumentReference
    get() = FirebaseFirestore.getInstance()
        .collection("users")
        .document(this.uid)