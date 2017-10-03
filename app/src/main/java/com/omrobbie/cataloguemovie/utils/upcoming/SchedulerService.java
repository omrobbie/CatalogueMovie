package com.omrobbie.cataloguemovie.utils.upcoming;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.omrobbie.cataloguemovie.R;
import com.omrobbie.cataloguemovie.api.APIClient;
import com.omrobbie.cataloguemovie.mvp.model.upcoming.ResultsItem;
import com.omrobbie.cataloguemovie.mvp.model.upcoming.UpcomingModel;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchedulerService extends GcmTaskService {

    public static String TAG_TASK_UPCOMING = "upcoming movies";

    private Call<UpcomingModel> apiCall;
    private APIClient apiClient = new APIClient();

    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if (taskParams.getTag().equals(TAG_TASK_UPCOMING)) {
            loadData();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }

        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (apiCall != null) apiCall.cancel();
    }

    private void loadData() {
        apiCall = apiClient.getService().getUpcomingMovie();
        apiCall.enqueue(new Callback<UpcomingModel>() {
            @Override
            public void onResponse(Call<UpcomingModel> call, Response<UpcomingModel> response) {
                if (response.isSuccessful()) {
                    List<ResultsItem> items = response.body().getResults();
                    int index = new Random().nextInt(items.size());

                    String title = items.get(index).getTitle();
                    String message = items.get(index).getOverview();
                    int notifId = 200;

                    showNotification(getApplicationContext(), title, message, notifId);

                } else loadFailed();
            }

            @Override
            public void onFailure(Call<UpcomingModel> call, Throwable t) {
                loadFailed();
            }
        });
    }

    private void loadFailed() {
        Toast.makeText(this, "Failed to load data.\nPlease check your Internet connections!", Toast.LENGTH_SHORT).show();
    }

    private void showNotification(Context context, String title, String message, int notifId) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.black))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        notificationManagerCompat.notify(notifId, builder.build());
    }
}
