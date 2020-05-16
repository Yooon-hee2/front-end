package com.example.Capstone.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class NotificationJobFireBaseService : JobService() {
    override fun onStartJob(job: JobParameters): Boolean {
        Log.d("NotificationJobService", "onStartJob")
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("id", 111)
        val pendingIntent = PendingIntent.getBroadcast(this, 111, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val intent2 = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("id", 121)
        val pendingIntent2 = PendingIntent.getBroadcast(this, 121, intent2, PendingIntent.FLAG_UPDATE_CURRENT)

        val intent3 = Intent(this, AlarmBroadcastReceiver::class.java)
        val pendingIntent3 = PendingIntent.getBroadcast(this, 131, intent3, PendingIntent.FLAG_UPDATE_CURRENT)
        /**
         * Intent 플래그
         * FLAG_ONE_SHOT : 한번만 사용하고 다음에 이 PendingIntent가 불려지면 Fail을 함
         * FLAG_NO_CREATE : PendingIntent를 생성하지 않음. PendingIntent가 실행중인것을 체크를 함
         * FLAG_CANCEL_CURRENT : 실행중인 PendingIntent가 있다면 기존 인텐트를 취소하고 새로만듬
         * FLAG_UPDATE_CURRENT : 실행중인 PendingIntent가 있다면  Extra Data만 교체함
         */

        manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 2 * 60 * 60 * 1000, pendingIntent) //2시간 간격
        manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() +  7 * 24 * 60 * 60 * 1000, pendingIntent2) // 1주일 간격
        manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3 * 24 * 60 * 60 * 1000, pendingIntent3) // 3일 간격
        return false // Answers the question: "Is there still work going on?"
    }

    override fun onStopJob(job: JobParameters): Boolean {
        return false // Answers the question: "Should this job be retried?"
    }
}