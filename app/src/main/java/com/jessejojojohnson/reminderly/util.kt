package com.jessejojojohnson.reminderly

import android.util.Log
import java.util.UUID

private const val TAG = "ReminderlyLog"

fun ld(message: String) {
    Log.d(TAG, message)
}

fun randomId(): String = UUID.randomUUID().toString()