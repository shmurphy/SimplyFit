package shmurphy.tacoma.uw.edu.simplyfitter;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseOptionFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public String mItem;
    public int mWorkoutID;

    public ExerciseOptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_exercise_option, container, false);

        // hide the add workout fab
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.workout_fab);
        floatingActionButton.hide();

        // hide the add exercise floating action button
        FloatingActionButton exerciseFloatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.add_exercise_fab);
        exerciseFloatingActionButton.hide();

        ArrayList<String> list = new ArrayList<>();
        list.add("Aerobic");
        list.add("Anaerobic");
        list.add("Flexibility");

        Spinner spinner = (Spinner) view.findViewById(R.id.exercise_spinner);
        spinner.setOnItemSelectedListener(this);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, R.id.spinner_item_text, list);

// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        // button in the exercise option fragment
        final Button addExerciseButton = (Button) view.findViewById(R.id.exercise_option_button);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch add exercise fragment with the type from the spinner
//            Log.d("spinner!", mItem);
            if(mItem.equals("Aerobic")) {
                AddAerobicFragment addExerciseFragment = new AddAerobicFragment();
                addExerciseFragment.setWorkoutID(mWorkoutID);
                addExerciseFragment.setmType("Aerobic");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, addExerciseFragment)
                        .addToBackStack(null)
                        .commit();
            } else if(mItem.equals("Anaerobic")) {
                AddWeightsFragment addWeightsFragment = new AddWeightsFragment();
                addWeightsFragment.setWorkoutID(mWorkoutID);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, addWeightsFragment)
                        .addToBackStack(null)
                        .commit();
            } else if (mItem.equals("Flexibility")) {
                AddAerobicFragment addFlexibilityFragment = new AddAerobicFragment();
                addFlexibilityFragment.setWorkoutID(mWorkoutID);
                addFlexibilityFragment.setmType("Flexibility");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, addFlexibilityFragment)
                        .addToBackStack(null)
                        .commit();
            }
            }
        });


        return view;
    }

    // listeners for spinner
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
         mItem = (String) parent.getItemAtPosition(pos);


    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void setWorkoutID(int workoutID) {
        mWorkoutID = workoutID;
    }

}
