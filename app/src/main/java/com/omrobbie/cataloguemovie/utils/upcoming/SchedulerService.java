package com.omrobbie.cataloguemovie.utils.upcoming;

import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

public class SchedulerService extends GcmTaskService {

    public static String TAG_TASK_UPCOMING = "upcoming movies";

    @Override
    public int onRunTask(TaskParams taskParams) {
        return 0;
    }
}
