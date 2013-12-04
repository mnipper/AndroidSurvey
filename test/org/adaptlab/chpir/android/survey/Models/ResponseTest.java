package org.adaptlab.chpir.android.survey.Models;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doCallRealMethod;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Response.class, Question.class, Survey.class })
public class ResponseTest extends ActiveAndroidTestBase {
	private static final String RESPONSE_TEXT = "main";
	private static final String OTHER_RESPONSE = "other";
	private static final String TABLE = "Responses";
	
	private Response response;
	private Question question;
	private Survey survey;
	
	@Override
	public void onSetup() {
		response = new Response();
		question = mock(Question.class);
		survey = mock(Survey.class);
		PowerMockito.mockStatic(Response.class);
		when(tableInfo.getTableName()).thenReturn(TABLE);
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
	
	@Test
	public void shouldSetAsSent() throws Exception {
		assertEquals(response.isSent(), false);
		response.setAsSent();
		assertEquals(response.isSent(), true);
	}
	
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
		assertThat(Response.getAll(), instanceOf(LinkedList.class));
	}
	
	@Test
	public void shouldGetAll() throws Exception {
		//when(select.from(Response.class)).thenReturn(1);
		mock(Response.class);
		assertThat(Response.getAll().size(), equalTo(1));
	}
	
	@Test
	public void shouldCreateJsonObject() throws Exception {
		//TODO Implement
	}
}
