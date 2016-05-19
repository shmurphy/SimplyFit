/* TCSS 450 - Mobile Apps - Group 11 */

package shmurphy.tacoma.uw.edu.simplyfitter.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a workout. Each workout contains its name, start time, end time, and location of
 * the workout.
 */
public class Workout {

    /** Used to hold the information about the workout */
    public String mName, mStart, mEnd, mLocation;

    public static final String WORKOUT_ID = "id";

    public static String mUserID;
    public int mID;

    public ArrayList<Exercise> mExercises = new ArrayList<>();

// TODO will need a "myExercises" list

    /**
     * Creates a new Workout with the specified name, start time, end time, and location.
     *
     * @param name the name of the workout
     * @param start the start time of the workout
     * @param end the end time of the workout
     * @param location the location of the workout
     */
    public Workout(String name, String start, String end, String location, String userID, int ID) {
        mName = name;
        mStart = start;
        mEnd = end;
        mLocation = location;
        mID = ID;
//        mUserID = userID;
    }

    /**
     * Returns a String representation of the Workout.
     *
     * @return a String containing the name and the location.
     */
    public String toString() {
        return mName + " at " + mLocation;
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns workout list if success.
     * @param workoutJSON  * @return reason or null if successful.
     */
    public static String parseWorkoutJSON(String workoutJSON, List<Workout> workoutList, int day, String userID) {
        // added day param to keep track of which day this workout is for
        mUserID = userID;
        String reason = null;
        if (workoutJSON != null) {
            try {
                JSONArray arr = new JSONArray(workoutJSON);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    // constructs a new calendar day using the information stored in the databse
                    CalendarDay calendarDay = new CalendarDay(obj.getInt(CalendarDay.DAY));

                    String username = obj.getString(CalendarDay.USER_ID);

                    // constructs a new workout using the information from the database
                    Workout workout = new Workout(obj.getString(CalendarDay.WORKOUT_NAME),
                            obj.getString(CalendarDay.WORKOUT_START), obj.getString(CalendarDay.WORKOUT_END),
                            obj.getString(CalendarDay.WORKOUT_LOCATION), obj.getString(CalendarDay.USER_ID),
                            obj.getInt(WORKOUT_ID));
                    if(calendarDay.mDay == (day) && mUserID.equals(username)) {
                        workoutList.add(workout);
                    }
                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }
        }
        return reason;
    }

}
