package org.adaptlab.chpir.android.survey;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.robolectric.Robolectric.shadowOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import android.support.v4.app.Fragment;

@RunWith(RobolectricTestRunner.class)
public class SingleFragmentActivityTest {
	private TestSingleFragmentActivity activityFragment;
	
	@Before
	public void setUp() {
		activityFragment = Robolectric.buildActivity(TestSingleFragmentActivity.class).create().get();
	}
	
	@Test
	public void shouldReturnLayoutResId() throws Exception {
		assertNotNull(activityFragment.getLayoutResId());	//TODO Not sure if this is the right test. Should test if Id is correct...
	}
	
	@Test
	public void shouldSetTheLayoutOnCreate() throws Exception {
		assertEquals(R.id.fragmentContainer, shadowOf(activityFragment).getContentView().getId());
	}
	
	static class TestSingleFragmentActivity extends SingleFragmentActivity {
		@Override
		protected Fragment createFragment() {
			return new SurveyFragment();
		}	
	}

}
