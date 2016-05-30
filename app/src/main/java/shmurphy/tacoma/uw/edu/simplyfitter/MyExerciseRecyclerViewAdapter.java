package shmurphy.tacoma.uw.edu.simplyfitter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import shmurphy.tacoma.uw.edu.simplyfitter.model.Exercise;

import shmurphy.tacoma.uw.edu.simplyfitter.ExerciseListFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Exercise} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyExerciseRecyclerViewAdapter extends RecyclerView.Adapter<MyExerciseRecyclerViewAdapter.ViewHolder> {

    private final List<Exercise> mExercises;
    private final OnListFragmentInteractionListener mListener;
    private ExerciseListFragment.DeleteExerciseListener mDeleteExerciseListener;
    private ExerciseListFragment.EditExerciseListener mEditExerciseListener;
    private CheckBox mYogaBox;

    private final static String EXERCISE_DELETE_URL
            = "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/deleteExercise.php?";

    public MyExerciseRecyclerViewAdapter(List<Exercise> items, OnListFragmentInteractionListener listener,
                                         ExerciseListFragment.DeleteExerciseListener deleteListener,
                                         ExerciseListFragment.EditExerciseListener editExerciseListener) {
        mExercises = items;
        mListener = listener;
        mDeleteExerciseListener = deleteListener;
        mEditExerciseListener = editExerciseListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_exercise, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mExercise = mExercises.get(position);
        holder.mNameView.setText(mExercises.get(position).mName);

        if(holder.mExercise.getmType().equals("Weight")) {
            StringBuilder setSB = new StringBuilder();
            setSB.append("Sets - \n");
            if(holder.mExercise.mWeightSets.size() > 0) {
                setSB.append("1: ");
                setSB.append(holder.mExercise.mWeightSets.get(0).toString());
            }
            for(int i = 1; i < holder.mExercise.mWeightSets.size(); i++) {
                setSB.append(System.getProperty("line.separator"));
                setSB.append(i + 1);
                setSB.append(": ");
                setSB.append(holder.mExercise.mWeightSets.get(i).toString());
            }
            holder.mSetsView.setText(setSB.toString());
        } else {
            StringBuilder aerobicDetails = new StringBuilder();

            if(holder.mExercise.mHours == 0 && holder.mExercise.mMinutes == 0) {
//                aerobicDetails.append("Flexibility");
                if(holder.mExercise.mYoga) {
                    aerobicDetails.append("Yoga Pose");
                } else {
                    aerobicDetails.append("Stretch");

//                    holder.mSetsView.setVisibility(View.GONE);
                }
            } else {
                if(holder.mExercise.mHours > 0) {
                    aerobicDetails.append(holder.mExercise.mHours + " hours and ");
                }
                aerobicDetails.append(holder.mExercise.mMinutes + " minutes");
            }

            holder.mSetsView.setText(aerobicDetails.toString());

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

        FloatingActionButton deleteButton = (FloatingActionButton)
                holder.mView.findViewById(R.id.delete_exercise_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteExerciseListener.deleteExercise(buildDeleteExerciseURL(v, position));
                holder.mExercise.delete(mExercises, position); // delete from the list
            }
        });


        FloatingActionButton editButton = (FloatingActionButton)
                holder.mView.findViewById(R.id.edit_exercise_fab);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deleteExerciseURL = buildDeleteExerciseURL(v, position);
                Log.d("Editing ", holder.mExercise.mType);
                mEditExerciseListener.editExercise(holder.mExercise, deleteExerciseURL);
            }
        });

    }

    /**
     * Used to build the URL String for the PHP file.
     *
     * @param v the View
     * @param position the position of the item we are deleting
     * @return a String of the URL
     */
    private String buildDeleteExerciseURL(View v, int position) {
        StringBuilder sb = new StringBuilder(EXERCISE_DELETE_URL);

        int id = mExercises.get(position).mID;
        String type = mExercises.get(position).mType;

        try {
            sb.append("id=");
            sb.append(id);

            sb.append("&type=");
            sb.append(type);

            Log.i("DeleteWorkout ", sb.toString());
        }
        catch(Exception e) {
            Toast.makeText(v.getContext(),
                    "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return mExercises.size();
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
