package com.esa.evsync.app.pages.UserPicker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.esa.evsync.app.dataModels.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserPickerViewModel(val userIDs: ArrayList<String>): ViewModel() {
    private var _users = MutableLiveData<ArrayList<UserPickerDataModel>>()
    val users: LiveData<ArrayList<UserPickerDataModel>> get() = _users
    var markedResults = mutableListOf<UserPickerDataModel>() // A list to hold selected items

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun showToastMessage(msg: String) {
        _toastMessage.postValue(msg)
    }

    fun fetchUserInfo() {
        try {
            viewModelScope.launch(Dispatchers.Main) {
                val usersList = ArrayList(userIDs.map { userID ->
                    async(Dispatchers.IO) {
                        Firebase.firestore.collection("users").document(userID).get().await()
                    }
                }.awaitAll()
                    .map {userSnapshot ->
                        val userData = userSnapshot.toObject(UserModel::class.java)!!
                        UserPickerDataModel(
                            id = userSnapshot.id,
                            name = userData.username ?: "Name",
                            email = userData.email ?: "email@address.com",
                            profileImageUrl = userData.profile_picture,
                            isSelected =  false
                        )
                    })
                _users.postValue(usersList)
            }
        } catch (e: Error) {
            showToastMessage("Failed to fetch event info")
            Log.e("Firebase", "Failed to fetch event info", e)
        }
    }

    companion object {
        // Factory method to create a ViewModel instance
        fun Factory(userIDs: ArrayList<String>): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(UserPickerViewModel::class.java)) {
                        return UserPickerViewModel(userIDs) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}