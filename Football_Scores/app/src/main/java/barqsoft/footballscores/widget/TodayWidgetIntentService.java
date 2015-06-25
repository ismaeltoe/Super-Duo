package barqsoft.footballscores.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.scoresAdapter;

/**
 * Created by Ismael on 24/06/2015.
 */
public class TodayWidgetIntentService extends IntentService {

    public TodayWidgetIntentService() {
        super("TodayWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                TodayWidgetProvider.class));

        // Get today's date
        Date todayDate = new Date(System.currentTimeMillis());
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        String today = mformat.format(todayDate);

        // Get today's match score from the ContentProvider
        Cursor data = getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(),
                null, null, new String[]{today}, null);

        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        // Extract the match data from the Cursor
        String homeName = data.getString(scoresAdapter.COL_HOME);
        String awayName = data.getString(scoresAdapter.COL_AWAY);
        String score = Utilies.getScores(data.getInt(scoresAdapter.COL_HOME_GOALS),
                data.getInt(scoresAdapter.COL_AWAY_GOALS));
        String time = data.getString(scoresAdapter.COL_MATCHTIME);

        data.close();

        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_today);

            // Add the data to the RemoteViews
            views.setImageViewResource(R.id.widget_home_icon,
                    Utilies.getTeamCrestByTeamName(homeName));
            views.setImageViewResource(R.id.widget_away_icon,
                    Utilies.getTeamCrestByTeamName(awayName));
            views.setTextViewText(R.id.widget_home_name, homeName);
            views.setTextViewText(R.id.widget_away_name, awayName);
            views.setTextViewText(R.id.widget_score, score);
            views.setTextViewText(R.id.widget_time, time);

            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                views.setContentDescription(R.id.widget_home_icon, null);
                views.setContentDescription(R.id.widget_away_icon, null);
                views.setContentDescription(R.id.widget_home_name, homeName);
                views.setContentDescription(R.id.widget_away_name, awayName);
                views.setContentDescription(R.id.widget_score, score);
                views.setContentDescription(R.id.widget_time, time);
            }

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
