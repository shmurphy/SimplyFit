package shmurphy.tacoma.uw.edu.simplyfitter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import shmurphy.tacoma.uw.edu.simplyfitter.model.CalendarDay;
import shmurphy.tacoma.uw.edu.simplyfitter.model.Exercise;
import shmurphy.tacoma.uw.edu.simplyfitter.model.WeightSet;
import shmurphy.tacoma.uw.edu.simplyfitter.model.Workout;

/**
 * Fragment to hold a list of Workouts.
 */
public class WorkoutListFragment extends Fragment  {

    private static final String AEROBICS_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/test.php?cmd=aerobics";

    private static final String WEIGHTS_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/test.php?cmd=weights";

    private static final String SETS_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/test.php?cmd=weightSet";

    /** Used to build the URL for the test.php file */
    private static final String WORKOUT_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/test.php?cmd=workouts";

    private RecyclerView mRecyclerView; // the recycler view used for this fragment
    private int mColumnCount = 1;       // number of columns for the fragment

    private OnListFragmentInteractionListener mListener;

    public int mDay; // used to keep track of the current day
    public String mUserID;
    public int mCurrentMonth;
    public int mCurrentYear;

    private String[] mMonths = {"", "January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"};

    public ArrayList<Workout> mWorkouts = new ArrayList<>();

    private DeleteWorkoutListener mDeleteWorkoutListener;
    private EditWorkoutListener mEditWorkoutListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WorkoutListFragment() {
    }

    /**
     * Sets the day field so the workout list will be associated with one specific day.
     * @param day
     */
    public void setDay(int day) {
        mDay = day;
    }

    public void setmUserID(String userID) {
        mUserID = userID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Workouts for " + mMonths[mCurrentMonth] + " " + mDay + ", " + mCurrentYear);


        View view = inflater.inflate(R.layout.fragment_workout_list, container, false);

        // hide the add exercise floating action button
        Button exerciseFloatingActionButton = (Button)
                getActivity().findViewById(R.id.add_exercise_fab);
        exerciseFloatingActionButton.setVisibility(View.GONE);

        Button chooseDateButton = (Button) getActivity().findViewById(R.id.choose_date_button);
        chooseDateButton.setVisibility(View.GONE);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            mDeleteWorkoutListener = (DeleteWorkoutListener) context;
            mEditWorkoutListener = (EditWorkoutListener) context;
        }

        DownloadWorkoutsTask task = new DownloadWorkoutsTask();
        task.execute(new String[]{WORKOUT_URL});

        // show the add workout button
        Button floatingActionButton = (Button)
                getActivity().findViewById(R.id.workout_fab);
        floatingActionButton.setVisibility(View.VISIBLE);

        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(getContext()));

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Workout item);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface DeleteWorkoutListener {
        public void deleteWorkout(String url);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface EditWorkoutListener {
        public void editWorkout(Workout workout, String deleteURL);
    }

    /**
     * Downloads the Workouts.
     */
    private class DownloadWorkoutsTask extends AsyncTask<String, Void, String> {
        @Override protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of workouts, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            mWorkouts.clear();  // neccessary to avoid duplicate workouts
            result = Workout.parseWorkoutJSON(result, mWorkouts, mDay, mUserID, mCurrentMonth, mCurrentYear);
            // sending the day to the parseJSON so that it can know which day to grab workouts for

            DownloadAerobicsTask task = new DownloadAerobicsTask(); // downloads all aerobics
            task.execute(new String[]{AEROBICS_URL});

            DownloadWeightsTask weightsTask = new DownloadWeightsTask(); // downloads all weights
            weightsTask.execute(new String[]{WEIGHTS_URL});

            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
        }

    }

    /**
     * Task to download all of the aerobics exercises to add to the list fragment.
     */
    private class DownloadAerobicsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of days, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            for(int j = 0; j < mWorkouts.size(); j++) {     // clear the exercises
                mWorkouts.get(j).mExercises.clear();        // necessary to avoid duplicate data bug
            }

            result = Exercise.parseExerciseJSON("aerobic", result, mWorkouts);

//            Log.d("WorkoutListFragment", "Aerobic " + mWorkouts.get(0).mExercises.toString());

            // Something wrong with the JSON returned.
            if (result != null) {

                Log.d("debug", "something wrong with the Exercise JSON...");

                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            // Everything is good, show the list of workouts.
//            if (!mExerciseList.isEmpty()) {
//                mRecyclerView.setAdapter(new MyExerciseRecyclerViewAdapter(mExerciseList, mListener));
//            }
        }
    }

    /**
     * Task to download all of the Exercise to add to the list fragment.
     */
    private class DownloadWeightsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of weight workouts, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            result = Exercise.parseExerciseJSON("weight", result, mWorkouts);

            // Something wrong with the JSON returned.
            if (result != null) {

                Log.d("debug", "something wrong with the Exercise JSON...");

                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if (!mWorkouts.isEmpty()) {
                MyWorkoutRecyclerViewAdapter workoutRecyclerViewAdapter = new MyWorkoutRecyclerViewAdapter(mWorkouts, mListener,
                        mDeleteWorkoutListener, mEditWorkoutListener);
                workoutRecyclerViewAdapter.setActivity(getActivity());
                mRecyclerView.setAdapter(workoutRecyclerViewAdapter);

            }
        }
    }

    /**
     * Task to download all of the Exercise to add to the list fragment.
     */
    private class DownloadSetsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of sets, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            // send the exercise list to add the sets to each weight exercise
//            result = WeightSet.parseWeightSetJSON(result, mExerciseList, mWorkoutID);

            // Something wrong with the JSON returned.
            if (result != null) {

                Log.d("debug", "something wrong with the WeightSet JSON...");

                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            // Everything is good, show the list of exercises.
//            if (!mExerciseList.isEmpty()) {
//                mRecyclerView.setAdapter(new MyExerciseRecyclerViewAdapter(mExerciseList, mListener));
//            }
        }
    }

    public void setDate(int month, int year) {
        mCurrentMonth = month;
        mCurrentYear = year;
    }

}
