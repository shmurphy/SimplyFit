package shmurphy.tacoma.uw.edu.simplyfitter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shmurphy.tacoma.uw.edu.simplyfitter.WorkoutListFragment.OnListFragmentInteractionListener;
import shmurphy.tacoma.uw.edu.simplyfitter.model.Workout;
//import shmurphy.tacoma.uw.edu.simplyfitter.model.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyWorkoutRecyclerViewAdapter extends RecyclerView.Adapter<MyWorkoutRecyclerViewAdapter.ViewHolder> {

    private final List<Workout> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyWorkoutRecyclerViewAdapter(List<Workout> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.mTitleView.setText("New Workout");
        holder.mNameView.setText(mValues.get(position).mName);
        holder.mLocationView.setText("Location: " + mValues.get(position).mLocation);
        holder.mTimeView.setText("Time: " + mValues.get(position).mStart + " to " + mValues.get(position).mEnd);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
//        public final TextView mTitleView;
        public final TextView mNameView;
        public final TextView mLocationView;
        public final TextView mTimeView;
        public Workout mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mTitleView = (TextView) view.findViewById(R.id.workout_title);
            mNameView = (TextView) view.findViewById(R.id.workout_name);
            mLocationView = (TextView) view.findViewById(R.id.workout_location);
            mTimeView = (TextView) view.findViewById(R.id.workout_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLocationView.getText() + "'";
        }
    }
}
