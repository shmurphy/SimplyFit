package shmurphy.tacoma.uw.edu.simplyfitter;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public int mHour;
    public int mMinute;

    public String mType;

    public AddWorkoutFragment mAddWorkoutFragment;
    public TimePickerFragment() {
        // Required empty public constructor

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        Toast.makeText(getActivity(), "You picked " + hourOfDay + ":" + minute
//                , Toast.LENGTH_LONG)
//                .show();
        mHour = hourOfDay;
        mMinute = minute;

        mAddWorkoutFragment.setTime(mType, mHour, mMinute);

    }

    // added this to have a reference to the add workout fragment.
    // this way we can set the time text views as soon as the time is picked by the user.
    public void setAddWorkoutFragment(AddWorkoutFragment addWorkoutFragment) {
        mAddWorkoutFragment = addWorkoutFragment;
    }

    public void setType(String t) {
        mType = t;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();


        // Create a new instance of TimePickerDialog and return it

        return new TimePickerDialog(getActivity(), this, mHour, mMinute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public int getmHour() {
        return mHour;
    }
    public int getmMinute() {
        return mMinute;
    }
}
