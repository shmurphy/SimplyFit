package shmurphy.tacoma.uw.edu.simplyfitter.model;

/**
 * Created by shmurphy on 4/27/16.
 */
public class Workout {

    public String mName, mStart, mEnd, mLocation;

    public Workout(String name, String start, String end, String location) {
        mName = name;
        mStart = start;
        mEnd = end;
        mLocation = location;
    }

    public String toString() {
        return "Workout- " + mName + " at " + mLocation + ", from " + mStart + " to " + mEnd;
    }

}
