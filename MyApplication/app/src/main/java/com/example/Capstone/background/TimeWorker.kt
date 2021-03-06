package com.example.Capstone.background

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters

class TimeWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val intent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
        intent.putExtra("id", 111)
        applicationContext.sendBroadcast(intent)

        return Result.success()
    }

}