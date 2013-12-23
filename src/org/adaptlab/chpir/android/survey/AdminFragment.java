package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.PollService;
import org.adaptlab.chpir.android.survey.Models.AdminSettings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AdminFragment extends Fragment {

    private EditText mDeviceIdentifierEditText;
    private EditText mSyncIntervalEditText;
    private EditText mApiEndPointEditText;
    private EditText mCustomLocaleEditText;
    private TextView mLastUpdateTextView;
    private Button mSaveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_settings, parent,
                false);
        mDeviceIdentifierEditText = (EditText) v
                .findViewById(R.id.device_identifier_edit_text);
        mDeviceIdentifierEditText.setText(getAdminSettingsInstanceDeviceId());

        mSyncIntervalEditText = (EditText) v
                .findViewById(R.id.sync_interval_edit_text);
        mSyncIntervalEditText.setText(getAdminSettingsInstanceSyncInterval());
        
        mApiEndPointEditText = (EditText) v.findViewById(R.id.api_endpoint_edit_text);
        mApiEndPointEditText.setText(getAdminSettingsInstanceApiUrl());
        
        mCustomLocaleEditText = (EditText) v.findViewById(R.id.custom_locale_edit_text);
        mCustomLocaleEditText.setText(getAdminSettingsInstanceCustomLocaleCode());
        
        mLastUpdateTextView = (TextView) v.findViewById(R.id.last_update_label);
        mLastUpdateTextView.setText(mLastUpdateTextView.getText().toString() + getLastUpdateTime());

        mSaveButton = (Button) v.findViewById(R.id.save_admin_settings_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AdminSettings.getInstance().setDeviceIdentifier(mDeviceIdentifierEditText
                        .getText().toString());
                
                AdminSettings.getInstance().setSyncInterval(Integer
                        .parseInt(mSyncIntervalEditText.getText().toString()));
                
                AdminSettings.getInstance().setApiUrl(mApiEndPointEditText.getText().toString());
                
                // If this code is set, it will override the language selection on the device
                // for all instrument translations.
                AdminSettings.getInstance().setCustomLocaleCode(mCustomLocaleEditText.getText().toString());
                
                PollService.setPollInterval(AdminSettings.getInstance().getSyncInterval());
                
                // Restart the polling immediately with new interval.
                // This immediately hits the server again upon save.
                PollService.restartServiceAlarm(getActivity().getApplicationContext());
                
                ActiveRecordCloudSync.setEndPoint(getAdminSettingsInstanceApiUrl());
                
                getActivity().finish();
            }
        });

        return v;
    }

    //TODO For testing convenience
    
	public String getLastUpdateTime() {
		return (PollService.getLastUpdate()) + "";
	}

	public String getAdminSettingsInstanceCustomLocaleCode() {
		return AdminSettings.getInstance().getCustomLocaleCode();
	}

	public String getAdminSettingsInstanceApiUrl() {
		return AdminSettings.getInstance().getApiUrl();
	}

	public String getAdminSettingsInstanceSyncInterval() {
		return String.valueOf(AdminSettings.getInstance().getSyncIntervalInMinutes());
	}

	public String getAdminSettingsInstanceDeviceId() {
		return AdminSettings.getInstance().getDeviceIdentifier();
	}
}
