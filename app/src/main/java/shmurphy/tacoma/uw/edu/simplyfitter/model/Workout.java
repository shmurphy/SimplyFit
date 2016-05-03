package shmurphy.tacoma.uw.edu.simplyfitter.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by shmurphy on 4/27/16.
 */
public class Workout {

    public String mName, mStart, mEnd, mLocation;

    public Workout(String name, String start, String end, String location) {
        mName = name;
        mStart = start;
        mEnd = end;
        mLocation = location;
    }

    public String toString() {
//        return mName + " at " + mLocation + ", from " + mStart + " to " + mEnd;
        return mName + " at " + mLocation;
    }

    /**  * Parses the json string, returns an error message if unsuccessful.  * Returns workout list if success.
     * @param workoutJSON  * @return reason or null if successful.
     */
    public static String parseWorkoutJSON(String workoutJSON, List<Workout> workoutList, String day) {
        // added day param to keep track of which day this workout is for

        String reason = null;
        if (workoutJSON != null) {
            try {
                JSONArray arr = new JSONArray(workoutJSON);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    CalendarDay calendarDay = new CalendarDay(obj.getString(CalendarDay.DAY));
                    Workout workout = new Workout(obj.getString(CalendarDay.WORKOUT_NAME),
                            obj.getString(CalendarDay.WORKOUT_START), obj.getString(CalendarDay.WORKOUT_END),
                            obj.getString(CalendarDay.WORKOUT_LOCATION));



//                    Log.d("DEBUG-CALENDARDAY", calendarDay.mDay);
//                    Log.d("DEBUG-DAY", day);

                    if(calendarDay.mDay.equals(day)) {
                        workoutList.add(workout);
                    }

//                    workoutList.add(workout);

                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }
        }
//        Log.d("DEBUG-CALENDARDAY LIST", calendarDayList.toString());


//        Collections.sort(calendarDayList);
        return reason;
    }

}
