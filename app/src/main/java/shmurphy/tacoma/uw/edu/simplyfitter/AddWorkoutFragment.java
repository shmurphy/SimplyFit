/* TCSS 450 - Mobile Apps - Group 11 */

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

import org.w3c.dom.Text;


/**
 * Fragment to add new workouts. Accesses each of the EditText elements from the .xml file.
 */
public class AddWorkoutFragment extends Fragment {

    /** Used to build the add workout URL for the addWorkout.php file */
    private final static String WORKOUT_ADD_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/addWorkout.php?";

    /** All of the EditText elements from the fragment_add_workout.xml file */
    private EditText mNameEditText;
    private EditText mLocationEditText;
    private TextView mDateTextView;
    private TextView mStartTextView;
    private TextView mEndTextView;


    private int mDate; // used to keep track of the current date this workout will be added to

    private String mUserID;

    public int mHour;
    public int mMinute;

    public TimePickerFragment mStartTimePickerFragment;
    public TimePickerFragment mEndTimePickerFragment;

    private AddWorkoutListener mListener;

    /**
     * Required empty constructor
     */
    public AddWorkoutFragment() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface AddWorkoutListener {
        public void addWorkout(String url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Add a New Workout");

        View v = inflater.inflate(R.layout.fragment_add_workout, container, false);

        // access all of the EditText elements from the layout file
        mNameEditText = (EditText) v.findViewById(R.id.add_workout_name);
        mLocationEditText = (EditText) v.findViewById(R.id.add_workout_location);
        mDateTextView = (TextView) v.findViewById(R.id.add_workout_date);

        mStartTextView = (TextView) v.findViewById(R.id.start_time);
        mEndTextView = (TextView) v.findViewById(R.id.end_time);

        // Set the date TextView to display the date we are adding to
        mDateTextView.setText("New Workout for May " + mDate + ", 2016");

        // hide the add workout floating action button
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.workout_fab);
        floatingActionButton.hide();

        // hide the add exercise floating action button
        FloatingActionButton exerciseFloatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.add_exercise_fab);
        exerciseFloatingActionButton.hide();

        // add workout button
        Button addWorkoutButton = (Button) v.findViewById(R.id.add_workout_button);
        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildWorkoutURL(v);
                mListener.addWorkout(url);
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddWorkoutListener) {
            mListener = (AddWorkoutListener) context;
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
    private String buildWorkoutURL(View v) {
        StringBuilder sb = new StringBuilder(WORKOUT_ADD_URL);
        try {
            String workoutName = mNameEditText.getText().toString();
            sb.append("name=");
            sb.append(formatString(workoutName));

            sb.append("&start=");
            sb.append(mStartTimePickerFragment.getmHour());
            sb.append(":");
            sb.append(mStartTimePickerFragment.getmMinute());

            sb.append("&end=");
            sb.append(mEndTimePickerFragment.getmHour());
            sb.append(":");
            sb.append(mEndTimePickerFragment.getmMinute());

            String workoutLocation = mLocationEditText.getText().toString();
            sb.append("&location=");
            sb.append(formatString(workoutLocation));

            sb.append("&day=");
            sb.append(mDate);

            sb.append("&userID=");
            sb.append(mUserID);

            Log.i("AddWorkoutFragment", sb.toString());
        }
        catch(Exception e) {
            Toast.makeText(v.getContext(),
                    "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
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

    /**
     * Sets the date field to be the specified date.
     *
     * @param date
     */
    public void setDate(int date) {
        mDate = date;
    }

    public void setStartTimePicker(TimePickerFragment timePickerFragment) {
        mStartTimePickerFragment = timePickerFragment;
    }

    public void setEndTimePickerFragment(TimePickerFragment timePickerFragment) {
        mEndTimePickerFragment = timePickerFragment;

    }

    public void setUserID(String userID) {
        mUserID = userID;
    }

    public void setTime(String type, int hour, int minute) {

        boolean pm = false;
        if(hour >= 12) {        // pm
            hour -= 12;
            pm = true;
        }

        if(hour == 0) {
            hour = 12;
        }

        String hourString = Integer.toString(hour);
        String minuteString = Integer.toString(minute);

        String newString = minuteString;

        if(minuteString.length() == 1) {
            newString = "0";
            newString += minuteString;
        }

        String textView = hourString + ":" + newString;
        if(pm) {
            textView += " PM";
        } else {
            textView += " AM";
        }


        if(type.equals("Start")) {
            mStartTextView.setText(textView);
        } else {
            mEndTextView.setText(textView);
        }
    }

}
