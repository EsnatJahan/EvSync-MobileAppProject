package com.esa.evsync.app.pages.TaskDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.esa.evsync.app.dataModels.TaskModel
import com.esa.evsync.app.dataModels.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TaskDetailsViewModel(val taskId: String): ViewModel() {
    private var db = Firebase.firestore

    private val _task = MutableLiveData<TaskModel>()
    private val _members = MutableLiveData<ArrayList<UserModel>>()
    private val _toastMessage = MutableLiveData<String>()

    val task: LiveData<TaskModel> get() = _task
    val members: LiveData<ArrayList<UserModel>> get() = _members
    val toastMessage: LiveData<String> get() = _toastMessage

    private fun showToast(message: String) {
        _toastMessage.postValue(message)

    }

    fun fetchEventInfo(onComplete: ()->Unit = {
        fetchassigned()
    }) {
        viewModelScope.launch(Dispatchers.Main) {
            val ref = withContext(Dispatchers.IO) {
                db.collection("tasks").document(taskId).get().await()
            }
            val taskdata = ref.toObject(TaskModel::class.java)!!
            taskdata.id = ref.id
            _task.value = taskdata
            onComplete()
        }
    }

    fun fetchassigned() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                Log.d("datafetch", "Fetching member data with event:\n $task")
                val newMembers = arrayListOf(*(task.value?.assigned!!.map {memberRef ->
                    async(Dispatchers.IO) {memberRef.get().await()}
                }.awaitAll().map {
                    val member = it.toObject(UserModel::class.java)!!
                    member.id = it.id
                    member
                }).toTypedArray())
                Log.d("datafetch", "Fetching member data")
                _members.postValue(newMembers)
            } catch (e: Error) {
                Log.e("Firebase", "Failed to fetch members info", e)
                showToast("Failed to load members")
            }
        }
    }

    companion object {
        // Factory method to create a ViewModel instance
        fun Factory(taskId: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(TaskDetailsViewModel::class.java)) {
                        return TaskDetailsViewModel(taskId) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}