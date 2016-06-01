package shmurphy.tacoma.uw.edu.simplyfitter;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import shmurphy.tacoma.uw.edu.simplyfitter.model.Exercise;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddWeightsFragment extends Fragment {


    /** Used to build the add exercise URL for the addWorkout.php file */
    private final static String WEIGHTS_ADD_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/addWeights.php?";

    /** All of the EditText elements from the fragment_add_workout.xml file */
    private EditText mNameEditText;
    private EditText mReps1;
    private EditText mWeight1;
    private EditText mReps2;
    private EditText mWeight2;
    private EditText mReps3;
    private EditText mWeight3;

    private AddWeightsListener mListener;

    public String mDeleteURL;
    public int mWorkoutID;
    public boolean mEditingMode;
    public Exercise mPreviousExercise;

    public AddWeightsFragment() {
        // Required empty public constructor
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface AddWeightsListener {
        public void addWeightsExercise(String url, String deleteURL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Add a Strength Exercise");

        View v = inflater.inflate(R.layout.fragment_add_weights, container, false);

        mNameEditText = (EditText) v.findViewById(R.id.add_weights_name);

        mReps1 = (EditText) v.findViewById(R.id.weights_reps_1);
        mReps2 = (EditText) v.findViewById(R.id.weights_reps_2);
        mReps3 = (EditText) v.findViewById(R.id.weights_reps_3);

        mWeight1 = (EditText) v.findViewById(R.id.weights_weight_1);
        mWeight2 = (EditText) v.findViewById(R.id.weights_weight_2);
        mWeight3 = (EditText) v.findViewById(R.id.weights_weight_3);

        // check each set to make sure there are values from the previous exercise.
        // do this because it's not necessary to have multiple sets or weight for each set.
        if(mEditingMode) {
            mNameEditText.setText(mPreviousExercise.mName);
            Button addButton = (Button) v.findViewById(R.id.add_weights_button);
            addButton.setText("Update Exercise");

            fillReps(0, mReps1);
            fillReps(1, mReps2);
            fillReps(2, mReps3);

            fillWeights(0, mWeight1);
            fillWeights(1, mWeight2);
            fillWeights(2, mWeight3);

        }

        // hide the add workout floating action button
        Button floatingActionButton = (Button)
                getActivity().findViewById(R.id.workout_fab);
        floatingActionButton.setVisibility(View.GONE);

        // hide the add exercise floating action button
        Button exerciseFloatingActionButton = (Button)
                getActivity().findViewById(R.id.add_exercise_fab);
        exerciseFloatingActionButton.setVisibility(View.GONE);

        // add exercise button
        Button addExerciseButton = (Button) v.findViewById(R.id.add_weights_button);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildExerciseURL(v);

                mListener.addWeightsExercise(url, mDeleteURL);
            }
        });

        return v;
    }

    private void fillReps(int setNum, EditText editText) {
        if(mPreviousExercise.mWeightSets.size() > setNum &&
                mPreviousExercise.mWeightSets.get(setNum).mReps > 0 ) {
            editText.setText(Integer.toString(mPreviousExercise.mWeightSets.get(setNum).mReps));
        }
    }

    private void fillWeights(int setNum, EditText editText) {
        if(mPreviousExercise.mWeightSets.size() > setNum &&
                mPreviousExercise.mWeightSets.get(setNum).mWeight > 0) {
            editText.setText(Integer.toString(mPreviousExercise.mWeightSets.get(setNum).mWeight));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddWeightsListener) {
            mListener = (AddWeightsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddAerobicListener");
        }
    }

    /**
     * Used to build the URL String for the PHP file.
     *
     * @param v the View
     * @return a String of the URL
     */
    private String buildExerciseURL(View v) {
        StringBuilder sb = new StringBuilder(WEIGHTS_ADD_URL);
        try {
            String exerciseName = mNameEditText.getText().toString();
            String formatted = "";
            sb.append("name=");
            sb.append(formatString(exerciseName));
            // format the name so that if it contains any spaces it can be added to the table correctly
//            if(exerciseName.contains(" ")) {
//                int space = exerciseName.indexOf(" ");
//                formatted = exerciseName.substring(0, space);
//                formatted += "%20";
//                formatted += exerciseName.substring(space+1, exerciseName.length());
//                Log.d("AddAerobicFragment", formatted);
//                sb.append(formatted);
//            } else {
//                sb.append(exerciseName);
//            }

            sb.append("&workoutID=");
            sb.append(mWorkoutID);

            sb.append("&reps1=");
            sb.append(mReps1.getText().toString());

            sb.append("&reps2=");
            sb.append(mReps2.getText().toString());

            sb.append("&reps3=");
            sb.append(mReps3.getText().toString());

            sb.append("&weight1=");
            sb.append(mWeight1.getText().toString());

            sb.append("&weight2=");
            sb.append(mWeight2.getText().toString());

            sb.append("&weight3=");
            sb.append(mWeight3.getText().toString());

//            sb.append("&weightsID");
//            sb.append(20); // TODO this needs to be changed to the actual weights id.

            Log.i("AddWeightsFragment", sb.toString());
        }
        catch(Exception e) {
            Toast.makeText(v.getContext(),
                    "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    public void setWorkoutID(int workoutID) {
        mWorkoutID = workoutID;
    }


    /**
     * Helper method to format user input.
     * Capitalizes and replaces spaces in the string to allow for insertion into the database.
     *
     * @param s the String to format.
     */
    private String formatString(String s) {
        // if the first character is lowercase, capitalize it
        if(s.charAt(0) > 96) {
            StringBuilder nameSB = new StringBuilder();
            int firstLetter = s.charAt(0);
            firstLetter = firstLetter - 32;
            nameSB.append((char) firstLetter);
            nameSB.append(s.substring(1, s.length()));

            s = nameSB.toString();
        }

        // format the name so that if it contains any spaces it can be added to the table correctly
        if(s.contains(" ")) {
            String formatted = "";
            int space = s.indexOf(" ");
            formatted = s.substring(0, space);
            formatted += "%20";
            formatted += s.substring(space+1, s.length());
            s = formatted;
        }
        return s;
    }

    public void setmDeleteURL(String deleteURL) {
        mDeleteURL = deleteURL;
    }

    public void setMEditingMode(boolean editing) {
        mEditingMode = editing;
    }

    public void setPreviousExercise(Exercise exercise) {
        mPreviousExercise = exercise;
    }
}
