package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

/**
 * Created by Ismael on 24/06/2015.
 */
public class TodayWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int homeIcon = R.drawable.arsenal;
        int awayIcon = R.drawable.chelsea;
        String homeName = "Arsenal";
        String awayName = "Chelsea";
        String score = "0 - 6";

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.widget_today_small);

            // Add the data to the RemoteViews
            views.setImageViewResource(R.id.widget_home_icon, homeIcon);
            views.setImageViewResource(R.id.widget_away_icon, awayIcon);
            views.setTextViewText(R.id.widget_home_name, homeName);
            views.setTextViewText(R.id.widget_away_name, awayName);
            views.setTextViewText(R.id.widget_score, score);

            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                views.setContentDescription(R.id.widget_home_icon, null);
                views.setContentDescription(R.id.widget_away_icon, null);
                views.setContentDescription(R.id.widget_home_name, homeName);
                views.setContentDescription(R.id.widget_away_name, awayName);
                views.setContentDescription(R.id.widget_score, score);
            }

            Intent launchIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
