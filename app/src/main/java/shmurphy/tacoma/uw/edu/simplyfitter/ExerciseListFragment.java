package shmurphy.tacoma.uw.edu.simplyfitter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import shmurphy.tacoma.uw.edu.simplyfitter.model.Exercise;
import shmurphy.tacoma.uw.edu.simplyfitter.model.WeightSet;
import shmurphy.tacoma.uw.edu.simplyfitter.model.Workout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ExerciseListFragment extends Fragment {
    /**
     * Used to build the Workout URL for the test.php file
     */
    private static final String AEROBICS_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/test.php?cmd=aerobics";

    private static final String WEIGHTS_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/test.php?cmd=weights";

    private static final String SETS_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/test.php?cmd=weightSet";

    private RecyclerView mRecyclerView;

    private int mColumnCount = 1;

    private int mWorkoutID;
    private String mWorkoutName;

    List<Exercise> mExerciseList = new ArrayList<Exercise>(30);

    private OnListFragmentInteractionListener mListener;
    private DeleteExerciseListener mDeleteExerciseListener;
    private EditExerciseListener mEditExerciseListener;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExerciseListFragment() {
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface DeleteExerciseListener {
        public void deleteExercise(String url);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface EditExerciseListener {
        public void editExercise(Exercise exercise, String deleteURL);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(mWorkoutName + " Exercises");

        Log.d("exerciselist", getActivity().getTitle().toString());

        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            mDeleteExerciseListener = (DeleteExerciseListener) context;
            mEditExerciseListener = (EditExerciseListener) context;
        }

        // hide the add workout floating action button
        Button floatingActionButton = (Button)
                getActivity().findViewById(R.id.workout_fab);
        floatingActionButton.setVisibility(View.GONE);

        // show the exercise floating action button
        FloatingActionButton exerciseFloatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.add_exercise_fab);
        exerciseFloatingActionButton.show();

        // check for connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            DownloadAerobicsTask task = new DownloadAerobicsTask(); // downloads all aerobics
            task.execute(new String[]{AEROBICS_URL});

            DownloadWeightsTask weightsTask = new DownloadWeightsTask(); // downloads all weights
            weightsTask.execute(new String[]{WEIGHTS_URL});


        } else {
            Toast.makeText(view.getContext(),
                    "No network connection available. Cannot display exercises",
                    Toast.LENGTH_SHORT)
                    .show();
        }


        try {
            InputStream inputStream = getActivity().openFileInput(
                    getString(R.string.LOGIN_FILE));
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                Toast.makeText(getActivity(), stringBuilder.toString(), Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        void onListFragmentInteraction(Exercise item);
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

//            Log.d("ExerciseListFragment", "Before " + mExerciseList.toString());

            mExerciseList.clear();
            result = Exercise.parseExerciseJSONForList("aerobic", result, mExerciseList, mWorkoutID);

//            Log.d("ExerciseListFragment", "After " + mExerciseList.toString());

            // Something wrong with the JSON returned.
            if (result != null) {

                Log.d("debug", "something wrong with the Exercise JSON...");

                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            // Everything is good, show the list of workouts.
            if (!mExerciseList.isEmpty()) {
                mRecyclerView.setAdapter(new MyExerciseRecyclerViewAdapter(mExerciseList, mListener,
                        mDeleteExerciseListener, mEditExerciseListener));
            }
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

            result = Exercise.parseExerciseJSONForList("weight", result, mExerciseList, mWorkoutID);

            // Something wrong with the JSON returned.
            if (result != null) {

                Log.d("debug", "something wrong with the Exercise JSON...");

                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            // Everything is good, show the list of workouts.
            if (!mExerciseList.isEmpty()) {
                DownloadSetsTask setsTask = new DownloadSetsTask(); // downloads all sets
                setsTask.execute(new String[]{SETS_URL});

//                mRecyclerView.setAdapter(new MyExerciseRecyclerViewAdapter(mExerciseList, mListener));
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
            result = WeightSet.parseWeightSetJSON(result, mExerciseList, mWorkoutID);

            // Something wrong with the JSON returned.
            if (result != null) {

                Log.d("debug", "something wrong with the WeightSet JSON...");

                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            // Everything is good, show the list of exercises.
            if (!mExerciseList.isEmpty()) {
                mRecyclerView.setAdapter(new MyExerciseRecyclerViewAdapter(mExerciseList, mListener,
                        mDeleteExerciseListener, mEditExerciseListener));
            }
        }
    }

    public void setMWorkout(Workout theWorkout) {

        mWorkoutID = theWorkout.mID;
        mWorkoutName = theWorkout.mName + " Workout";
    }
}

