package com.example.android.runningtrackerapp.other

import android.graphics.Color

object Constants {

    const val DATABASE_NAME = "running_tracker_db"

    const val LOCATION_PERMISSION_TEXT = "accept location permissions to use this app"
    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

    const val TIMER_UPDATE_INTERVAL = 50L

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val LOCATION_FASTEST_INTERVAL = 2000L

    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 15f

    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 111

    const val SHARED_PREFS = "SHARED_PREFS"
    const val KEY_FIRST_TIME = "KEY_FIRST_TIME"
    const val KEY_NAME = "KEY_NAME"
    const val KEY_WEIGHT = "KEY_WEIGHT"
}