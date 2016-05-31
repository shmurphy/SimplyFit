/* TCSS 450 - Mobile Apps - Group 11 */

package shmurphy.tacoma.uw.edu.simplyfitter.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a day on the Calendar. Each day holds a list of workouts from that day.
 */
public class CalendarDay implements Serializable {

    public int mDay;     // the day of the months
    public ArrayList<Workout> myWorkouts = new ArrayList<>(); // array of this day's workouts
    public int mYear;
    public int mMonth;


    public static String mUserID;

    // used to access the database table
    public static final String DAY = "day", WORKOUT_NAME = "name", WORKOUT_START = "start",
                                WORKOUT_END = "end", WORKOUT_LOCATION = "location", USER_ID = "userID",
                                MONTH = "month", YEAR = "year";

    /**
     * Constructs a new CalendarDay based on the day of the month.
     *
     * @param day the day of the month
     */
    public CalendarDay(int day, int month, int year) {
        mDay = day;
        mMonth = month;
        mYear = year;
//        myWorkouts = new ArrayList<>();
    }

    public void setMyWorkouts(ArrayList<Workout> workoutList) {
        myWorkouts = workoutList;
    }


    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns workout list if success.
     * @param workoutJSON  * @return reason or null if successful.
     */
    public static String parseWorkoutJSON(String workoutJSON, List<CalendarDay> calendarDayList, String userID,
                                          int theMonth, int theYear) {
        mUserID = userID;
        String reason = null;
        if (workoutJSON != null && workoutJSON.length() > 0) {      // added length > 0 to display even when no workouts
            try {

                JSONArray arr = new JSONArray(workoutJSON);

                for (int i = 0; i < arr.length(); i++) {        // keep this at i = 0! workouts don't display with i = 1
                    JSONObject obj = arr.getJSONObject(i);

                    CalendarDay calendarDay = new CalendarDay(obj.getInt(CalendarDay.DAY),
                            obj.getInt(CalendarDay.MONTH), obj.getInt(CalendarDay.YEAR));
//                    Log.d("calendarday", "after calendar day created");
//                    Log.d("calendarday", username);



                    Workout workout = new Workout(obj.getString(CalendarDay.WORKOUT_NAME),
                            obj.getString(CalendarDay.WORKOUT_START), obj.getString(CalendarDay.WORKOUT_END),
                            obj.getString(CalendarDay.WORKOUT_LOCATION), obj.getString(CalendarDay.USER_ID),
                            obj.getInt(Workout.WORKOUT_ID), obj.getInt(CalendarDay.MONTH),
                            obj.getInt(CalendarDay.YEAR));
//                    Log.d("calendarday", "after workout");

                    String username = obj.getString(CalendarDay.USER_ID);
                    int day = calendarDay.mDay;
                    int year = calendarDay.mYear;
                    int month = calendarDay.mMonth;

                    // if there are no workouts logged for this day yet, we add the new workout,
                    // and set that day's position accordingly.
                    // if there is already a workout logged for this day, we fetch that day from the list,
                    // add in the new workout, and reset the day's position.

                    if(year == theYear && month == theMonth) {
                        if (username.equals(mUserID)) {
                            if (calendarDayList.get(day).myWorkouts.size() < 1) {
                                calendarDay.myWorkouts.add(workout);
                                calendarDayList.set(day, calendarDay);
                            } else {
                                CalendarDay tempDay = calendarDayList.get(day);
                                tempDay.myWorkouts.add(workout);
                                calendarDayList.set(day, tempDay);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }
        }
        return reason;
    }


    /**
     * Returns the day
     *
     * @return the day field
     */
    public int getmDay() {
        return mDay;
    }

    /**
     * Sets the day of the month field to be a new day
     *
     * @param mDay the new day of the month to set
     */
    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    /**
     * Returns the list of workouts for this day.
     *
     * @return an ArrayList of Workouts
     */
    public List<Workout> getMyWorkouts() {
        return myWorkouts;
    }

    /**
     * Returns a String representation of the CalendarDay.
     *
     * @return A String containing the Date of this day.
     */
    public String toString() {
        return "Date: " + mDay;
    }
}
