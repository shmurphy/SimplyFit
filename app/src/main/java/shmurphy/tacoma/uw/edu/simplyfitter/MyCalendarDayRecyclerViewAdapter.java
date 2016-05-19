/** TCSS 450 - Mobile Apps - Group 11 */

package shmurphy.tacoma.uw.edu.simplyfitter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shmurphy.tacoma.uw.edu.simplyfitter.CalendarListFragment.OnListFragmentInteractionListener;
import shmurphy.tacoma.uw.edu.simplyfitter.model.CalendarDay;

import java.util.List;

/**
 * The RecyclerViewAdapter to manage the Calendar List view.
 */
public class MyCalendarDayRecyclerViewAdapter extends RecyclerView.Adapter<MyCalendarDayRecyclerViewAdapter.ViewHolder> {

    private final List<CalendarDay> mCalDays; // list of all of the days. each day has its own list of workouts
    private final OnListFragmentInteractionListener mListener;

    public MyCalendarDayRecyclerViewAdapter(List<CalendarDay> items, OnListFragmentInteractionListener listener) {

        mCalDays = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_calendarday, parent, false);
        return new ViewHolder(view);
    }

    @Override
    /**
     * This is where we set the text for all of the TextViews.
     */
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mCalDays.get(position);

        // this if statement is to set the very top row to be like column headers.
        if(position == 0) {
            holder.mIdView.setText("Day");
            holder.mIdView.setTextSize(25);
            holder.mContentView.setText("Workouts");
            holder.mContentView.setTextSize(25);
        } else {
            holder.mIdView.setTextSize(17);
            holder.mContentView.setTextSize(17);

            holder.mIdView.setText(Integer.toString(mCalDays.get(position).getmDay()));

            // this if statement checks to see if the calendar day we are setting has a list of
            // workouts yet. if it does, we display it in the mSetsView TextView
            if (mCalDays.get(position).getMyWorkouts().size() > 0) {
                String text = "";
                text += mCalDays.get(position).getMyWorkouts().get(0).toString();

                for (int i = 1; i < mCalDays.get(position).getMyWorkouts().size(); i++) {
                    text += ", ";
                    text += mCalDays.get(position).getMyWorkouts().get(i).toString();
                }
                holder.mContentView.setText(text);
            } else { // there are no workouts logged for this day yet, so we leave it blank
                holder.mContentView.setText("");
                Log.d("calendaryrecycler", "Workout list is 0");
            }
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


    }

    @Override
    public int getItemCount() {
        return mCalDays.size();
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
