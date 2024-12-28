package com.esa.evsync.app.pages.EventDetails

import com.esa.evsync.app.dataModels.EventModel

interface EventDetailsUpdatable  {
    fun updateEvent(event: EventModel)
}
