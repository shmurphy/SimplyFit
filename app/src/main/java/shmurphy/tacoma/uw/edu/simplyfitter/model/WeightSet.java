package shmurphy.tacoma.uw.edu.simplyfitter.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by shmurphy on 5/16/16.
 */
public class WeightSet {

    public static final String REPS = "reps", WEIGHT = "weight", WEIGHTS_ID = "weightsID";

    public int mReps, mWeight, mWeightsID;

    public WeightSet(int reps, int weight, int weightsID) {
        mReps = reps;
        mWeight = weight;
        mWeightsID = weightsID;
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns exercis list if success.
     * @param weightSetJSON  * @return reason or null if successful.
     */
    public static String parseWeightSetJSON(String weightSetJSON, List<Exercise> exerciseList, int workoutID) {
        String reason = null;

        if (weightSetJSON != null && weightSetJSON.length() > 0) { // added > 0 to show even when no weights yet

            try {
                JSONArray arr = new JSONArray(weightSetJSON);
                Exercise exercise = null;
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    WeightSet set = new WeightSet(obj.getInt(WeightSet.REPS),
                            obj.getInt(WeightSet.WEIGHT), obj.getInt(WeightSet.WEIGHTS_ID));


                    int objWeightsID = obj.getInt(WeightSet.WEIGHTS_ID);

                    // go through the list of exercises and find the one this set is attached to
                    for(int j = 0; j < exerciseList.size(); j++) {

                        if(exerciseList.get(j).getmID() == objWeightsID) {
//                            Log.d("WeightSet", "Exercise Weights ID " + exerciseList.get(j).getmName() + " id: " + exerciseList.get(j).getmID());
//                            Log.d("WeightSet", "WeightSet Weights ID " + objWeightsID);
                            exerciseList.get(j).mWeightSets.add(set);    // add the set to the exercise's list of sets
                        }
                    }

                }

            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }
        }
        return reason;
    }

    @Override
    public String toString() {
        if(mWeight > 0) {
            return mReps + " reps - " + mWeight + " lbs";
        } else {
            return mReps + " reps - body weight";
        }
    }

}
