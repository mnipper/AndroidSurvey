package org.adaptlab.chpir.android.survey;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.adaptlab.chpir.android.survey.Models.AdminSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

//@Config(shadows = ShadowAdminSettings.class)
@RunWith(RobolectricTestRunner.class)
@PrepareForTest({ AdminSettings.class, AdminFragment.class })
public class AdminFragmentTest {
	
	private static final String API_URL = "http://localhost:3000";
	private static final String CUSTOM_LOCALE_CODE = "code";
	private static final String DEVICE_ID = "This is the Device ID";
	private static final String LAST_UPDATE_TIME = "2013-12-18-11-00";
    private static final String SYNC_INTERVAL = "1";
	private AdminActivity activity;
	private AdminFragment fragment;
	private EditText mApiEndPointEditText;
	private EditText mCustomLocaleEditText;
	private EditText mDeviceIdentifierEditText;
	private TextView mLastUpdateTextView;
	private EditText mSyncIntervalEditText;
	private View view;

	private void createView() {
		view = fragment.onCreateView(LayoutInflater.from(Robolectric.application), new LinearLayout(Robolectric.application), null);
	}
	
	@Test
	public void fragmentShouldNotBeNull() throws Exception {
		assertNotNull(fragment);
	}

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(AdminActivity.class).create().get();
		fragment = spy(new AdminFragment());
		startFragment(fragment);
		stubMockedMethods();
		setUpInstanceVariables();
	}

	private void setUpInstanceVariables() {
		createView();
		mDeviceIdentifierEditText = Whitebox.getInternalState(fragment, "mDeviceIdentifierEditText");
		mSyncIntervalEditText = Whitebox.getInternalState(fragment, "mSyncIntervalEditText");
		mApiEndPointEditText = Whitebox.getInternalState(fragment, "mApiEndPointEditText");
		mCustomLocaleEditText = Whitebox.getInternalState(fragment, "mCustomLocaleEditText");
		mLastUpdateTextView = Whitebox.getInternalState(fragment, "mLastUpdateTextView");
	}

	@Test
	public void shouldDisplayLastUpdateTime() throws Exception {
		assertEquals(activity.getResources().getString(R.string.last_update) + LAST_UPDATE_TIME, mLastUpdateTextView.getText().toString());
	}
	
	@Test
	public void shouldHaveApiEndPointText() throws Exception {
		EditText eText1 = Whitebox.getInternalState(fragment, "mApiEndPointEditText");
		EditText eText2 = (EditText) view.findViewById(R.id.api_endpoint_edit_text);
		assertEquals(eText1, eText2);
	}
	
	@Test 
	public void shouldHaveCustomLocaleEditText() throws Exception {
		EditText eText1 = Whitebox.getInternalState(fragment, "mCustomLocaleEditText");
		EditText eText2 = (EditText) view.findViewById(R.id.custom_locale_edit_text);
		assertEquals(eText1, eText2);
	}

	@Test
	public void shouldHaveDeviceIdentifierEditText() throws Exception {
		EditText eText1 = Whitebox.getInternalState(fragment, "mDeviceIdentifierEditText");
		EditText eText2 = (EditText) view.findViewById(R.id.device_identifier_edit_text);
		assertEquals(eText1, eText2);
	}
	
	@Test
	public void shouldHaveLastUpdateTextView() throws Exception {
		TextView tView1 = Whitebox.getInternalState(fragment, "mLastUpdateTextView");
		TextView tView2 = (TextView) view.findViewById(R.id.last_update_label);
		assertEquals(tView1, tView2);
	}
	
	@Test
	public void shouldHaveSyncIntervalEditText() throws Exception {
		EditText eText1 = Whitebox.getInternalState(fragment, "mSyncIntervalEditText");
		EditText eText2 = (EditText) view.findViewById(R.id.sync_interval_edit_text);
		assertEquals(eText1, eText2);
	}
	
	@Test
	public void shouldReturnView() throws Exception {
		assertThat(fragment.onCreateView(LayoutInflater.from(activity), new LinearLayout(Robolectric.application), null), instanceOf(View.class));
	}
	
	@Test
	public void shouldSetApiEndPointText() throws Exception {
		assertThat(API_URL, equalTo(mApiEndPointEditText.getText().toString()));
	}
	
	@Test
	public void shouldSetCustomLocaleCodeText() throws Exception {
		assertEquals(CUSTOM_LOCALE_CODE, mCustomLocaleEditText.getText().toString());
	}
	
	@Test
	public void shouldSetDeviceIdText() throws Exception {
		assertThat(DEVICE_ID, equalTo(mDeviceIdentifierEditText.getText().toString()));
	}

	@Test
	public void shouldSetSyncIntervalText() throws Exception {
		assertThat(SYNC_INTERVAL, equalTo(mSyncIntervalEditText.getText().toString()));
	}

	@Test
	public void shoulHaveSaveButton() throws Exception {
		Button button1 = Whitebox.getInternalState(fragment, "mSaveButton");
		Button button2 = (Button) view.findViewById(R.id.save_admin_settings_button);
		assertEquals(button1, button2);
	}
	
	private void startFragment(AdminFragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
	}
	
	private void stubMockedMethods() {
		when(fragment.getAdminSettingsInstanceDeviceId()).thenReturn(DEVICE_ID);
		when(fragment.getAdminSettingsInstanceSyncInterval()).thenReturn(SYNC_INTERVAL);
		when(fragment.getAdminSettingsInstanceApiUrl()).thenReturn(API_URL);
		when(fragment.getAdminSettingsInstanceCustomLocaleCode()).thenReturn(CUSTOM_LOCALE_CODE);
		when(fragment.getLastUpdateTime()).thenReturn(LAST_UPDATE_TIME);
	}
	
	 //TODO Write Unit Tests for onClickButton
}
