package shmurphy.tacoma.uw.edu.simplyfitter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseOptionFragment extends Fragment {


    public ExerciseOptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_option, container, false);

        ArrayList<String> list = new ArrayList<>();
        list.add("Aerobic");
        list.add("Weights");
        list.add("Meditation");

        Spinner spinner = (Spinner) view.findViewById(R.id.exercise_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, R.id.spinner_item_text, list);

// Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        return view;
    }

}
