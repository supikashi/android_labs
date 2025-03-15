package com.spksh.lab8

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews

class WebsiteWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val prefs = context.getSharedPreferences("widget_prefs", MODE_PRIVATE)
    val url = prefs.getString("url", "https://google.com") ?: "https://google.com"

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val views = RemoteViews(context.packageName, R.layout.widget_layout).apply {
        setTextViewText(R.id.widget_url_text, url)
        setOnClickPendingIntent(R.id.widget_open_button, pendingIntent)
    }

    appWidgetManager.updateAppWidget(appWidgetId, views)
}