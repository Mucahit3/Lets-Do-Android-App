package com.example.muhammetmucahit.letsdo.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

public final class OpenActivityJsonUtils {

    public static int TOTAL_ACTIVITIES = 0;

    public static Activity[] getSimpleActivityStringFromJson (String activityJsonStr) throws JSONException, IOException {

        JSONArray arr = new JSONArray(activityJsonStr);

        String countJSONString = NetworkUtils.getActivityCount();
        JSONObject obj = new JSONObject(countJSONString);

        TOTAL_ACTIVITIES = Integer.parseInt(obj.getString("count"));

        Activity[] activities = new Activity[TOTAL_ACTIVITIES];

        for (int i = 0; i < TOTAL_ACTIVITIES; i++) {
            activities[i] = new Activity(
                    arr.getJSONObject(i).getString("name"),
                    arr.getJSONObject(i).getString("image"),
                    arr.getJSONObject(i).getString("time")
            );
        }

        return activities;
    }

    public static ArrayList getActivitiesForSummer(Activity[] activities) {

        ArrayList<Activity> summerActivities = new ArrayList<>();

        for (int i = 0; i < activities.length; i++) {
            if (activities[i].time.equals("summer") || activities[i].time.equals("both")) {
                summerActivities.add(new Activity(
                        activities[i].name,
                        activities[i].image,
                        activities[i].time
                ));
            }
        }

        return summerActivities;
    }

    public static ArrayList getActivitiesForWinter(Activity[] activities) {

        ArrayList<Activity> winterActivities = new ArrayList<>();

        for (int i = 0; i < activities.length; i++) {
            if (activities[i].time.equals("summer") || activities[i].time.equals("both")) {
                winterActivities.add(new Activity(
                        activities[i].name,
                        activities[i].image,
                        activities[i].time
                ));
            }
        }

        return winterActivities;
    }
}