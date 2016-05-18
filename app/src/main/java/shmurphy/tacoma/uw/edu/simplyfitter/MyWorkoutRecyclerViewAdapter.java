/** TCSS 450 - Mobile Apps - Group 11 */

package shmurphy.tacoma.uw.edu.simplyfitter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import shmurphy.tacoma.uw.edu.simplyfitter.WorkoutListFragment.OnListFragmentInteractionListener;
import shmurphy.tacoma.uw.edu.simplyfitter.model.Workout;

import java.util.List;

/**
 * RecyclerViewAdapter to manage the Workout List view.
 */
public class MyWorkoutRecyclerViewAdapter extends RecyclerView.Adapter<MyWorkoutRecyclerViewAdapter.ViewHolder> {

    private final List<Workout> mWorkouts;      // list of workouts for the day
    private final OnListFragmentInteractionListener mListener;

    public MyWorkoutRecyclerViewAdapter(List<Workout> items, OnListFragmentInteractionListener listener) {
        mWorkouts = items;
        mListener = listener;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mWorkouts.get(position);
        holder.mNameView.setText(mWorkouts.get(position).mName);
        holder.mLocationView.setText("Location: " + mWorkouts.get(position).mLocation);
        holder.mTimeView.setText("Time: " + mWorkouts.get(position).mStart + " to " + mWorkouts.get(position).mEnd);
//        holder.mExerciseView.setText(mWorkouts.get(position).mExercises.toString());

        if(mWorkouts.get(position).mExercises.size() > 0) {
            StringBuilder exerciseSB = new StringBuilder();
            exerciseSB.append(mWorkouts.get(position).mExercises.get(0).toString());
            for(int i = 1; i < mWorkouts.get(position).mExercises.size(); i++) {
                exerciseSB.append(", ");
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
//        public final TextView mExerciseTitleView;
        public Workout mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.workout_name);
            mLocationView = (TextView) view.findViewById(R.id.workout_location);
            mTimeView = (TextView) view.findViewById(R.id.workout_time);
            mExerciseView = (TextView) view.findViewById(R.id.workout_exercises);
//            mExerciseTitleView = (TextView) view.findViewById(R.id.workout_fragment_exercise_title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLocationView.getText() + "'";
        }
    }
}
