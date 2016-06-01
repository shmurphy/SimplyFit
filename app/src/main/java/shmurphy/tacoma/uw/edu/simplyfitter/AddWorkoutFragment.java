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

import shmurphy.tacoma.uw.edu.simplyfitter.model.Workout;


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

    private int mCurrentYear;
    private int mCurrentMonth;

    private int mHour;
    private int mMinute;
    public boolean mAdded = false;
    private int mDate; // used to keep track of the current date this workout will be added to

    private String mUserID;

    public String mDeleteURL;

    public int mWorkoutID;

    public Workout mPreviousWorkout;

    public TimePickerFragment mStartTimePickerFragment;
    public TimePickerFragment mEndTimePickerFragment;

    public boolean mEditingMode;

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
        public void addWorkout(String url, String deleteURL);
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
        mDateTextView.setText("May " + mDate + ", 2016");

        getActivity().setTitle("Add New Workout");

        if(mEditingMode) {

            Button addButton = (Button) v.findViewById(R.id.add_workout_button);
            addButton.setText("Update Workout");


            mNameEditText.setText(mPreviousWorkout.mName);
            mLocationEditText.setText(mPreviousWorkout.mLocation);
            mStartTextView.setText(mPreviousWorkout.mStart);
            mEndTextView.setText(mPreviousWorkout.mEnd);
            getActivity().setTitle("Edit " + mPreviousWorkout.mName + " Workout");

        }


        // hide the add workout floating action button
        Button floatingActionButton = (Button)
                getActivity().findViewById(R.id.workout_fab);
        floatingActionButton.setVisibility(View.GONE);

        // hide the add exercise floating action button
        Button exerciseFloatingActionButton = (Button)
                getActivity().findViewById(R.id.add_exercise_fab);
        exerciseFloatingActionButton.setVisibility(View.GONE);

        // add workout button
        Button addWorkoutButton = (Button) v.findViewById(R.id.add_workout_button);
        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildWorkoutURL(v);
                mAdded = true;
                mListener.addWorkout(url, mDeleteURL);
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

            if(mEditingMode) {  // editing
                if(mStartTimePickerFragment == null) {  // user didn't change start time, use last one
                    sb.append("&start=");
                    int space = mPreviousWorkout.mStart.indexOf(" ");
                    sb.append(mPreviousWorkout.mStart.substring(0, space));
                } else {    // user launched start time picker
                    sb.append("&start=");
                    sb.append(mStartTimePickerFragment.getmHour());
                    sb.append(":");
                    sb.append(mStartTimePickerFragment.getmMinute());
                }

                if(mEndTimePickerFragment == null) {    // user didn't change end time, use last one
                    sb.append("&end=");
                    int space = mPreviousWorkout.mEnd.indexOf(" ");
                    sb.append(mPreviousWorkout.mEnd.substring(0, space));
                } else {   // user launched the end time picker
                    sb.append("&end=");
                    sb.append(mEndTimePickerFragment.getmHour());
                    sb.append(":");
                    sb.append(mEndTimePickerFragment.getmMinute());
                }
            } else {    // not editing
                sb.append("&start=");
                sb.append(mStartTimePickerFragment.getmHour());
                sb.append(":");
                sb.append(mStartTimePickerFragment.getmMinute());

                sb.append("&end=");
                sb.append(mEndTimePickerFragment.getmHour());
                sb.append(":");
                sb.append(mEndTimePickerFragment.getmMinute());
            }

            String workoutLocation = mLocationEditText.getText().toString();
            sb.append("&location=");
            sb.append(formatString(workoutLocation));

            sb.append("&day=");
            sb.append(mDate);

            sb.append("&userID=");
            sb.append(mUserID);

            sb.append("&month=");
            sb.append(mCurrentMonth);

            sb.append("&year=");
            sb.append(mCurrentYear);

            if(mWorkoutID > 0) {
                sb.append("&id=");
                sb.append(mWorkoutID);
            }

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

    public void setWorkoutID(int workoutID) {
        mWorkoutID = workoutID;
    }

    public void setMDeleteURL(String url) {
        mDeleteURL = url;
    }

    public void setMEditingMode(boolean editing) {
        mEditingMode = editing;
    }

    public void setPreviousWorkout(Workout workout) {
        mPreviousWorkout = workout;
    }

    public void setDate(int day, int month, int year) {
        mDate = day;
        mCurrentMonth = month;
        mCurrentYear = year;
    }
}
