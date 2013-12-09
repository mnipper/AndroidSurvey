package org.adaptlab.chpir.android.survey.Models;

import java.util.LinkedList;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;

import android.util.Log;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {Survey.class, Instrument.class, Response.class, Question.class, AdminSettings.class, JSONObject.class, Log.class } )
public class SurveyTest extends ActiveAndroidTestBase {
	private static final String RESPONSE_TEXT = "This is the response";
	private static final Long REMOTE_ID = 12382903L;
	private static final String DEVICE_ID = "ThisIsTheDeviceId";
	private static final String TABLE = "Surveys";
	
	private Survey survey;
	private Instrument instrument;
	private Response response;
	private Question question;
	private AdminSettings adminSettings;
	
	@Override
	public void onSetup() {
		survey = new Survey();
		instrument = mock(Instrument.class);
		response = mock(Response.class);
		question = mock(Question.class);
		adminSettings = mock(AdminSettings.class);
		PowerMockito.mockStatic(AdminSettings.class);
		when(tableInfo.getTableName()).thenReturn(TABLE);
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
	public void shouldReturnList() throws Exception {
		assertThat(survey.responses(), instanceOf(LinkedList.class));
	}
	
	@Test
	public void shouldSetAndGetResponse() throws Exception {
		when(response.getText()).thenReturn(RESPONSE_TEXT);
		response.setSurvey(survey);
		for(Response r : survey.responses()) {
			assertThat(r.getText(), equalTo(RESPONSE_TEXT));
		}
	}
	
	@Test
	public void shouldTestGetResponseByQuestion() throws Exception {
		response.setQuestion(question);
		response.setSurvey(survey);
		assertEquals(response, survey.getResponseByQuestion(question)); 
	}
	
	@Test	//TODO survey.toJSON() currently returns null
	public void shouldTestJsonObjectCreation() throws Exception {
		PowerMockito.mockStatic(JSONObject.class);
		PowerMockito.mockStatic(Log.class);
		adminSettings.setDeviceIdentifier(DEVICE_ID);
		instrument.setRemoteId(REMOTE_ID);
		survey.setInstrument(instrument);
		
		assertThat(survey.toJSON(), instanceOf(JSONObject.class));
		
		JSONObject json = survey.toJSON();
		assertThat(json.getString("instrument_id"), equalTo(REMOTE_ID.toString()));
		assertThat(json.getString("device_identifier"), equalTo(DEVICE_ID));
		assertNotNull(json.getString("uuid"));
	}
}
