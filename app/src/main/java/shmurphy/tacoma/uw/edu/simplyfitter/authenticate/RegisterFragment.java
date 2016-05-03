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

import shmurphy.tacoma.uw.edu.simplyfitter.AddWorkoutFragment;
import shmurphy.tacoma.uw.edu.simplyfitter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private static final String ADD_USER_URL =
            "http://cssgate.insttech.washington.edu/~shmurphy/SimplyFit/addUser.php?";

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_register, container, false);

        final EditText userIdText = (EditText) v.findViewById(R.id.register_userid_edittext);
        final EditText pwdText = (EditText) v.findViewById(R.id.register_password_edittext);
        Button registerButton = (Button) v.findViewById(R.id.add_user_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userIdText.getText().toString();
                String pwd = pwdText.getText().toString();

                String url = buildAddUserURL(v, userId, pwd);

//                String url = buildLoginURL(v, userId, pwd);
                ((SignInActivity) getActivity()).login(userId, pwd, url);// call the activity, to switch to the new activity
            }
        });

        return v;
    }

    private String buildAddUserURL(View v, String userId, String pwd) {
        StringBuilder sb = new StringBuilder(ADD_USER_URL);
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
