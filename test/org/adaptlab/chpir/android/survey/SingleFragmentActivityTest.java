package org.adaptlab.chpir.android.survey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Robolectric.shadowOf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

@RunWith(RobolectricTestRunner.class)
public class SingleFragmentActivityTest {
	private TestSingleFragmentActivity activityFragment;
	
	@Before
	public void setUp() {
		activityFragment = Robolectric.buildActivity(TestSingleFragmentActivity.class).create().get();
	}
	
	@Test
	public void shouldReturnLayoutResId() throws Exception {
		assertEquals(activityFragment.getLayoutResId(), R.layout.activity_fragment);
	}
	
	@Test
	public void shouldSetTheLayoutOnCreate() throws Exception {
		assertEquals(R.id.fragmentContainer, shadowOf(activityFragment).getContentView().getId());
	}
	
	@Test
	public void shouldHaveFragment() throws Exception {
		FragmentManager fm = activityFragment.getSupportFragmentManager();
        assertNotNull(fm.findFragmentById(R.id.fragmentContainer));
	}
	
	static class TestSingleFragmentActivity extends SingleFragmentActivity {
		@Override
		protected Fragment createFragment() {
			return new SurveyFragment();
		}	
	}

}
