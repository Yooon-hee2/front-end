package com.example.Capstone.background

import android.content.Context
import com.firebase.jobdispatcher.*


object JobScheduler {
    private const val JOB_ID = 1111
    fun start(context: Context) {
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))
        val myJob: Job = dispatcher.newJobBuilder()
            .setService(NotificationJobFireBaseService::class.java) // 잡서비스 등록
            .setTag("TSLetterNotification") // 태그 등록
            .setRecurring(true) //재활용
            .setLifetime(Lifetime.FOREVER) //다시켜도 작동을 시킬껀지?
            .setTrigger(Trigger.executionWindow(0, 60)) //트리거 시간
            .setReplaceCurrent(true)
            .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
            .build()
        dispatcher.mustSchedule(myJob)
    }
}