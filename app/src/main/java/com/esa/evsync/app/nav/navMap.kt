package com.esa.evsync.app.nav

import com.esa.evsync.R

class NavMap {
    companion object {
        // This map is now static (shared across all instances)
        val destinationToNavItemMap = mapOf(
            R.id.nav_eventAddFragment to R.id.nav_events,
            R.id.nav_events to R.id.nav_events,
            R.id.nav_tasks to R.id.nav_tasks,
            R.id.nav_settings to R.id.nav_settings
        )
    }
}
