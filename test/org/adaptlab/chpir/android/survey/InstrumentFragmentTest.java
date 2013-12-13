package org.adaptlab.chpir.android.survey;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
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
@PrepareForTest({ InstrumentFragment.class })
public class InstrumentFragmentTest {
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
	
	@Test	//TODO FIX
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
	}
	
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
	
	//TODO Write tests for remaining methods
	
}
