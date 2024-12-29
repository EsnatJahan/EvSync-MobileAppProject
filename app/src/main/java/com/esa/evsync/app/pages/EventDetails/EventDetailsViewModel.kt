package com.esa.evsync.app.pages.EventDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.esa.evsync.app.dataModels.EventModel
import com.esa.evsync.app.dataModels.TaskModel
import com.esa.evsync.app.dataModels.UserModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EventDetailsViewModel(val eventId: String): ViewModel() {
    private var db = Firebase.firestore

    private val _eventName = MutableLiveData<String>()
    private val _eventDescriptor = MutableLiveData<String>()
    private val _event = MutableLiveData<EventModel>()
    private val _tasks = MutableLiveData<ArrayList<TaskModel>>()
    private val _members = MutableLiveData<ArrayList<UserModel>>()
    private val _toastMessage = MutableLiveData<String>()

    val tasks: LiveData<ArrayList<TaskModel>> get() = _tasks
    val members: LiveData<ArrayList<UserModel>> get() = _members
    val event: LiveData<EventModel> get() = _event
    val eventName: LiveData<String> get() = _eventName
    val eventDescriptor: LiveData<String> get() = _eventDescriptor
    val toastMessage: LiveData<String> get() = _toastMessage

    private fun showToast(message: String) {
        _toastMessage.postValue(message)
    }

    fun fetchEventInfo(onComplete: ()->Unit = {
        fetchTasks()
        fetchMembers()

    }) {
        viewModelScope.launch(Dispatchers.Main) {
            val ref = withContext(Dispatchers.IO) {
                db.collection("events").document(eventId).get().await()
            }
            val newEvent = ref.toObject(EventModel::class.java)!!
            newEvent.id = ref.id
            _event.value = newEvent
            _eventName.value = newEvent.name!!
            _eventDescriptor.value = newEvent.description!!
            onComplete()
        }
    }

    fun fetchTasks() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                Log.d("datafetch", "Fetching task data")
                val newTasks = arrayListOf(*(event.value?.tasks!!.map { taskRef ->
                    async(Dispatchers.IO) {taskRef.get().await()}
                }
                    .awaitAll()
                    .map {
                        val task = it.toObject(TaskModel::class.java)!!
                        task.id = it.id
                        task
                    }).toTypedArray())
                _tasks.postValue(newTasks)
            } catch (e: Error) {
                Log.e("Firebase", "Failed to fetch task info", e)
                showToast("Failed to load tasks")
            }
        }
    }

    fun fetchMembers() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                Log.d("datafetch", "Fetching member data with event:\n $event")
                val newMembers = arrayListOf(*(event.value?.members!!.map {memberRef ->
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

    fun removeMember(id: String, onComplete: () -> Unit = {}) {
        if (event.value == null) {
            Log.e("remove member", "Failed to remove member. Event data now found")
            showToast("Failed to remove member")
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = Firebase.firestore
                val memberRef = db.collection("users").document(id)
                if (event.value?.owner == memberRef) {
                    showToast("Can't remove the owner")
                    Log.d("member remove", "Can't remove owner")
                    return@launch
                }
                Log.d("member remove", "removing member")
                db.collection("events").document(event.value!!.id!!).update("members", FieldValue.arrayRemove(memberRef)).await()
                withContext(Dispatchers.Main) { onComplete() }
                fetchEventInfo(::fetchMembers)
            } catch (e: Error) {
                showToast("Failed to remove user")
            }
        }
    }

    fun addMembers(memberIds: ArrayList<String>?) {
        if (memberIds.isNullOrEmpty()) return
        // add members
        val userRefs = memberIds.map { db.collection("users").document(it) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("events").document(eventId)
                    .update("members", FieldValue.arrayUnion(*userRefs.toTypedArray())).await()
                fetchEventInfo(::fetchMembers)
            } catch (e: Error) {
                Log.e("Firebase", "failed to fetch updated info", e)
                showToast("Failed to add members")
            }

        }
    }

    companion object {
        // Factory method to create a ViewModel instance
        fun Factory(eventId: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(EventDetailsViewModel::class.java)) {
                        return EventDetailsViewModel(eventId) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}