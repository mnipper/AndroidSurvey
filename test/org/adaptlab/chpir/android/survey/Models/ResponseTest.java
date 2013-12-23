package org.adaptlab.chpir.android.survey.Models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
@PrepareForTest({ Response.class, Question.class, Survey.class })
public class ResponseTest {
	private static final String RESPONSE_TEXT = "main";
	private static final String OTHER_RESPONSE = "other";
	
	private Response response;
	private Question question;
	private Survey survey;
	
	@Before
	public void onSetup() {
		response = spy(new Response());
		question = mock(Question.class);
		survey = mock(Survey.class);
		PowerMockito.mockStatic(Response.class);
	}
	
	@Test
	public void shouldInitiallyNotBeSent() throws Exception {
		assertThat(response.isSent(), equalTo(false));
	}
	
	@Test
	public void shouldSetAndGetQuestion() throws Exception {
		response.setQuestion(question);
		assertEquals(question, response.getQuestion());
	}
	
	@Test
	public void shouldSetAndGetResponseText() throws Exception {
		response.setResponse(RESPONSE_TEXT);
		assertEquals(RESPONSE_TEXT, response.getText());
	}
	
	@Test
	public void shouldSetAndGetSurvey() throws Exception {
		response.setSurvey(survey);
		assertEquals(survey, response.getSurvey());
	}
	
	@Test
	public void shouldSetAndGetOtherReponse() throws Exception {
		response.setOtherResponse(OTHER_RESPONSE);
		assertEquals(OTHER_RESPONSE, response.getOtherResponse());
	}
	
	/*@Test //TODO FIX
	public void shouldSetAsSent() throws Exception {
		Response resp = spy(new Response());
		assertEquals(resp.isSent(), false);
		doNothing().when(resp).delete();
		resp.setAsSent();
		assertEquals(resp.isSent(), true);
	}*/
	
	@Test
	public void shouldReturnSurveyNotReadyToSend() throws Exception {
		Survey mySurvey = mock(Survey.class);
		when(mySurvey.readyToSend()).thenReturn(false);
		response.setSurvey(mySurvey);
		assertEquals(response.readyToSend(), false);
	}
	
	@Test
	public void shouldReturnSurveyReadyToSend() throws Exception {
		Survey mySurvey = mock(Survey.class);
		when(mySurvey.readyToSend()).thenReturn(true);
		response.setSurvey(mySurvey);
		assertEquals(response.readyToSend(), true);
	}
	
	@Test
	public void shouldReturnList() throws Exception {
		assertThat(Response.getAll(), instanceOf(ArrayList.class));
	}
	
	@Test
	public void shouldGetAll() throws Exception {
		/*when(select.from(Response.class)).thenReturn(1);
		mock(Response.class);
		assertThat(Response.getAll().size(), equalTo(1));*/
		//TODO FIX
	}
	
	@Test
	public void shouldCreateJsonObject() throws Exception {
		//TODO Implement
	}
}
