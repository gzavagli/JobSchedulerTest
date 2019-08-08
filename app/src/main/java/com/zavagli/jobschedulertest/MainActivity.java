package com.zavagli.jobschedulertest;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MyJobService.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scheduleJob();
    }

    private void scheduleJob() {
        Log.d(TAG, "scheduleJob()");

        ComponentName componentName = new ComponentName(this, MyJobService.class);
        JobInfo jobInfo = getJobInfo(123, 1, componentName);

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled!");
        } else {
            Log.d(TAG, "Job not scheduled");
        }
    }

    private JobInfo getJobInfo(final int id, final long minute, final ComponentName name) {
        final JobInfo jobInfo;
        final long interval = TimeUnit.MINUTES.toMillis(minute);
        final boolean isPersistent = true;
        final int networkType = JobInfo.NETWORK_TYPE_ANY;

        Log.d(TAG, "JobInfo.getMinPeriodMillis: " + JobInfo.getMinPeriodMillis());
        Log.d(TAG, "JobInfo.getMinFlexMillis: " + JobInfo.getMinFlexMillis());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobInfo = new JobInfo.Builder(id, name)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .setPeriodic(JobInfo.getMinPeriodMillis(), JobInfo.getMinFlexMillis())
                    .build();
        } else {
            jobInfo = new JobInfo.Builder(id, name)
                    .setPeriodic(interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .build();
        }

        return jobInfo;
    }
}
