package org.adaptlab.chpir.android.survey.Models;

import java.util.LinkedList;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {Survey.class, Instrument.class, Response.class, Question.class, AdminSettings.class} )
public class SurveyMock {
	private static final String RESPONSE_TEXT = "This is the response";
	private static final Long REMOTE_ID = 12382903L;
	private static final String DEVICE_ID = "ThisIsTheDeviceId";
	
	private Survey survey;
	private Instrument instrument;
	private Response response;
	private Question question;
	private AdminSettings adminSettings;
	
	@Before
	public void setUp() throws Exception {
		survey = mock(Survey.class);
		instrument = mock(Instrument.class);
		response = mock(Response.class);
		question = mock(Question.class);
		adminSettings = mock(AdminSettings.class);
		PowerMockito.mockStatic(AdminSettings.class);
	}

	@Test
	public void shouldSetAndGetSameInstrument() throws Exception {
		survey = mock(Survey.class);
		instrument = mock(Instrument.class);
		survey.setInstrument(instrument);
		when(survey.getInstrument()).thenReturn(instrument);
	}
	
	@Test
	public void shouldSetAndGetResponseList() throws Exception {
		response.setResponse(RESPONSE_TEXT);
		response.setSurvey(survey);
		assertThat(survey.responses(), instanceOf(LinkedList.class));
		for(Response r : survey.responses()) {
			assertThat(r.getText(), equalTo(RESPONSE_TEXT));
		}
	}
	
	@Test
	public void shouldTestGetResponseByQuestion() throws Exception {
		response.setQuestion(question);
		response.setSurvey(survey);
		when(survey.getResponseByQuestion(question)).thenReturn(response); 
	}
	
	@Test	//TODO survey.toJSON() currently returns null
	public void shouldTestJsonObjectCreation() throws Exception {
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
