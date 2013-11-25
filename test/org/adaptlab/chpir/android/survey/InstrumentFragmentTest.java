package org.adaptlab.chpir.android.survey;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.tester.android.view.TestMenuItem;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(RobolectricTestRunner.class)
public class InstrumentFragmentTest {
	private InstrumentFragment instFragment;
	private InstrumentActivity activity;

	@Before
	public void setUp() throws Exception {
		startFragment();
	}
	
	public void startFragment() {
		activity = Robolectric.buildActivity(InstrumentActivity.class).create().start().resume().get();
		instFragment = (InstrumentFragment) activity.createFragment();
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    fragmentTransaction.add(instFragment, null );
	    fragmentTransaction.commit();
	}
	
	@Test
	public void shouldHaveOptionsMenuSetToTrue() throws Exception {
		assertThat(instFragment.hasOptionsMenu(), equalTo(true));
	}
	
	@Test
	public void shouldHaveOptionsMenuInflated() throws Exception {
		assertThat(instFragment.isMenuVisible(), equalTo(true));	//TODO check if this is the correct way to test whether menu has been inflated
	}
	
	@Test
	public void shouldDetectAdminMenuItemSelected() throws Exception {
		MenuItem item = new TestMenuItem() {
			public int getItemId() {
			    return R.id.menu_item_admin;
			  }
		};
		instFragment.onOptionsItemSelected(item);
		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
		Intent startedIntent = shadowActivity.getNextStartedActivity();
		ShadowIntent shadowIntent = Robolectric.shadowOf(startedIntent);
		assertThat(shadowIntent.getComponent().getClassName(), equalTo(AdminActivity.class.getName()));	
	}
	
	@Test 
	public void shouldDetectRefreshMenuItemSelected() throws Exception {
		MenuItem item = new TestMenuItem() {
			public int getItemId() {
			    return R.id.menu_item_refresh;
			  }
		};
		instFragment.onOptionsItemSelected(item);
		assertNotNull(instFragment.getListAdapter());
	}
	
	//TODO Write tests for remaining methods
	
}
