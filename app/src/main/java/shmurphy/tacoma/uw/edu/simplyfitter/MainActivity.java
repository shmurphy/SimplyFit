package shmurphy.tacoma.uw.edu.simplyfitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import shmurphy.tacoma.uw.edu.simplyfitter.model.CalendarDay;
import shmurphy.tacoma.uw.edu.simplyfitter.model.Workout;

public class MainActivity extends AppCompatActivity implements CalendarListFragment.OnListFragmentInteractionListener,
WorkoutListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


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

    @Override
    public void onListFragmentInteraction(Workout item) {

    }
}
