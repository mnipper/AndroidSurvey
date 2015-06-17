package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.PollService;
import org.adaptlab.chpir.android.survey.Models.AdminSettings;
import org.adaptlab.chpir.android.survey.Tasks.ApkUpdateTask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class AdminFragment extends Fragment {

    private EditText mDeviceIdentifierEditText;
    private EditText mDeviceLabelEditText;
    private EditText mSyncIntervalEditText;
    private EditText mApiDomainNameEditText;
    private EditText mApiVersionEditText;
    private EditText mProjectIdEditText;
    private EditText mApiKeyEditText;
    private EditText mCustomLocaleEditText;
    private TextView mLastUpdateTextView;
    private CheckBox mShowSurveysCheckBox;
    private CheckBox mShowSkipCheckBox;
    private CheckBox mShowNACheckBox;
    private CheckBox mShowRFCheckBox;
    private CheckBox mShowDKCheckBox;
    private CheckBox mRequirePasswordCheckBox;
    private CheckBox mRecordSurveyLocationCheckBox;
    private TextView mVersionCodeTextView;
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
        
        mDeviceLabelEditText = (EditText) v.findViewById(R.id.device_label_edit_text);
        mDeviceLabelEditText.setText(AdminSettings.getInstance().getDeviceLabel());

        mSyncIntervalEditText = (EditText) v
                .findViewById(R.id.sync_interval_edit_text);
        mSyncIntervalEditText.setText(getAdminSettingsInstanceSyncInterval());
        
        mApiDomainNameEditText = (EditText) v.findViewById(R.id.api_endpoint_text);
        mApiDomainNameEditText.setText(getAdminSettingsInstanceApiDomainName());
        
        mApiVersionEditText = (EditText) v.findViewById(R.id.api_version_text);
        mApiVersionEditText.setText(getAdminSettingsInstanceApiVersion());
        
        mProjectIdEditText = (EditText) v.findViewById(R.id.project_id_text);
        mProjectIdEditText.setText(getAdminSettingsInstanceProjectId());
        
        mApiKeyEditText = (EditText) v.findViewById(R.id.api_key_text);
        mApiKeyEditText.setText(getAdminSettingsInstanceApiKey());
        
        mCustomLocaleEditText = (EditText) v.findViewById(R.id.custom_locale_edit_text);
        mCustomLocaleEditText.setText(getAdminSettingsInstanceCustomLocaleCode());
        
        mShowSurveysCheckBox = (CheckBox) v.findViewById(R.id.show_surveys_checkbox);
        mShowSurveysCheckBox.setChecked(AdminSettings.getInstance().getShowSurveys());

        mShowSkipCheckBox = (CheckBox) v.findViewById(R.id.show_skip_checkbox);
        mShowSkipCheckBox.setChecked(AdminSettings.getInstance().getShowSkip());
        
        mShowNACheckBox = (CheckBox) v.findViewById(R.id.show_na_checkbox);
        mShowNACheckBox.setChecked(AdminSettings.getInstance().getShowNA());
        
        mShowRFCheckBox = (CheckBox) v.findViewById(R.id.show_rf_checkbox);
        mShowRFCheckBox.setChecked(AdminSettings.getInstance().getShowRF());
        
        mShowDKCheckBox = (CheckBox) v.findViewById(R.id.show_dk_checkbox);
        mShowDKCheckBox.setChecked(AdminSettings.getInstance().getShowDK());
        
        mRequirePasswordCheckBox = (CheckBox) v.findViewById(R.id.require_password);
        mRequirePasswordCheckBox.setChecked(AdminSettings.getInstance().getRequirePassword());
        
        mRecordSurveyLocationCheckBox = (CheckBox) v.findViewById(R.id.record_survey_location_checkbox);
        mRecordSurveyLocationCheckBox.setChecked(AdminSettings.getInstance().getRecordSurveyLocation());
        
        mLastUpdateTextView = (TextView) v.findViewById(R.id.last_update_label);
        mLastUpdateTextView.setText(mLastUpdateTextView.getText().toString() + getLastUpdateTime());
        
        mVersionCodeTextView = (TextView) v.findViewById(R.id.version_code_label);
        mVersionCodeTextView.setText(getString(R.string.version_code) + AppUtil.getVersionCode(getActivity()));
        
        mSaveButton = (Button) v.findViewById(R.id.save_admin_settings_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AdminSettings.getInstance().setDeviceIdentifier(mDeviceIdentifierEditText.getText().toString());
                AdminSettings.getInstance().setDeviceLabel(mDeviceLabelEditText.getText().toString()); 
                AdminSettings.getInstance().setSyncInterval(Integer.parseInt(mSyncIntervalEditText.getText().toString()));               
                AdminSettings.getInstance().setApiDomainName(mApiDomainNameEditText.getText().toString());
                AdminSettings.getInstance().setApiVersion(mApiVersionEditText.getText().toString());
                AdminSettings.getInstance().setProjectId(mProjectIdEditText.getText().toString());
                AdminSettings.getInstance().setApiKey(mApiKeyEditText.getText().toString());

                // If this code is set, it will override the language selection on the device
                // for all instrument translations.
                AdminSettings.getInstance().setCustomLocaleCode(mCustomLocaleEditText.getText().toString());
                
                PollService.setPollInterval(AdminSettings.getInstance().getSyncInterval());                
                ActiveRecordCloudSync.setAccessToken(getAdminSettingsInstanceApiKey());
                ActiveRecordCloudSync.setEndPoint(getAdminSettingsInstanceApiUrl());
                AppUtil.appInit(getActivity());
                
                AdminSettings.getInstance().setShowSurveys(mShowSurveysCheckBox.isChecked());
                AdminSettings.getInstance().setShowSkip(mShowSkipCheckBox.isChecked());
                AdminSettings.getInstance().setShowNA(mShowNACheckBox.isChecked());
                AdminSettings.getInstance().setShowRF(mShowRFCheckBox.isChecked());
                AdminSettings.getInstance().setShowDK(mShowDKCheckBox.isChecked());
                AdminSettings.getInstance().setRequirePassword(mRequirePasswordCheckBox.isChecked());
                AdminSettings.getInstance().setRecordSurveyLocation(mRecordSurveyLocationCheckBox.isChecked());
                
                getActivity().finish();
            }
        });

        return v;
    }
    
	public String getLastUpdateTime() {
		return (PollService.getLastUpdate()) + "";
	}

	public String getAdminSettingsInstanceCustomLocaleCode() {
		return AdminSettings.getInstance().getCustomLocaleCode();
	}
	
	public String getAdminSettingsInstanceApiDomainName() {
		return AdminSettings.getInstance().getApiDomainName();
	}

	public String getAdminSettingsInstanceApiUrl() {
	    
	    // Append forward slash to domain name if does not exist
	    String domainName = AdminSettings.getInstance().getApiDomainName();
	    char lastChar = domainName.charAt(domainName.length() - 1);
	    if (lastChar != '/') domainName = domainName + "/";
	    
		return domainName + "api/" + AdminSettings.getInstance().getApiVersion() + "/" + 
				"projects/" + AdminSettings.getInstance().getProjectId() + "/";
	}

	public String getAdminSettingsInstanceSyncInterval() {
		return String.valueOf(AdminSettings.getInstance().getSyncIntervalInMinutes());
	}

	public String getAdminSettingsInstanceDeviceId() {
		return AdminSettings.getInstance().getDeviceIdentifier();
	}
	
	public String getAdminSettingsInstanceApiVersion() {
		return AdminSettings.getInstance().getApiVersion();
	}
	
	public String getAdminSettingsInstanceProjectId() {
		return AdminSettings.getInstance().getProjectId();
	}
	
	public String getAdminSettingsInstanceApiKey() {
		return AdminSettings.getInstance().getApiKey();
	}
}
