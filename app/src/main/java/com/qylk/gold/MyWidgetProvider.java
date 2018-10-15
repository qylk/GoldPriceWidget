package com.qylk.gold;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.RemoteViews;

import java.util.List;

public class MyWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager,
                         final int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        int appId = 0;
        for (int i = 0; i < appWidgetIds.length; i++) {
            appId = appWidgetIds[i];
        }
        refresh(context, appId);
    }

    private void refresh(final Context context, final int appId) {
        new AsyncTask<Void, Void, List<Pair<Long, Float>>>() {
            @Override
            protected List<Pair<Long, Float>> doInBackground(Void... voids) {
                return DataRepository.getData();
            }

            @Override
            protected void onPostExecute(List<Pair<Long, Float>> pairs) {
                super.onPostExecute(pairs);
                if (pairs != null && pairs.size() > 0) {
                    final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);
                    remoteViews.setImageViewBitmap(R.id.lineChart, UIUtils.drawBitmap(context, pairs));
                    AppWidgetManager.getInstance(context).updateAppWidget(appId, remoteViews);
                }
            }
        }.execute();
    }
}
