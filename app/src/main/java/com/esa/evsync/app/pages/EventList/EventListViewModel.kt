package com.esa.evsync.app.pages.EventList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esa.evsync.app.dataModels.EventModel
import com.esa.evsync.app.utils.documentReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EventListViewModel: ViewModel() {
    private var _events = MutableLiveData<ArrayList<EventModel>>()
    val event: LiveData<ArrayList<EventModel>> get() = _events

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private fun showToast(message: String) {
        _toastMessage.postValue(message)
    }

    fun fetchEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val eventSnapshots = Firebase.firestore.collection("events")
                    .whereArrayContains(
                        "members",
                        FirebaseAuth.getInstance().currentUser!!.documentReference
                    )
                    .get()
                    .await()
                val newEventList = ArrayList(eventSnapshots.map {
                    val event = it.toObject(EventModel::class.java)
                    event.id = it.id
                    event
                })
                _events.postValue(newEventList)
            } catch (e: Error) {
                Log.e("Firebase", "failed to load event list", e)
                showToast("Failed to load events")
            }
        }
    }

    fun addEvent(newEvent: NewEventInfo?) {
        if (newEvent == null) return

        val currentUser = FirebaseAuth.getInstance().currentUser!!
        val db = Firebase.firestore
        val event = EventModel(
            name = newEvent.name,
            description = newEvent.description,
            image = null,
            owner = currentUser.documentReference,
            members = ArrayList(listOf(currentUser.documentReference)),
            tasks = ArrayList()
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("events").add(event).await()
                fetchEvents()
                showToast("Event created")
            } catch (e: Error) {
                Log.e("Firebase", "Failed to create event", e)
                showToast("Failed to create event")
            }
        }
    }
    
}