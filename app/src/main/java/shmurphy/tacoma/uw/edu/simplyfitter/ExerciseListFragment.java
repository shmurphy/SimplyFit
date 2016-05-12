package shmurphy.tacoma.uw.edu.simplyfitter;

import android.content.Context;
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
import android.widget.Toast;

import shmurphy.tacoma.uw.edu.simplyfitter.model.Exercise;

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
    private static final String EXERCISE_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/test.php?cmd=exercise";
    private RecyclerView mRecyclerView;

    private int mColumnCount = 1;

    private int mWorkoutID;

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExerciseListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Exercises");

        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }

        // hide the floating action button
        // TODO another floating button for adding an exercise
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.workout_fab);
        floatingActionButton.hide();

        // check for connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            DownloadExercisesTask task = new DownloadExercisesTask();
            task.execute(new String[]{EXERCISE_URL});
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

        DownloadExercisesTask task = new DownloadExercisesTask(); // starts the task to download
        task.execute(new String[]{EXERCISE_URL});                        // all of the calendar days

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
     * Task to download all of the Exercise to add to the list fragment.
     */
    private class DownloadExercisesTask extends AsyncTask<String, Void, String> {
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
//            Log.d("debug", "were here");
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            List<Exercise> exerciseList = new ArrayList<Exercise>(30);

            result = Exercise.parseExerciseJSON(result, exerciseList, mWorkoutID);

            Log.d("debug", exerciseList.toString());

            // Something wrong with the JSON returned.
            if (result != null) {

                Log.d("debug", "something wrong with the JSON...");

                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            // Everything is good, show the list of workouts.
            if (!exerciseList.isEmpty()) {
                Log.d("debug", "everything is good");

                mRecyclerView.setAdapter(new MyExerciseRecyclerViewAdapter(exerciseList, mListener));
            }
        }
    }

    public void setMWorkoutID(int theWorkoutID) {
        mWorkoutID = theWorkoutID;
    }
}

