/* TCSS 450 - Mobile Apps - Group 11 */

package shmurphy.tacoma.uw.edu.simplyfitter.authenticate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import shmurphy.tacoma.uw.edu.simplyfitter.R;

/**
 * The fragment for logging in.
 */
public class LoginFragment extends Fragment {

    /** Used to build the login URL for the login.php file */
    private static final String LOGIN_URL =
            "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/login.php?";

    /**
     * Required empty public constructor
     */
    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Simply Fit");
        View v =  inflater.inflate(R.layout.fragment_login, container, false);
        final EditText userIdText = (EditText) v.findViewById(R.id.userid_edittext);
        final EditText pwdText = (EditText) v.findViewById(R.id.password_edittext);

        // login button
        Button loginButton = (Button) v.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userIdText.getText().toString();
                String pwd = pwdText.getText().toString();
                String url = buildLoginURL(v, userId, pwd);

                // Check for valid input
                if (TextUtils.isEmpty(userId)) {
                    Toast.makeText(v.getContext(), "Enter username"
                            , Toast.LENGTH_SHORT)
                            .show();
                    userIdText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(v.getContext(), "Enter password"
                            , Toast.LENGTH_SHORT)
                            .show();
                    pwdText.requestFocus();
                    return;
                }
                if (pwd.length() < 6) {
                    Toast.makeText(v.getContext(), "Enter password of at least 6 characters"
                            , Toast.LENGTH_SHORT)
                            .show();
                    pwdText.requestFocus();
                    return;
                }

                ((SignInActivity) getActivity()).login(userId, pwd, url);// call the activity, to switch to the new activity
            }
        });

        // Register button
        Button registerButton = (Button) v.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();

                // start the register fragment
                getFragmentManager().beginTransaction()
                .replace(R.id.login_fragment_container, registerFragment)
                .addToBackStack(null)
                .commit();
            }
        });

        return v;
    }

    /**
     * Listener for logging in.
     */
    public interface LoginInteractionListener {
        public void login(String userID, String pwd, String url);
    }

    /**
     * Used to build the URL String for the PHP file.
     *
     * @param v the View
     * @param userId the userId the user entered
     * @param pwd the password the user entered
     * @return a String of the URL
     */
    private String buildLoginURL(View v, String userId, String pwd) {
        StringBuilder sb = new StringBuilder(LOGIN_URL);
        try {
            sb.append("userID=");
            sb.append(userId);
            sb.append("&pwd=");
            sb.append(pwd);
        }
        catch(Exception e) {
            Toast.makeText(v.getContext(),
                    "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }
}
