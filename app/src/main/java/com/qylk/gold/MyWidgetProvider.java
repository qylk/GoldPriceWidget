package com.qylk.gold;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {
    public static final String ACTION = "com.qylk.gold.update";

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager,
                         final int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        refresh(context, false);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION.equals(intent.getAction())) {
            refresh(context, true);
        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        MyJobService.stop(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        MyJobService.stop(context);
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);
        remoteViews.setImageViewResource(R.id.lineChart, android.R.drawable.ic_popup_sync);
        int[] appIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, MyWidgetProvider.class));
        for (int id : appIds) {
            AppWidgetManager.getInstance(context).updateAppWidget(id, remoteViews);
        }
    }

    private void refresh(Context context, boolean force) {
        MyJobService.start(context, force);
    }
}
