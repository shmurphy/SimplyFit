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

import android.app.Activity.*;

import org.w3c.dom.Text;

import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddWorkoutFragment extends Fragment {

    private final static String COURSE_ADD_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/addWorkout.php?";

    private EditText mNameEditText;
    private EditText mLocationEditText;
    private EditText mStartTimeEditText;
    private EditText mEndTimeEditText;
    private TextView mDateTextView;


    private AddWorkoutListener mListener;

    public AddWorkoutFragment() {
        // Required empty public constructor
    }

    public interface AddWorkoutListener {
        public void addWorkout(String url);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_workout, container, false);

        mNameEditText = (EditText) v.findViewById(R.id.add_workout_name);
        mLocationEditText = (EditText) v.findViewById(R.id.add_workout_location);
        mStartTimeEditText = (EditText) v.findViewById(R.id.add_workout_start);
        mEndTimeEditText = (EditText) v.findViewById(R.id.add_workout_end);
        mDateTextView = (TextView) v.findViewById(R.id.add_workout_date);



        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.workout_fab);
        floatingActionButton.hide();


        Button addWorkoutButton = (Button) v.findViewById(R.id.add_workout_button);
        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildCourseURL(v);
                mListener.addWorkout(url);
            }
        });

        return v;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddWorkoutListener) {
            mListener = (AddWorkoutListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CourseAddListener");
        }
    }

    private String buildCourseURL(View v) {
        StringBuilder sb = new StringBuilder(COURSE_ADD_URL);
        try {
            String workoutName = mNameEditText.getText().toString();
            sb.append("name=");
            sb.append(workoutName);

            String startTime = mStartTimeEditText.getText().toString();
            sb.append("&start=");
            sb.append(startTime);

            String endTime = mEndTimeEditText.getText().toString();
            sb.append("&end=");
            sb.append(endTime);

            String workoutLocation = mLocationEditText.getText().toString();
            sb.append("&location=");
            sb.append(workoutLocation);

            String date = mDateTextView.getText().toString();
            sb.append("&day=");
            sb.append(date);

            Log.i("AddWorkoutFragment", sb.toString());
        }
        catch(Exception e) {
            Toast.makeText(v.getContext(),
                    "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }



}
