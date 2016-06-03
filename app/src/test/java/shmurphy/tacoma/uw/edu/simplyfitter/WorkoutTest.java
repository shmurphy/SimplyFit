package shmurphy.tacoma.uw.edu.simplyfitter;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import shmurphy.tacoma.uw.edu.simplyfitter.model.Exercise;
import shmurphy.tacoma.uw.edu.simplyfitter.model.Workout;

/**
 * Created by shmurphy on 6/3/16.
 */
public class WorkoutTest extends TestCase {

    Workout mWorkout;
    List<Exercise> mExercises;
    Exercise mExercise;

    @Before
    public void setUp() {
        mWorkout = new Workout("Run", "1", "2", "Home", "shmurphy", 1, 6, 2016);
        mExercise = new Exercise("Aerobic", "Jog", "1");
        mExercises = new ArrayList<>();
        mExercises.add(mExercise);
    }

    @Test
    public void testConstructor() {
        assertNotNull(mWorkout);
    }

    @Test
    public void testParseWorkoutJSON() {
        String workoutJSON = "[{\"name\":\"Run\",\"start\":\"2\",\"end\":\"3\",\"location\":\"Home\", \"userID\":\"shmurphy\",\"workoutID\":2,\"month\":5,\"year\":2016}";
        String message =  Workout.parseWorkoutJSON(workoutJSON
                , new ArrayList<Workout>(), 14, "shmurphy", 5, 2016);
        assertTrue("JSON With Valid String", message == null);
    }

    @Test
    public void testGetMName() {
        assertEquals(mWorkout.getmName(), "Run");
    }

    @Test
    public void testGetMStart() {
        assertEquals(mWorkout.getmStart(), "1");
    }

    @Test
    public void testGetMEnd() {
        assertEquals(mWorkout.getmEnd(), "2");
    }

    @Test
    public void testGetMLocation() {
        assertEquals(mWorkout.getmLocation(), "Home");
    }

    @Test
    public void testGetMUserID() {
        assertEquals(mWorkout.getmUserID(), "shmurphy");
    }

    @Test
    public void testGetMWorkoutID() {
        assertEquals(mWorkout.getmID(), 1);
    }

    @Test
    public void testGetMDay() {
        mWorkout.setmDay(5);
        assertEquals(mWorkout.getmDay(), 5);
    }

    @Test
    public void getMExercises() {
        mWorkout.mExercises.add(mExercise);
        assertEquals(mWorkout.getmExercises(), mExercises);
    }
}
