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
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String LOGIN_URL =
            "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/login.php?";
    private LoginInteractionListener mListener;



    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_login, container, false);
        final EditText userIdText = (EditText) v.findViewById(R.id.userid_edittext);
        final EditText pwdText = (EditText) v.findViewById(R.id.password_edittext);
        Button signInButton = (Button) v.findViewById(R.id.login_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userIdText.getText().toString();
                String pwd = pwdText.getText().toString();

                String url = buildLoginURL(v, userId, pwd);
//                mListener.login(userId, pwd);

//                if (TextUtils.isEmpty(userId))  {
//                    Toast.makeText(v.getContext(), "Enter userid"
//                            , Toast.LENGTH_SHORT)
//                            .show();
//                    userIdText.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(userId)) {
//                    Toast.makeText(v.getContext(), "Enter a valid email address"
//                            , Toast.LENGTH_SHORT)
//                            .show();
//                    userIdText.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(pwd))  {
//                    Toast.makeText(v.getContext(), "Enter password"
//                            , Toast.LENGTH_SHORT)
//                            .show();
//                    pwdText.requestFocus();
//                    return;
//                }
//                if (pwd.length() < 6) {
//                    Toast.makeText(v.getContext(), "Enter password of at least 6 characters"
//                            , Toast.LENGTH_SHORT)
//                            .show();
//                    pwdText.requestFocus();
//                    return;
//                }




                ((SignInActivity) getActivity()).login(userId, pwd, url);// call the activity, to switch to the new activity
            }
        });
        return v;
    }

    public interface LoginInteractionListener {
        public void login(String userID, String pwd, String url);
    }

    private String buildLoginURL(View v, String userId, String pwd) {
//        userID=shannon&pwd=testing

        StringBuilder sb = new StringBuilder(LOGIN_URL);
        try {
//            String workoutName = mNameEditText.getText().toString();
//            sb.append("name=");
//            sb.append(workoutName);
//
//            String startTime = mStartTimeEditText.getText().toString();
//            sb.append("&start=");
//            sb.append(startTime);
//
//            String endTime = mEndTimeEditText.getText().toString();
//            sb.append("&end=");
//            sb.append(endTime);
//
//            String workoutLocation = mLocationEditText.getText().toString();
//            sb.append("&location=");
//            sb.append(workoutLocation);
//
////            String date = mDateTextView.getText().toString();
//            sb.append("&day=");
//            sb.append(mDate);
//
//            Log.i("AddWorkoutFragment", sb.toString());

//            sb.append("userID=shannon&pwd=testing");
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
