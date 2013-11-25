package org.adaptlab.chpir.android.survey.Models;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class InstrumentTest {
	private Instrument instrument;
	private static final String TITLE = "myTitle";
	private static final Long REMOTE_ID = 12382903L;
	
	@Before
	public void setUp() {
		instrument = new Instrument();
	}
	
	@Test
	public void shouldReturnTitle() throws Exception {
		instrument.setTitle(TITLE);
		assertThat(instrument.getTitle(), equalTo(TITLE));
	}
	
	@Test
	public void shouldReturnRemoteId() throws Exception {
		instrument.setRemoteId(REMOTE_ID);
		assertThat(instrument.getRemoteId(), equalTo(REMOTE_ID));
	}
	
}
