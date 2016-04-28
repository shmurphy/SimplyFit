package shmurphy.tacoma.uw.edu.simplyfitter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shmurphy.tacoma.uw.edu.simplyfitter.CalendarListFragment.OnListFragmentInteractionListener;
//import shmurphy.tacoma.uw.edu.simplyfitter.dummy.DummyContent.DummyItem;
import shmurphy.tacoma.uw.edu.simplyfitter.model.CalendarDay;

import java.util.Calendar;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCalendarDayRecyclerViewAdapter extends RecyclerView.Adapter<MyCalendarDayRecyclerViewAdapter.ViewHolder> {

    private final List<CalendarDay> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyCalendarDayRecyclerViewAdapter(List<CalendarDay> items, OnListFragmentInteractionListener listener) {
        mValues = items; // this is a list of all of the days. each day has its own list of workouts
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_calendarday, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);


//        if(mValues.get(position).getmDay() == null) {
//            Log.d("DEBUG-NULL", "this was null");
//        } else {
//            Log.d("DEBUG-NOT NULL", "this wasn't null");
//        }
        holder.mIdView.setText(mValues.get(position).getmDay());

//        Log.d("DEBUG-WORKOUTS", mValues.get(position).getMyWorkouts().toString());
        if(mValues.get(position).getMyWorkouts().size() > 0) {
            holder.mContentView.setText(mValues.get(position).getMyWorkouts().toString());
        } else {
            holder.mContentView.setText("");

        }

//        Log.d("DEBUG-SIZE", String.valueOf(mValues.get(position).getMyWorkouts().size()));


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
        public final TextView mIdView;
        public final TextView mContentView;
        public CalendarDay mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.calendar_date);
            mContentView = (TextView) view.findViewById(R.id.date_workouts);
        }

        @Override
        public String toString() {
            return super.toString() + " '" ;
        }
    }
}
