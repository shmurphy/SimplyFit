/**
 * TCSS 450 - Mobile Application Programming
 * Spring 2016
 */
package shmurphy.tacoma.uw.edu.simplyfitter;

import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;

import shmurphy.tacoma.uw.edu.simplyfitter.authenticate.SignInActivity;

/**
 * Note: You have to be not currently logged out of the app for this test to pass.
 * If the tests fail it's because when you are already logged in you are automatically
 * taken to the MainActivity, so log out before running this test.
 *
 * @author Shannon Murphy
 */
public class SignInActivityTest extends ActivityInstrumentationTestCase2<SignInActivity> {
    private Solo solo;

    public SignInActivityTest() {
        super(SignInActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        solo.finishOpenedActivities();
    }

    /**
     * Tests that the login button brings up the login fragment.
     */
    public void testLoginFragment() {
        boolean fragmentLoaded = solo.searchText("Simply Fit Login");
        assertTrue("Login fragment loaded", fragmentLoaded);

    }

    /**
     * Tests that the register button brings up the register fragment.
     */
    public void testRegisterFragment() {
        solo.clickOnView(getActivity().findViewById(R.id.register_button));
        boolean textFound = solo.searchText("Simply Fit Register");
        assertTrue("Register fragment loaded", textFound);
    }

    /**
     * Tests that the user is able to succesfully log in by checking that the
     * CalendarListFragment was loaded to show the user's workouts.
     */
    public void testLoggingIn() {
        solo.enterText(0, "shannon");
        solo.enterText(1, "testing");
        solo.clickOnView(getActivity().findViewById(R.id.login_button));
        boolean mainActivityLoaded = solo.searchText("2016 Workouts");

        assertTrue("Calendar List loaded", mainActivityLoaded);

        // Have to add this because the MainActivity is launched by clicking login.
        // Solo clicks logout so that the test returns to LoginActivity and the rest of the
        // tests can run properly.
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_logout));
    }

    /**
     * Tests for when a user enters the wrong username and/or password.
     * We test that the failed to add toast pops up.
     */
    public void testWrongLogin() {
        solo.enterText(0, "shannon");
        solo.enterText(1, "testifg");
        solo.clickOnView(getActivity().findViewById(R.id.login_button));
        boolean wrongCombo = solo.searchText("Failed to add");

        assertTrue("User entered wrong password/userID combo", wrongCombo);
    }

    /**
     * Tests registering a new user. Note that after running this test once successfully
     * this user will already exists and the register will not let you register with the
     * same username.
     * Added a check so that if the username is already registered we can verify that the
     * toast pops up correctly.
     */
    public void testRegistering() {
        solo.clickOnView(getActivity().findViewById(R.id.register_button));
        solo.enterText(0, "newuser");
        solo.enterText(1, "testing");

        solo.clickOnView(getActivity().findViewById(R.id.add_user_button));
        boolean alreadyRegistered = solo.searchText("Failed to add");
        boolean mainActivityLoaded = solo.searchText("2016 Workouts");

        if (mainActivityLoaded) {
            assertTrue("Calendar List loaded", mainActivityLoaded);
            // Have to add this because the MainActivity is launched by clicking login.
            // Solo clicks logout so that the test returns to LoginActivity and the rest of the
            // tests can run properly.
            solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_logout));
        } else { // this user is already registered. test that the toast pops up.
            assertTrue("Username is already registerd", alreadyRegistered);
        }
    }

}
