package com.jessejojojohnson.reminderly.domain

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/*
* How Reminders work:
*
* */

class ReminderNotificationWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        return Result.failure()
    }

    companion object {
        const val TAG = "remindernotificationworker"
    }
}