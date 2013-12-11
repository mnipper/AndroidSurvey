package org.adaptlab.chpir.android.survey.Models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;

import android.util.Log;

@RunWith(RobolectricTestRunner.class)
@PrepareForTest( {Survey.class, Instrument.class, Response.class, Question.class, AdminSettings.class, JSONObject.class, Log.class } )
public class SurveyTest { 
	private static final String RESPONSE_TEXT = "This is the response";
	private static final Long REMOTE_ID = 12382903L;
	private static final String DEVICE_ID = "ThisIsTheDeviceId";
	private static final Integer VERSION_NUMBER = 34234;
	
	private Survey survey;
	private Instrument instrument;
	private Response response;
	private Question question;
	private Survey spySurvey;
	private JSONObject json;
	
	@Before
	public void onSetup() {
		survey = new Survey();
		instrument = mock(Instrument.class);
		response = mock(Response.class);
		question = mock(Question.class);
		spySurvey = spy(new Survey());
	}

	@Test
	public void shouldNotBeSent() throws Exception {
		assertThat(survey.isSent(), equalTo(false));
	}
	
	@Test 
	public void shouldNotBeReadyToSend() throws Exception {
		assertThat(survey.readyToSend(), equalTo(false));
	}
	
	@Test
	public void shouldGetUniqueUUID() throws Exception {
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
	
	@Test
	public void shouldSetAndGetSameInstrument() throws Exception {
		survey.setInstrument(instrument);
		assertEquals(instrument, survey.getInstrument());
	}
	
	@Test
	public void shouldSetResponsesAndGetListOfResponse() throws Exception {
		Response resp = mock(Response.class);
		when(resp.getText()).thenReturn(RESPONSE_TEXT);
		LinkedList<Response> list = new LinkedList<Response>();
		list.add(resp);
		Survey sur = spy(new Survey());
		resp.setSurvey(sur);
		doReturn(list).when(sur).responses();
		for(Response r : sur.responses()) {
			assertThat(r.getText(), equalTo(RESPONSE_TEXT));
		}
	}
	
	@Test
	public void shouldNotReturnAnyResponse() throws Exception {
		LinkedList<Response> list = new LinkedList<Response>();
		list.add(mock(Response.class));
		Survey survey1 = spy(new Survey());
		doReturn(list).when(survey1).responses();
		assertNull(survey1.getResponseByQuestion(mock(Question.class)));
	}
	
	@Test
	public void shouldReturnResponseBasedOnQuestion() throws Exception {
		LinkedList<Response> list = new LinkedList<Response>();
		list.add(response);
		Survey survey1 = spy(new Survey());
		doReturn(list).when(survey1).responses();
		doReturn(question).when(response).getQuestion();
		assertEquals(response, survey1.getResponseByQuestion(question)); 
	}
	/*	TODO
	 * 1. Stub static correctly - AdminSettings
	 * 2. JSONOBject not saving properly
	*/
	private void setUpJson() {
		doReturn(instrument).when(spySurvey).getInstrument();
		when(instrument.getRemoteId()).thenReturn(REMOTE_ID);
		when(instrument.getVersionNumber()).thenReturn(VERSION_NUMBER);
		AdminSettings admin = spy(new AdminSettings());
		doReturn(admin).when(AdminSettings.class);
		doReturn(DEVICE_ID).when(admin).getDeviceIdentifier();
		json = spySurvey.toJSON();
	}
	
	@Test
	public void shouldHaveJsonStringInstrumentId() throws Exception {
		setUpJson();
		assertEquals(REMOTE_ID.toString(), json.getString("instrument_id"));
	}
	
	@Test
	public void shouldHaveJsonStringInstrumentVersionNumber() throws Exception {
		setUpJson();
		assertEquals(VERSION_NUMBER.toString(), json.getString("instrument_version_number"));
	}
	
	@Test
	public void shouldHaveJsonStringDeviceIdentifier() throws Exception {
		setUpJson();
		assertEquals(DEVICE_ID, json.getString("device_identifier"));
	}
	
	@Test
	public void shouldHaveJsonStringUuid() throws Exception {
		setUpJson();
		assertNotNull(json.getString("uuid"));
	}
}
