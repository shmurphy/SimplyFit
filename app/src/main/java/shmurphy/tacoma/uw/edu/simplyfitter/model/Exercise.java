/* TCSS 450 - Mobile Apps - Group 11 */

// TODO ExerciseListFragment, ExerciseRecyclerViewAdapter
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
public class Exercise implements Serializable {

    public String mType, mName;     // keep these NON static! made errors with adding to exercise list
    public static int mWorkoutID;
    public int mHours, mMinutes;
    public int mID;

    public ArrayList<WeightSet> mWeightSets = new ArrayList<>();

    public static final String DAY = "day", EXERCISE_NAME = "name", EXERCISE_HOURS = "hours",
            EXERCISE_MINUTES = "minutes", EXERCISE_WORKOUT_ID = "workoutID";

    // constructor for Weights workout
    public Exercise(String type, String name, String workoutID) {
        mType = type;
        mName = name;

    }

    // constructor for Aerobics workout
    public Exercise(String type, String name, int hours, int minutes, String workoutID) {
        mType = type;
        mName = name;
        mHours = hours;
        mMinutes = minutes;
//        mWorkoutID = workoutID;
    }


    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns exercis list if success.
     * @param exerciseJSON  * @return reason or null if successful.
     */
    public static String parseExerciseJSON(String type, String exerciseJSON, List<Exercise> exerciseList, int workoutID) {
        String reason = null;

        Log.d("debug", "exerciseJSON" + exerciseJSON);

        mWorkoutID = workoutID;

        if (exerciseJSON != null) {

            try {
                JSONArray arr = new JSONArray(exerciseJSON);
                Exercise exercise = null;
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    if(type.equals("aerobic")) {
//                        Log.d("debug", "new aerobic");

                        exercise = new Exercise("Aerobic", obj.getString(Exercise.EXERCISE_NAME),
                                obj.getInt(Exercise.EXERCISE_HOURS), obj.getInt(Exercise.EXERCISE_MINUTES),
                                obj.getString(Exercise.EXERCISE_WORKOUT_ID));
                    } else if (type.equals("weight")) {
//                        Log.d("debug", "new weight");
                        exercise = new Exercise("Weight", obj.getString(Exercise.EXERCISE_NAME),
                                obj.getString(Exercise.EXERCISE_WORKOUT_ID));
                    }

                    int objWorkoutID = obj.getInt(Exercise.EXERCISE_WORKOUT_ID);
//                    mID = obj.getInt("id"); // gets the exercise ID
                    exercise.setmID(obj.getInt("id"));
                    // check if the workoutID for this exercise matches the specific workout.
                    // if it does, add it. else, don't add it.
                    if(objWorkoutID == mWorkoutID) {
                        Log.d("debug", "this should be added " + exercise.toString());
                        exerciseList.add(exercise);
                    }
                }

            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }
        }
        return reason;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String type) {
        mType = type;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String name) {
        mName = name;
    }

    public static int getmWorkoutID() {
        return mWorkoutID;
    }

    public static void setmWorkoutID(int workoutID) {
        mWorkoutID = workoutID;
    }

    public int getmHours() {
        return mHours;
    }

    public void setmHours(int hours) {
        mHours = hours;
    }

    public int getmMinutes() {
        return mMinutes;
    }

    public void setmMinutes(int minutes) {
        mMinutes = minutes;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int id) {
        mID = id;
    }

    public String toString() {
        return "name: " + mName;
    }

}
