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

import shmurphy.tacoma.uw.edu.simplyfitter.model.Exercise;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddAerobicFragment extends Fragment {


    /** Used to build the add exercise URL for the addWorkout.php file */
    private final static String AEROBIC_ADD_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/addAerobic.php?";

    /** All of the EditText elements from the fragment_add_workout.xml file */
    private EditText mNameEditText;
    private EditText mHoursEditText;
    private EditText mMinutesEditText;

    public boolean mEditingMode;
    public boolean mAdded = false;

    public String mType; // either aerobic or flexibility, because these use the same fragment

    private AddAerobicListener mListener;

    public int mWorkoutID;
    public String mDeleteURL;
    public Exercise mPreviousExercise;

//    public String mDay;

    public AddAerobicFragment() {
        // Required empty public constructor
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface AddAerobicListener {
        public void addAerobicExercise(String url, String deleteURL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_aerobic, container, false);
        mNameEditText = (EditText) v.findViewById(R.id.add_aerobic_name);
        mHoursEditText = (EditText) v.findViewById(R.id.add_aerobic_hours);
        mMinutesEditText = (EditText) v.findViewById(R.id.add_aerobic_minutes);
        TextView mDurationTextView = (TextView) v.findViewById(R.id.exercise_duration_text);
        TextView mAndTextView = (TextView) v.findViewById(R.id.aerobic_and_text);
        TextView nameTextView = (TextView) v.findViewById(R.id.exercise_name);

        if(mType.equals("Flexibility")) {
            getActivity().setTitle("Add a Flexibility Exercise");
            mHoursEditText.setVisibility(View.GONE);        // hide the time text views
            mMinutesEditText.setVisibility(View.GONE);
            mDurationTextView.setVisibility(View.GONE);
            mAndTextView.setVisibility(View.GONE);
        } else if(mType.equals("Aerobic")) {
            getActivity().setTitle("Add an Aerobic Exercise");
        }

        if(mEditingMode) {
            mNameEditText.setText(mPreviousExercise.mName);
            if(mType.equals("Aerobic")) {
                mHoursEditText.setText(Integer.toString(mPreviousExercise.mHours));
                mMinutesEditText.setText(Integer.toString(mPreviousExercise.mMinutes));
            }
        }




        // hide the add workout floating action button
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.workout_fab);
        floatingActionButton.hide();

        // hide the add exercise floating action button
        FloatingActionButton exerciseFloatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.add_exercise_fab);
        exerciseFloatingActionButton.hide();


        // add exercise button
        Button addExerciseButton = (Button) v.findViewById(R.id.add_aerobic_button);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildExerciseURL(v);
                mAdded = true;
                mListener.addAerobicExercise(url, mDeleteURL);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddAerobicListener) {
            mListener = (AddAerobicListener) context;
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
        StringBuilder sb = new StringBuilder(AEROBIC_ADD_URL);
        try {
            String exerciseName = mNameEditText.getText().toString();
            sb.append("name=");
            sb.append(formatString(exerciseName));

            String hours = "";
            if(mType.equals("Aerobic")) {
                hours = mHoursEditText.getText().toString();
            }
            sb.append("&hour=");
            sb.append(hours);

            String minutes = "";
            if(mType.equals("Aerobic")) {
                minutes = mMinutesEditText.getText().toString();
            }
            sb.append("&minute=");
            sb.append(minutes);

            sb.append("&workoutID=");
            sb.append(mWorkoutID);

            sb.append("&type=");
            sb.append(mType);

            Log.i("AddAerobicFragment", sb.toString());
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

    public void setmType(String type) {
        mType = type;
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
        while(s.contains(" ")) {
            String formatted = "";
            int space = s.indexOf(" ");
            formatted = s.substring(0, space);
            formatted += "%20";
            formatted += s.substring(space+1, s.length());
            s = formatted;
        }
        return s;
    }

    public void setMDeleteURL(String url) {
        mDeleteURL = url;
    }

    public void setMEditingMode(boolean editing) {
        mEditingMode = editing;
    }

    public void setPreviousExercise(Exercise exercise) {
        Log.d("AddAerobicFragment", exercise.mName);
        mPreviousExercise = exercise;
    }

}
