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
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddExerciseFragment extends Fragment {


    /** Used to build the add exercise URL for the addWorkout.php file */
    private final static String AEROBIC_ADD_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/addAerobic.php?";

    /** All of the EditText elements from the fragment_add_workout.xml file */
    private EditText mNameEditText;
    private EditText mHoursEditText;
    private EditText mMinutesEditText;

    private AddExerciseListener mListener;

    public int mWorkoutID;
//    public String mDay;

    public AddExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface AddExerciseListener {
        public void addExercise(String url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Add a New Workout");

        View v = inflater.inflate(R.layout.fragment_add_exercise, container, false);

        mNameEditText = (EditText) v.findViewById(R.id.add_exercise_name);
        mHoursEditText = (EditText) v.findViewById(R.id.add_exercise_hours);
        mMinutesEditText = (EditText) v.findViewById(R.id.add_exercise_minutes);

        // hide the add workout floating action button
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.workout_fab);
        floatingActionButton.hide();

        // hide the add exercise floating action button
        FloatingActionButton exerciseFloatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.add_exercise_fab);
        exerciseFloatingActionButton.hide();


        // add exercise button
        Button addExerciseButton = (Button) v.findViewById(R.id.add_exercise_button);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildExerciseURL(v);
                mListener.addExercise(url);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddExerciseListener) {
            mListener = (AddExerciseListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddWorkoutListener");
        }
    }

    /**
     * Used to build the URL String for the PHP file.
     *
     * @param v the View
     * @return a String of the URL
     */
    private String buildExerciseURL(View v) {
        StringBuilder sb = new StringBuilder(AEROBIC_ADD_URL);
        try {
            String exerciseName = mNameEditText.getText().toString();
            sb.append("name=");
            sb.append(exerciseName);

            String hours = mHoursEditText.getText().toString();
            sb.append("&hour=");
            sb.append(hours);

            String minutes = mMinutesEditText.getText().toString();
            sb.append("&minute=");
            sb.append(minutes);

            sb.append("&workoutID=");
            sb.append(mWorkoutID);

            Log.i("AddExerciseFragment", sb.toString());
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
