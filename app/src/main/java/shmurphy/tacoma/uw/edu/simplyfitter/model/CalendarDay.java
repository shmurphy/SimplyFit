package shmurphy.tacoma.uw.edu.simplyfitter.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by shmurphy on 4/27/16.
 */
public class CalendarDay implements Serializable {

    public String mDay;
    public List<Workout> myWorkouts; // array of this day's workouts

    public static final String DAY = "day", WORKOUT_NAME = "name", WORKOUT_START = "start",
                                WORKOUT_END = "end", WORKOUT_LOCATION = "location";

    public CalendarDay(String day) {

        mDay = day;
        myWorkouts = new ArrayList<>();
    }


    /**  * Parses the json string, returns an error message if unsuccessful.  * Returns workout list if success.
     * @param workoutJSON  * @return reason or null if successful.
     */
    public static String parseWorkoutJSON(String workoutJSON, List<CalendarDay> calendarDayList) {

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

                    int day = Integer.parseInt(calendarDay.mDay);
                    Log.d("DEBUG - I ", String.valueOf(i));

//                    if(calendarDayList.get(day).mDay == null) {
////                        Log.d("debug-DAY", "IT WAS NULL");
//                        calendarDay.myWorkouts.add(workout);
//                        calendarDayList.set(day, calendarDay);
//                    } else {
//                        calendarDayList.get(day).myWorkouts.add(workout);
//                    }

                    // if there are no workouts logged for this day yet, we add the new workout,
                    // and set that day's position accordingly.
                    // if there is already a workout logged for this day, we fetch that day from the list,
                    // add in the new workout, and reset the day's position.

                    if(calendarDayList.get(day).myWorkouts.size() < 1) {
                        calendarDay.myWorkouts.add(workout);
                        calendarDayList.set(day, calendarDay);
                    } else {
                        CalendarDay tempDay = calendarDayList.get(day);
                        tempDay.myWorkouts.add(workout);
                        calendarDayList.set(day, tempDay);
                    }



//                    calendarDayList.add(calendarDay);

                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }
        }
//        Log.d("DEBUG-CALENDARDAY LIST", calendarDayList.toString());


//        Collections.sort(calendarDayList);
        return reason;
    }


    public String getmDay() {
        return mDay;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }

    public List<Workout> getMyWorkouts() {
        return myWorkouts;
    }

    public String toString() {
        return "Date: " + mDay;
    }
}
