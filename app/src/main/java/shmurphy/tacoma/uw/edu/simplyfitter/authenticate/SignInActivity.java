/* TCSS 450 - Mobile Apps - Group 11 */

package shmurphy.tacoma.uw.edu.simplyfitter.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import shmurphy.tacoma.uw.edu.simplyfitter.MainActivity;
import shmurphy.tacoma.uw.edu.simplyfitter.R;

public class SignInActivity extends AppCompatActivity implements LoginFragment.LoginInteractionListener {

    private String mUserID;
//    private MainActivity mMain = new MainActivity();
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        if(!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.login_fragment_container, new LoginFragment())
                    .commit();
        } else {
//            mMain.setmUserID(mUserID);
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();

        }
    }

    /**
     * Launches the login task to start the login process.
     *
     * @param userID the userId entered
     * @param pwd the password entered
     * @param url the URL built from the userId and password
     */
    @Override
    public void login(String userID, String pwd, String url) {
        mUserID = userID;       // set correctly here

        Log.d("LOGIN,", mUserID);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Check if the login and password are valid

//            try {
//                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
//                        openFileOutput(getString(R.string.LOGIN_FILE)
//                                , Context.MODE_PRIVATE));
//                outputStreamWriter.write("email = " + userID + ";");
//                outputStreamWriter.write("password = " + pwd);
//                outputStreamWriter.close();
//                Toast.makeText(this, "Stored in File Successfully!", Toast.LENGTH_LONG)
//                        .show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } else {
            Toast.makeText(this, "No network connection available. Cannot authenticate user",
                    Toast.LENGTH_SHORT) .show();
            return;
        }

        LoginTask task = new LoginTask();
        task.execute(new String[]{url.toString()});
        task.setIntent(this);
    }

    /**
     * Executes the login function.
     */
    private class LoginTask extends AsyncTask<String, Void, String> {

        public boolean mSuccess;
        public Intent mIntent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to add user, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
//                    Toast.makeText(getApplicationContext(), "Logged in successfully!"
//                            , Toast.LENGTH_LONG)
//                            .show();
                    mSuccess = true;
                    mSharedPreferences.edit()
                            .putBoolean(getString(R.string.LOGGEDIN), true)
                            .putString("username",mUserID)      // add the username to the shared preferences
                            .commit();                          // so we can access it in MainActivity

                    startActivity(mIntent);
                    finish();


                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add: "
                            + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();

                    mSuccess = false;
//                    Log.d("Debug-login", Boolean.toString(mSuccess));

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        protected boolean getMSuccess() {
            return mSuccess;
        }

        protected void setIntent(SignInActivity activity) {
            Log.d("HERE", mUserID);
            mIntent = new Intent(activity, MainActivity.class);

        }
    }

}
