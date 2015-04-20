package org.adaptlab.chpir.android.survey;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EncryptionPasswordFragment extends Fragment {
    private EditText mPasswordEditText;
    private Button mDoneButton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_encryption_password, parent,
                false);
        
        mPasswordEditText = (EditText) v.findViewById(R.id.encryption_password_password_edit_text);
        
        mDoneButton = (Button) v.findViewById(R.id.encryption_password_done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String decryptionPassword = mPasswordEditText.getText().toString();
                
                if (decryptionPassword.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter a password!", Toast.LENGTH_LONG).show();
                } else {
                    ActiveAndroid.initialize(new Configuration.Builder(getActivity())
                        .setEncrypted(true)
                        .setPassword(decryptionPassword)
                        .create());
                    
                    AppUtil.setupDatabase(getActivity());
                }
            }
        });
        
        return v;       
    }

}
