package org.adaptlab.chpir.android.survey;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.shadowOf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.tester.android.view.TestMenuItem;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

@RunWith(RobolectricTestRunner.class)
public class InstrumentFragmentTest extends FragmentBaseTest {
	private static final String PASSWORD = "password";
	private InstrumentFragment mFragment;
	private InstrumentActivity mActivity;

	@Override 
	public void setUp() throws Exception {
		mActivity = Robolectric.buildActivity(InstrumentActivity.class).create().get();
		mFragment = spy(new InstrumentFragment());
		startFragment(mActivity, mFragment);
	}
	
	@Test
	public void activityShouldNotBeNull() throws Exception {
		assertNotNull(mActivity);
	}
	
	@Test
	public void fragmentShouldNotBeNull() throws Exception {
		assertNotNull(mFragment);
	}
	
	@Test
	public void shouldHaveOptionsMenuSetToTrue() throws Exception {
		assertThat(mFragment.hasOptionsMenu(), equalTo(true));
	}
	
	@Test
	public void shouldSetListAdapter() throws Exception {
		Bundle savedInstanceState = new Bundle();
		mFragment.onCreate(savedInstanceState);
		verify(mFragment, atLeastOnce()).setListAdapter(any(ArrayAdapter.class));
	}
	
	@Test
	public void shouldHaveOptionsMenuInflated() throws Exception {
		assertThat(mFragment.isMenuVisible(), equalTo(true));
	}
	
	@Test
	public void shouldDetectAdminMenuItemSelected() throws Exception {
		MenuItem item = new TestMenuItem(R.id.menu_item_admin);
		ShadowActivity shadowActivity = Robolectric.shadowOf(mActivity);
		assertNotNull(shadowActivity);
		mFragment.onOptionsItemSelected(item);
		Intent startedIntent = shadowActivity.getNextStartedActivity();
		assertNotNull(startedIntent);
		ShadowIntent shadowIntent = Robolectric.shadowOf(startedIntent);
		assertThat(shadowIntent.getComponent().getClassName(), equalTo(AdminActivity.class.getName()));	
	}
	
	@Test  
	public void shouldDetectRefreshMenuItemSelected() throws Exception {
		MenuItem item = new TestMenuItem(R.id.menu_item_refresh);
		mFragment.onOptionsItemSelected(item);
		//TODO Test that an inner class instance was created
	}
	
	@Test
	public void shouldCallCreateTabsOnResume() throws Exception {
		mFragment.onResume();
		verify(mFragment, times(1)).createTabs();
	}
	
	@Test
	public void onCreateTabs() throws Exception {
		//TODO Implement 
	}
	
	@Test
	public void testInstrumentAdapterClass() throws Exception {
		//TODO Implement
	}
	
	@Test
	public void testSurveyAdapterClass() throws Exception {
		//TODO Implement
	}
	
	@Test
	public void shouldLoadInstrumentTaskOnListViewClick() throws Exception {
		//TODO Implement
	}
	
	@Test
	public void shouldLoadSurveyTaskOnListViewClick() throws Exception {
		//TODO Implement
	}
	
	@Test
	public void shouldDisplayPasswordPromptDialog() throws Exception {
		Whitebox.invokeMethod(mFragment, "displayPasswordPrompt");
		AlertDialog alert = ShadowAlertDialog.getLatestAlertDialog();
		ShadowAlertDialog sAlert = shadowOf(alert);
		assertThat(sAlert.getTitle().toString(), equalTo(mActivity.getString(R.string.password_title)));
	}
	
	/*@Test //TODO FIX
	public void shouldStartAdminActivityIfCorrectPassword() throws Exception {
		invokeMethod(iFragment, "displayPasswordPrompt");
		AlertDialog alert = ShadowAlertDialog.getLatestAlertDialog();
		ShadowAlertDialog sAlert = shadowOf(alert);
		//alert.show();
		//Button positiveButton = shadowOf(alert).getButton(AlertDialog.BUTTON_POSITIVE);
        //positiveButton.performClick();
		//Button okButton = sAlert.getButton(0);
		//ShadowButton sOkButton = shadowOf(okButton);
		//sAlert.clickOn(0);
		//sAlert.getLatestDialog();
	}*/
	
	/*@Test
	public void shouldCheckAdminPassword() throws Exception {
		boolean passwordCorrect = Whitebox.invokeMethod(mFragment, "checkAdminPassword", PASSWORD);
		assertTrue(passwordCorrect);
	}*/
	
	/*@Test 
	public void shouldShowErrorIfIncorrectPassword() throws Exception {
		//TODO Implement
	}*/
	
}
