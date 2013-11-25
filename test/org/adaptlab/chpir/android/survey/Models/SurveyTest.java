package org.adaptlab.chpir.android.survey.Models;

import static org.junit.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;


@RunWith(RobolectricTestRunner.class)
public class SurveyTest {
	private Survey survey;
	private Instrument instrument;
	
	@Before
	public void setUp() {
		survey = new Survey();
		instrument = new Instrument();
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
	
	@Test	//TODO Currently failing
	public void shouldSetAndGetSameInstrument() throws Exception {
		survey.setInstrument(instrument);
		assertThat(instrument, equalTo(survey.getInstrument()));
	}
	
	@Test
	public void shouldReturnListOfResponses() throws Exception {
		//TODO Implement
		//assertThat(survey.responses(), instanceOf(List.class));
	}
	
	@Test
	public void shouldGetResponseByQuestion() throws Exception {
		//TODO Implement
	}
	
	@Test
	public void shouldMakeJsonObject() throws Exception {
		//TODO Implement
	}
}
