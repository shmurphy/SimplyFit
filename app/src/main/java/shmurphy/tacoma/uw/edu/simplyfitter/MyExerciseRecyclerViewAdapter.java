package shmurphy.tacoma.uw.edu.simplyfitter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shmurphy.tacoma.uw.edu.simplyfitter.model.Exercise;

import shmurphy.tacoma.uw.edu.simplyfitter.ExerciseListFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Exercise} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyExerciseRecyclerViewAdapter extends RecyclerView.Adapter<MyExerciseRecyclerViewAdapter.ViewHolder> {

    private final List<Exercise> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyExerciseRecyclerViewAdapter(List<Exercise> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mExercise = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).mName);

        if(holder.mExercise.getmType().equals("Weight")) {
            StringBuilder setSB = new StringBuilder();
            if(holder.mExercise.mWeightSets.size() > 1) {
                setSB.append("SET 1: ");
                setSB.append(holder.mExercise.mWeightSets.get(0).toString());
            }
            for(int i = 1; i < holder.mExercise.mWeightSets.size(); i++) {
                setSB.append(System.getProperty("line.separator"));
                setSB.append("SET ");
                setSB.append(i + 1);
                setSB.append(": ");
                setSB.append(holder.mExercise.mWeightSets.get(i).toString());
            }
            holder.mSetsView.setText(setSB.toString());
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mExercise);
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
        public final TextView mNameView;
        public final TextView mSetsView;
        public Exercise mExercise;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.exercise_fragment_name);
            mSetsView = (TextView) view.findViewById(R.id.exercise_fragment_sets);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSetsView.getText() + "'";
        }
    }
}
