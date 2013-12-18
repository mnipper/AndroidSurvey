package org.adaptlab.chpir.android.survey.Models;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricTestRunner;

import android.util.Log;

@RunWith(RobolectricTestRunner.class)
@PrepareForTest({ Option.class, Question.class, Instrument.class, Log.class, AdminSettings.class })
public class OptionTest {
	private static final String TEXT = "this text";
	private static final Long REMOTE_ID = 023121L;
	private static final String LANGUAGE = "en";
	
	private Option option;
	private Question question;
	private Question nextQuestion;
	private Instrument instrument;

	@Before
	public void onSetup() throws Exception {
		option = spy(new Option());
		question = mock(Question.class);
		nextQuestion = mock(Question.class);
		instrument = mock(Instrument.class);
		setUpStubbedMethods();
	}
	
	private void setUpStubbedMethods() {
		when(question.getInstrument()).thenReturn(instrument);
		when(instrument.getLanguage()).thenReturn(LANGUAGE);
	}

	@Test
	public void shouldSetAndGetQuestion() throws Exception {
		option.setQuestion(question);
		assertEquals(question, option.getQuestion());
	}
	
	@Test
	public void shouldSetAndGetRemoteId() throws Exception {
		option.setRemoteId(REMOTE_ID);
		assertEquals(REMOTE_ID, option.getRemoteId());
	}
	
	@Test 	
	public void shouldSetAndGetTextIfSameInstrumentLanguage() throws Exception {
		doReturn(LANGUAGE).when(option).getDeviceLanguage();
		option.setQuestion(question);
		option.setText(TEXT);
		assertEquals(TEXT, option.getText());
	}
	
	@Test
	public void shouldGetTranslationTextIfDifferentInstrumenLanguage() throws Exception {
		doReturn("sw").when(option).getDeviceLanguage();
		option.setQuestion(question);
		option.setText(TEXT);
		//TODO Implement
	}
	
	@Test
	public void shouldFindByRemoteId() throws Exception {
		//TODO Implement - FIX STATIC MOCKING
	}
	
	@Test
	public void shouldCreateObjectFromJson() throws Exception {
		//TODO: Implement
	}
	
	@Test
	public void shouldGetTranslationByLanguage() throws Exception {
		//TODO Implement
	}
	
	@Test
	public void shouldSetAndGetNextQuestion() throws Exception {
		String nextQuest = "nextQuestion";
		doReturn(nextQuestion).when(option).findByQuestionIdentifier(nextQuest);
		Whitebox.setInternalState(option, "mNextQuestion", nextQuest);
		option.getNextQuestion();
		verify(option, times(1)).findByQuestionIdentifier(nextQuest);
		assertEquals(nextQuestion, option.getNextQuestion());
	}
}
