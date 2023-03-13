package com.application.alphacapital.superapp.vault.services

import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.application.alphacapital.superapp.pms.jobservice.FirstJobService

class ServiceUtils
{
    companion object
    {
        private const val TAG = "SplashActivity"
        private const val SUCCESS_KEY = "SUCCESS"
        private const val FAILED_KEY = "FAILED"
        private const val JOB_ID = 123
        private const val PERIODIC_TIME: Long = 1 * 60 * 1000
    }

    fun scheduleJob(activity: Activity) {
        Log.e("<><> Start ", " <<>><><>>><<<>")
        val componentName = ComponentName(activity, FirstJobService::class.java)
        val info = JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .setPersisted(true)
                .build()

        val jobScheduler: JobScheduler = activity.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = jobScheduler.schedule(info)

        val isJobScheduledSuccess = resultCode == JobScheduler.RESULT_SUCCESS
        Log.e(TAG, "Job Scheduled ${if (isJobScheduledSuccess) SUCCESS_KEY else FAILED_KEY}")
    }

    fun cancelJob(activity: Activity) {
        val jobScheduler: JobScheduler = activity.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(JOB_ID)
        Log.e(TAG, "Job CANCELED")
    }

    fun isJobServiceOn(context: Context): Boolean
    {
        val scheduler = context.getSystemService(AppCompatActivity.JOB_SCHEDULER_SERVICE) as JobScheduler
        var hasBeenScheduled = false
        for (jobInfo in scheduler.allPendingJobs)
        {
            if (jobInfo.id == JOB_ID)
            {
                hasBeenScheduled = true
                break
            }
        }
        return hasBeenScheduled
    }
    
}