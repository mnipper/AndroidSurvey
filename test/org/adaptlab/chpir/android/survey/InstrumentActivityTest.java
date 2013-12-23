package org.adaptlab.chpir.android.survey;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class InstrumentActivityTest {
	private InstrumentActivity activity;
	
	@Before
	public void setUp() {
		activity = Robolectric.buildActivity(InstrumentActivity.class).create().get();
	}
	
	@Test
	public void shouldInstrumentFragmentType() throws Exception {
		assertThat(activity.createFragment(), instanceOf(InstrumentFragment.class));
	}
}
