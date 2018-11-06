package com.qylk.gold;

import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.List;

public class MyJobService extends JobService {
    private static final int JobId = 103;

    public static void start(Context context, boolean force) {
        try {
            JobInfo.Builder builder = new JobInfo.Builder(JobId,
                    new ComponentName(context.getApplicationContext(), MyJobService.class));
            builder.setOverrideDeadline(6 * 3600 * 1000);
            builder.setPersisted(true);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            JobScheduler js = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (js != null) {
                if (js.getAllPendingJobs().size() == 0) {
                    js.schedule(builder.build());
                } else if (force) {
                    js.cancelAll();
                    js.schedule(builder.build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop(Context context) {
        try {
            JobScheduler js = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (js != null) {
                js.cancel(JobId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        new AsyncTask<Void, Void, List<Pair<Long, Float>>>() {
            @Override
            protected List<Pair<Long, Float>> doInBackground(Void... voids) {
                return DataRepository.getData();
            }

            @Override
            protected void onPostExecute(List<Pair<Long, Float>> pairs) {
                super.onPostExecute(pairs);
                if (pairs != null && pairs.size() > 0) {
                    final RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.example_appwidget);
                    remoteViews.setImageViewBitmap(R.id.lineChart, UIUtils.drawBitmap(getApplicationContext(), pairs));
                    Intent intent = new Intent(MyWidgetProvider.ACTION);
                    intent.setPackage(getApplicationContext().getPackageName());
                    remoteViews.setOnClickPendingIntent(R.id.lineChart, PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                    Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                    int[] appIds = AppWidgetManager.getInstance(getApplicationContext()).getAppWidgetIds(new ComponentName(getApplicationContext(), MyWidgetProvider.class));
                    for (int id : appIds) {
                        AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(id, remoteViews);
                    }
                }
            }
        }.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}