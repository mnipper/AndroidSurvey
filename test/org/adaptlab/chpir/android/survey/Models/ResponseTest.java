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
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doCallRealMethod;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Response.class, Question.class, Survey.class })
public class ResponseTest {
	private static final String RESPONSE_TEXT = "main";
	private static final String OTHER_REPONSE = "other";
	
	private Response response;
	private Question question;
	private Survey survey;
	
	@Before
	public void setUp() throws Exception {
		response = mock(Response.class);
		question = mock(Question.class);
		survey = mock(Survey.class);
		PowerMockito.mockStatic(Response.class);
	}
	
	@Test
	public void shouldInitiallyNotBeSent() throws Exception {
		assertThat(response.isSent(), equalTo(false));
	}
	
	@Test	//TODO Verify how verify is used
	public void shouldSetAndGetQuestion() throws Exception {
		response.setQuestion(question);
		when(response.getQuestion()).thenReturn(question);
		//verify(response).getQuestion(); 	
	}
	
	@Test	//TODO Figure out correct use of doCallRealMethod
	public void shouldSetAndGetResponseText() throws Exception {
		//response.setResponse(RESPONSE_TEXT);
		//when(response.getText()).thenReturn(RESPONSE_TEXT);
		doCallRealMethod().when(response).getText();
		doCallRealMethod().when(response).setResponse(RESPONSE_TEXT);
		response.setResponse(RESPONSE_TEXT);
		when(response.getText()).thenReturn(RESPONSE_TEXT);
	}
	
	@Test
	public void shouldSetAndGetSurvey() throws Exception {
		response.setSurvey(survey);
		when(response.getSurvey()).thenReturn(survey);
	}
	
	@Test
	public void shouldSetAndGetOtherReponse() throws Exception {
		response.setOtherResponse(OTHER_REPONSE);
		when(response.getOtherResponse()).thenReturn(OTHER_REPONSE);
	}
	
	@Test
	public void shouldSetAsSent() throws Exception {
		when(response.isSent()).thenReturn(false);
		response.setAsSent();
		when(response.isSent()).thenReturn(true);
	}
	
	@Test	//TODO FIX
	public void shouldCheckIfSurveyIsReadyToSend() throws Exception {
		when(response.readyToSend()).thenReturn(false);
		survey.setAsComplete();
		response.setSurvey(survey);
		when(response.readyToSend()).thenReturn(true);
	}
	
	@Test
	public void shouldGetAll() throws Exception {
		assertThat(Response.getAll(), instanceOf(LinkedList.class));
		Response r = mock(Response.class);
		verify(r).save(); //TODO ActiveAndroid fails to open db
		assertThat(Response.getAll().size(), equalTo(1));
	}
	
	@Test
	public void shouldCreateJsonObject() throws Exception {
		//TODO Implement
	}
}
