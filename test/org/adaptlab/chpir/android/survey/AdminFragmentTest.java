package org.adaptlab.chpir.android.survey;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;
import static org.robolectric.Robolectric.shadowOf;

import org.adaptlab.chpir.android.robolectric.RoboTestRunner;
import org.adaptlab.chpir.android.survey.Models.AdminSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.robolectric.shadows.ShadowAdminSettings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

@Config(shadows = ShadowAdminSettings.class)
@RunWith(RoboTestRunner.class)
@PrepareForTest({ AdminSettings.class, AdminFragment.class })
public class AdminFragmentTest {
	
	private EditText mDeviceIdentifierEditText;
	private EditText mSyncIntervalEditText;
	private EditText mApiEndPointEditText;
	private Button mSaveButton;
	private AdminFragment fragment;
	private AdminSettings adminSettings;
	private ShadowAdminSettings shadowAdmin;
	private AdminActivity activity;
	private View view;
	private static final String DEVICE_ID = "This is the Device ID";
	private static final String SYNC_INTERVAL = "1";
	private static final String API_URL = "http://localhost:3000";

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(AdminActivity.class).create().get();
		fragment = spy(new AdminFragment());
		startFragment(fragment);
		adminSettings = new AdminSettings();
		//shadowAdmin = Robolectric.shadowOf(adminSettings);
	}
	
	private void startFragment(AdminFragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
	}

	private void setUpVariables() {
		//mSaveButton = Whitebox.getInternalState(fragment, "mSaveButton");
		//mDeviceIdentifierEditText = Whitebox.getInternalState(fragment, "mDeviceIdentifierEditText");
		/*view = LayoutInflater.from(activity).inflate(R.layout.fragment_admin_settings, (ViewGroup) activity.findViewById(R.layout.fragment_admin_settings), false);
		mSaveButton = (Button) view.findViewById(R.id.save_admin_settings_button);
		mDeviceIdentifierEditText = (EditText) view.findViewById(R.id.device_identifier_edit_text);
		mDeviceIdentifierEditText.setText(DEVICE_ID);
		mSyncIntervalEditText = (EditText) view.findViewById(R.id.sync_interval_edit_text);
		mSyncIntervalEditText.setText(SYNC_INTERVAL);
		mApiEndPointEditText = (EditText) view.findViewById(R.id.api_endpoint_edit_text);
		mApiEndPointEditText.setText(API_URL);*/
	}

	private void stubMockedMethods() {
		//ShadowAdminSettings sAdminSettings = shadowOf(adminSettings);
		//when(AdminSettings.getInstance()).thenReturn(adminSettings);
		//when(AdminSettings.getInstance().getDeviceIdentifier()).thenReturn(DEVICE_ID);
		//when(AdminSettings.getInstance().getSyncInterval()).thenReturn(Integer.parseInt(SYNC_INTERVAL));
		//when(AdminSettings.getInstance().getApiUrl()).thenReturn(API_URL);
	}

	private void createView() {
		view = fragment.onCreateView(LayoutInflater.from(Robolectric.application), new LinearLayout(Robolectric.application), null);
		//setUpVariables();
	}
	
	@Test
	public void fragmentShouldNotBeNull() throws Exception {
		assertNotNull(fragment);
	}
	
	@Test
	public void shouldReturnView() throws Exception {
		assertThat(fragment.onCreateView(LayoutInflater.from(activity), new LinearLayout(Robolectric.application), null), instanceOf(View.class));
	}

	@Test
	public void shouldHaveDeviceIdentifierEditText() throws Exception {
		createView();
		EditText eText1 = Whitebox.getInternalState(fragment, "mDeviceIdentifierEditText");
		EditText eText2 = (EditText) view.findViewById(R.id.device_identifier_edit_text);
		assertEquals(eText1, eText2);
	}
	
	@Test
	public void shouldHaveSyncIntervalEditText() throws Exception {
		createView();
		EditText eText1 = Whitebox.getInternalState(fragment, "mSyncIntervalEditText");
		EditText eText2 = (EditText) view.findViewById(R.id.sync_interval_edit_text);
		assertEquals(eText1, eText2);
	}
	
	@Test
	public void shouldHaveApiEndPointText() throws Exception {
		createView();
		EditText eText1 = Whitebox.getInternalState(fragment, "mApiEndPointEditText");
		EditText eText2 = (EditText) view.findViewById(R.id.api_endpoint_edit_text);
		assertEquals(eText1, eText2);
	}
	
	@Test 
	public void shouldHaveCustomLocaleEditText() throws Exception {
		createView();
		EditText eText1 = Whitebox.getInternalState(fragment, "mCustomLocaleEditText");
		EditText eText2 = (EditText) view.findViewById(R.id.custom_locale_edit_text);
		assertEquals(eText1, eText2);
	}
	
	@Test
	public void shouldHaveLastUpdateTextView() throws Exception {
		createView();
		TextView tView1 = Whitebox.getInternalState(fragment, "mLastUpdateTextView");
		TextView tView2 = (TextView) view.findViewById(R.id.last_update_label);
		assertEquals(tView1, tView2);
	}
	
	@Test
	public void shoulHaveSaveButton() throws Exception {
		createView();
		Button button1 = Whitebox.getInternalState(fragment, "mSaveButton");
		Button button2 = (Button) view.findViewById(R.id.save_admin_settings_button);
		assertEquals(button1, button2);
	}
	
	@Test
	public void shouldSetDeviceIdText() throws Exception {
		when(adminSettings.getDeviceIdentifier()).thenReturn(DEVICE_ID);
		createView();
		mDeviceIdentifierEditText = Whitebox.getInternalState(fragment, "mDeviceIdentifierEditText");
		assertThat(DEVICE_ID, equalTo(mDeviceIdentifierEditText.getText().toString()));
	}

	@Test
	public void shouldSetSyncIntervalText() throws Exception {
		createView();
		mSyncIntervalEditText = Whitebox.getInternalState(fragment, "mSyncIntervalEditText");
		assertThat(SYNC_INTERVAL, equalTo(mSyncIntervalEditText.getText().toString()));
	}

	@Test
	public void shouldSetApiEndPointText() throws Exception {
		createView();
		mApiEndPointEditText = Whitebox.getInternalState(fragment, "mApiEndPointEditText");
		assertThat(API_URL, equalTo(mApiEndPointEditText.getText().toString()));
	}
	
	 //TODO Calls to AdminSettings are currently returning Null...
	 
	/*@Test
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
	}*/

}
