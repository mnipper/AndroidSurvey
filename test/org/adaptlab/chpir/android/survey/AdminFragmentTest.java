package org.adaptlab.chpir.android.survey;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.adaptlab.chpir.android.survey.Models.AdminSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

@RunWith(RobolectricTestRunner.class)
public class AdminFragmentTest {
	private EditText mDeviceIdentifierEditText;
	private EditText mSyncIntervalEditText;
	private EditText mApiEndPointEditText;
	private Button mSaveButton;
	private AdminFragment fragment;
	private AdminActivity activity;
	private View view;
	private static final String DEVICE_ID = "This is the Device ID";
	private static final String SYNC_INTERVAL = "1";
	private static final String API_URL = "http://localhost:3000";

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(AdminActivity.class).create()
				.start().resume().get();
		fragment = (AdminFragment) activity.createFragment();
		view = LayoutInflater.from(activity).inflate(R.layout.fragment_admin_settings, (ViewGroup) activity.findViewById(R.layout.fragment_admin_settings), false);
		mSaveButton = (Button) view.findViewById(R.id.save_admin_settings_button);
		mDeviceIdentifierEditText = (EditText) view
				.findViewById(R.id.device_identifier_edit_text);
		mDeviceIdentifierEditText.setText(DEVICE_ID);
		mSyncIntervalEditText = (EditText) view
				.findViewById(R.id.sync_interval_edit_text);
		mSyncIntervalEditText.setText(SYNC_INTERVAL);
		mApiEndPointEditText = (EditText) view
				.findViewById(R.id.api_endpoint_edit_text);
		mApiEndPointEditText.setText(API_URL);
	}

	@Test
	public void shouldReturnView() throws Exception {
		View testView = fragment.onCreateView(LayoutInflater.from(activity),
				(ViewGroup) activity
						.findViewById(R.layout.fragment_admin_settings), null);
		assertThat(testView, instanceOf(View.class));
	}

	@Test
	public void shouldSaveDeviceId() throws Exception {
		mSaveButton.performClick();
		//mSaveButton.callOnClick();
		assertThat(DEVICE_ID, equalTo(AdminSettings.getInstance()
				.getDeviceIdentifier()));
	}

	@Test
	public void shouldSaveSyncInterval() throws Exception {
		mSaveButton.callOnClick();
		assertThat(Integer.parseInt(SYNC_INTERVAL), equalTo(AdminSettings
				.getInstance().getSyncInterval()));
	}

	@Test
	public void shouldSaveApiEndPoint() throws Exception {
		mSaveButton.callOnClick();
		assertThat(API_URL, equalTo(AdminSettings.getInstance().getApiUrl()));
	}
	
	 //TODO Calls to AdminSettings are currently returning Null...
	 
	@Test
	public void testIfViewIsNull() throws Exception {
		assertNotNull(view);
	}
	
	@Test
	public void testIfButtonIsNull() throws Exception {
		assertNotNull(mSaveButton);
	}
	
	@Test
	public void testAdminSettings() throws Exception {
		mSaveButton.performClick();
		assertNotNull(AdminSettings.getInstance().getApiUrl());
	}

}
