package shmurphy.tacoma.uw.edu.simplyfitter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import shmurphy.tacoma.uw.edu.simplyfitter.model.CalendarDay;
import shmurphy.tacoma.uw.edu.simplyfitter.model.Workout;

public class MainActivity extends AppCompatActivity implements CalendarListFragment.OnListFragmentInteractionListener,
WorkoutListFragment.OnListFragmentInteractionListener, AddWorkoutFragment.AddWorkoutListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.workout_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddWorkoutFragment addWorkoutFragment = new AddWorkoutFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, addWorkoutFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });



        if (savedInstanceState == null ||
                getSupportFragmentManager().findFragmentById(R.id.calendarlist_fragment) == null) {
            CalendarListFragment calendarListFragment = new CalendarListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, calendarListFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * From CalendarListFragment
     * Launches the WorkoutListFragment when a CalendarDay is selected
     * @param item
     */
    @Override
    public void onListFragmentInteraction(CalendarDay item) {
        String day = item.mDay;

        WorkoutListFragment workoutListFragment = new WorkoutListFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(CourseDetailFragment.COURSE_ITEM_SELECTED, item);
//        workoutListFragment.setArguments(args);
        workoutListFragment.setDay(day);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, workoutListFragment)
                .addToBackStack(null)
                .commit();

    }

    /**
     * From WorkoutListFragment
     * @param item
     */
    @Override
    public void onListFragmentInteraction(Workout item) {

    }

    /**
     * From AddWorkoutFragment
     * @param url
     */
    @Override
    public void addWorkout(String url) {

    }
}
