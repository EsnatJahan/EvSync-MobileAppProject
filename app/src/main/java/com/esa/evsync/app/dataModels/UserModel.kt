package com.esa.evsync.app.dataModels

import android.net.Uri

data class UserModel(
    var username: String?,
    var email: String?,
    var phone: String?,
    var profile_picture: Uri?,
)
