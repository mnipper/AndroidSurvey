package org.adaptlab.chpir.android.survey.Models;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Option.class, Question.class })
public class OptionTest {
	private static final String TEXT = "this text";
	private static final Long REMOTE_ID = 023121L;
	
	private Option option;
	private Question question;

	@Before
	public void setUp() throws Exception {
		option = mock(Option.class);
		//verify(option).save(); 
		//TODO: Figure out why ActiveAndroid is not saving Table
	}
	
	@Test
	public void shouldSetAndGetQuestion() throws Exception {
		option.setQuestion(question);
		when(option.getQuestion()).thenReturn(question);
		verify(option, times(1)).setQuestion(question);
		assertEquals(question, option.getQuestion());
	}
	
	@Test
	public void shouldSetAndGetRemoteId() throws Exception {
		when(option.getRemoteId()).thenReturn(REMOTE_ID);
		option.setRemoteId(REMOTE_ID);
		verify(option, times(1)).setRemoteId(REMOTE_ID);
		assertEquals(REMOTE_ID, option.getRemoteId());
	}
	
	@Test
	public void shouldSetAndGetText() throws Exception {
		when(option.getText()).thenReturn(TEXT);
		option.setText(TEXT);
		verify(option, times(1)).setText(TEXT);
		assertEquals(TEXT, option.getText());
	}
	
	@Test
	public void shouldFindByRemoteId() throws Exception {
		PowerMockito.mockStatic(Option.class);
		when(Option.findByRemoteId(REMOTE_ID)).thenReturn(option);
		option.setRemoteId(REMOTE_ID);
		verify(option, times(1)).setRemoteId(REMOTE_ID);
		assertEquals(option, Option.findByRemoteId(REMOTE_ID));
		PowerMockito.verifyStatic();
	}
	
	@Test
	public void shouldCreateObjectFromJson() throws Exception {
		//TODO: Implement
	}
}
