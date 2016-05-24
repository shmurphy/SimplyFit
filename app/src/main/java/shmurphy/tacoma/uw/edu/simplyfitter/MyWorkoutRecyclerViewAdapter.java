/** TCSS 450 - Mobile Apps - Group 11 */

package shmurphy.tacoma.uw.edu.simplyfitter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import shmurphy.tacoma.uw.edu.simplyfitter.WorkoutListFragment.OnListFragmentInteractionListener;
import shmurphy.tacoma.uw.edu.simplyfitter.model.Workout;

import java.util.List;

/**
 * RecyclerViewAdapter to manage the Workout List view.
 */
public class MyWorkoutRecyclerViewAdapter extends RecyclerView.Adapter<MyWorkoutRecyclerViewAdapter.ViewHolder> {

    private final List<Workout> mWorkouts;      // list of workouts for the day
    private final OnListFragmentInteractionListener mListener;
    private WorkoutListFragment.DeleteWorkoutListener mDeleteWorkoutListener;
    private WorkoutListFragment.EditWorkoutListener mEditWorkoutListener;

    /** Used to build the add workout URL for the addWorkout.php file */
    private final static String WORKOUT_DELETE_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/deleteWorkout.php?";


    public MyWorkoutRecyclerViewAdapter(List<Workout> items, OnListFragmentInteractionListener listener,
                                        WorkoutListFragment.DeleteWorkoutListener deleteListener,
                                        WorkoutListFragment.EditWorkoutListener editListener) {
        mWorkouts = items;
        mListener = listener;
        mDeleteWorkoutListener = deleteListener;
        mEditWorkoutListener = editListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    /**
     * This is where we set the text of all of the TextView elements.
     */
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mWorkouts.get(position);
        holder.mNameView.setText(mWorkouts.get(position).mName);
        holder.mLocationView.setText("Location: " + mWorkouts.get(position).mLocation);
        holder.mTimeView.setText("Time: " + mWorkouts.get(position).mStart + " to " + mWorkouts.get(position).mEnd);

        if(mWorkouts.get(position).mExercises.size() > 0) {
            StringBuilder exerciseSB = new StringBuilder();
            exerciseSB.append(mWorkouts.get(position).mExercises.get(0).toString());
            for(int i = 1; i < mWorkouts.get(position).mExercises.size(); i++) {
                exerciseSB.append(System.getProperty("line.separator"));
                exerciseSB.append(mWorkouts.get(position).mExercises.get(i));
            }
            holder.mExerciseView.setText(exerciseSB.toString());
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // here we will implement an exercise fragment list to be invoked
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        FloatingActionButton deleteButton = (FloatingActionButton)
                holder.mView.findViewById(R.id.delete_workout_fab);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteWorkoutListener.deleteWorkout(buildDeleteWorkoutURL(v, position));
                holder.mItem.delete(mWorkouts, position); // delete from the list TODO check if we even need this
            }
        });

        FloatingActionButton editButton = (FloatingActionButton)
                holder.mView.findViewById(R.id.edit_workout_fab);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deleteWorkoutURL = buildDeleteWorkoutURL(v, position);
                mEditWorkoutListener.editWorkout(holder.mItem, deleteWorkoutURL);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mWorkouts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;        // name of the workout
        public final TextView mLocationView;    // location of the workout
        public final TextView mTimeView;        // time of the workout
        public final TextView mExerciseView;
        public Workout mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.workout_name);
            mLocationView = (TextView) view.findViewById(R.id.workout_location);
            mTimeView = (TextView) view.findViewById(R.id.workout_time);
            mExerciseView = (TextView) view.findViewById(R.id.workout_exercises);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLocationView.getText() + "'";
        }
    }

    /**
     * Used to build the URL String for the PHP file.
     *
     * @param v the View
     * @param position the position of the item we are deleting
     * @return a String of the URL
     */
    private String buildDeleteWorkoutURL(View v, int position) {
        StringBuilder sb = new StringBuilder(WORKOUT_DELETE_URL);

        int id = mWorkouts.get(position).mID;

        try {
            sb.append("id=");
            sb.append(id);
            Log.i("DeleteWorkout ", sb.toString());
        }
        catch(Exception e) {
            Toast.makeText(v.getContext(),
                    "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }
}
