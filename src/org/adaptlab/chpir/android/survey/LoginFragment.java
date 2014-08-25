package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.DeviceUser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, parent,
                false);
        
        mUsernameEditText = (EditText) v.findViewById(R.id.login_username_edit_text);
        mPasswordEditText = (EditText) v.findViewById(R.id.login_password_edit_text);
        mLoginButton = (Button) v.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userName = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                
                DeviceUser deviceUser = DeviceUser.findByUserName(userName);
                
                if (deviceUser != null && deviceUser.checkPassword(password)) {
                    AuthUtils.signIn(deviceUser);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.invalid_username_or_password), Toast.LENGTH_LONG).show();                    
                }
            }            
        });
        
        return v;       
    }
}
