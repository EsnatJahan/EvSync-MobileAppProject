package com.esa.evsync.app.dataModels

import android.net.Uri

data class UserModel(
    var id: String? = null,
    var username: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var profile_picture: Uri? = null,
)
