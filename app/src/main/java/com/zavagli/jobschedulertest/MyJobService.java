package com.zavagli.jobschedulertest;

import android.app.ActivityManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class MyJobService extends JobService {

    private static final String TAG = MyJobService.class.getSimpleName();
    boolean isWorking = false;
    boolean jobCanceled = false;

    // Called by the Android system when it's time to run the job
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob()");
        isWorking = true;
        startWorkOnNewThread(jobParameters);
        return isWorking;
    }

    private void startWorkOnNewThread(final JobParameters jobParameters) {
        Log.d(TAG, "startWorkOnNewThread()");
        new Thread(new Runnable() {
            public void run() {
                doWork(jobParameters);
            }
        }).start();
    }

    private void doWork(JobParameters jobParameters) {
        Log.d(TAG, "doWork()");

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        Log.d(TAG, " memoryInfo.availMem " + memoryInfo.availMem + "\n" );
        Log.d(TAG, " memoryInfo.lowMemory " + memoryInfo.lowMemory + "\n" );
        Log.d(TAG, " memoryInfo.threshold " + memoryInfo.threshold + "\n" );

        Log.d(TAG, "doWork() - job finished");
        isWorking = false;
        boolean needsReschedule = false;
        jobFinished(jobParameters, needsReschedule);
    }

    // Called if the job was cancelled before being finished
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob()");
        jobCanceled = true;
        boolean needsReschedule = isWorking;
        jobFinished(jobParameters, needsReschedule);
        return needsReschedule;
    }

}
