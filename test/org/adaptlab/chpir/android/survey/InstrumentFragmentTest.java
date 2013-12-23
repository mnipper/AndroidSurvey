package org.adaptlab.chpir.android.survey;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.powermock.reflect.Whitebox.invokeMethod;
import static org.robolectric.Robolectric.shadowOf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.tester.android.view.TestMenuItem;

import android.app.AlertDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

@RunWith(RobolectricTestRunner.class)
@PrepareForTest({ InstrumentFragment.class })
public class InstrumentFragmentTest {
	private static final String PASSWORD = "password";
	private InstrumentFragment iFragment;
	private InstrumentActivity activity;

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(InstrumentActivity.class).create().get();
		iFragment = spy(new InstrumentFragment());
		startFragment();
	}
	
	public void startFragment() {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    fragmentTransaction.add(iFragment, null );
	    fragmentTransaction.commit();
	}
	
	@Test
	public void shouldHaveOptionsMenuSetToTrue() throws Exception {
		assertThat(iFragment.hasOptionsMenu(), equalTo(true));
	}
	
	@Test
	public void shouldHaveOptionsMenuInflated() throws Exception {
		assertThat(iFragment.isMenuVisible(), equalTo(true));
	}
	
	/*@Test	//TODO FIX
	public void shouldDetectAdminMenuItemSelected() throws Exception {
		MenuItem item = new TestMenuItem() {
			public int getItemId() {
			    return R.id.menu_item_admin;
			  }
		};
		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
		iFragment.onOptionsItemSelected(item);
		Intent startedIntent = shadowActivity.getNextStartedActivity();
		ShadowIntent shadowIntent = Robolectric.shadowOf(startedIntent);
		assertThat(shadowIntent.getComponent().getClassName(), equalTo(AdminActivity.class.getName()));	
	}*/
	
	@Test 
	public void shouldDetectRefreshMenuItemSelected() throws Exception {
		MenuItem item = new TestMenuItem() {
			public int getItemId() {
			    return R.id.menu_item_refresh;
			  }
		};
		iFragment.onOptionsItemSelected(item);
		assertNotNull(iFragment.getListAdapter());
	}
	
	/*@Test
	public void testAppInitMethod() {
		// TODO Implement
	}*/
	
	/*@Test //TODO FIX - Currently un-encrypted
	public void runDeviceSecurityChecks() throws Exception {
		boolean encrypted = invokeMethod(iFragment, "runDeviceSecurityChecks");
		assertFalse(encrypted);	
	}*/
	
	@Test
	public void shouldCheckAdminPassword() throws Exception {
		boolean passwordCorrect = invokeMethod(iFragment, "checkAdminPassword", PASSWORD);
		assertTrue(passwordCorrect);
	}
	
	@Test
	public void shouldDisplayPasswordPromptDialog() throws Exception {
		invokeMethod(iFragment, "displayPasswordPrompt");
		AlertDialog alert = ShadowAlertDialog.getLatestAlertDialog();
		ShadowAlertDialog sAlert = shadowOf(alert);
		assertThat(sAlert.getTitle().toString(), equalTo(activity.getString(R.string.password_title)));
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
	public void shouldShowErrorIfIncorrectPassword() throws Exception {
		//TODO Implement
	}*/
	
}
