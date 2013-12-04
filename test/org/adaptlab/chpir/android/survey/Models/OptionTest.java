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
public class OptionTest extends ActiveAndroidTestBase {
	private static final String TEXT = "this text";
	private static final Long REMOTE_ID = 023121L;
	private static final String TABLE = "Option";
	
	private Option option;
	private Question question;

	@Override
	public void onSetup() {
		option = new Option();
		question = mock(Question.class);
		when(tableInfo.getTableName()).thenReturn(TABLE);
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
	
	@Test 	//TODO Handle translations -- do mocks
	public void shouldSetAndGetText() throws Exception {
		option.setText(TEXT);
		assertEquals(TEXT, option.getText());
	}
	
	@Test	//TODO Mock return new Select() blah blah
	public void shouldFindByRemoteId() throws Exception {
		PowerMockito.mockStatic(Option.class);
		when(Option.findByRemoteId(REMOTE_ID)).thenReturn(option);	
		option.setRemoteId(REMOTE_ID);
		assertEquals(option, Option.findByRemoteId(REMOTE_ID));
		PowerMockito.verifyStatic();
	}
	
	@Test
	public void shouldCreateObjectFromJson() throws Exception {
		//TODO: Implement
	}
}
