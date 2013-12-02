package org.adaptlab.chpir.android.survey.Models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class SurveyTest {
	private Survey survey;

	@Before
	public void setUp() {
		survey = new Survey();
	}

	@Test
	public void testInitialState() throws Exception {
		assertThat(survey.isSent(), equalTo(false));
		assertThat(survey.readyToSend(), equalTo(false));
		String mUUID = UUID.randomUUID().toString();
		assertThat(survey.getUUID(), not(mUUID));
	}

	@Test
	public void shouldReturnUUID() throws Exception {
		assertNotNull(survey.getUUID());
	}

	@Test
	public void shouldSetAsSent() throws Exception {
		survey.setAsSent();
		assertThat(survey.isSent(), equalTo(true));
	}

	@Test
	public void shouldSetAsComplete() throws Exception {
		survey.setAsComplete();
		assertThat(survey.readyToSend(), equalTo(true));
	}

}
