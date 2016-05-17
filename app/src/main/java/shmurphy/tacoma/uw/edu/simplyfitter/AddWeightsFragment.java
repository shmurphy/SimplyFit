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

    public int mWorkoutID;

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
        public void addWeightsExercise(String url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Add a New Workout");

        View v = inflater.inflate(R.layout.fragment_add_weights, container, false);

        mNameEditText = (EditText) v.findViewById(R.id.add_weights_name);

        mReps1 = (EditText) v.findViewById(R.id.weights_reps_1);
        mReps2 = (EditText) v.findViewById(R.id.weights_reps_2);
        mReps3 = (EditText) v.findViewById(R.id.weights_reps_3);

        mWeight1 = (EditText) v.findViewById(R.id.weights_weight_1);
        mWeight2 = (EditText) v.findViewById(R.id.weights_weight_2);
        mWeight3 = (EditText) v.findViewById(R.id.weights_weight_3);


        // TODO need to get sets from the edit texts

        // hide the add workout floating action button
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.workout_fab);
        floatingActionButton.hide();

        // hide the add exercise floating action button
        FloatingActionButton exerciseFloatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.add_exercise_fab);
        exerciseFloatingActionButton.hide();

        // add exercise button
        Button addExerciseButton = (Button) v.findViewById(R.id.add_weights_button);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildExerciseURL(v);
                mListener.addWeightsExercise(url);
            }
        });

        return v;
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
            // format the name so that if it contains any spaces it can be added to the table correctly
            if(exerciseName.contains(" ")) {
                int space = exerciseName.indexOf(" ");
                formatted = exerciseName.substring(0, space);
                formatted += "%20";
                formatted += exerciseName.substring(space+1, exerciseName.length());
                Log.d("AddAerobicFragment", formatted);
                sb.append(formatted);
            } else {
                sb.append(exerciseName);
            }

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

}
