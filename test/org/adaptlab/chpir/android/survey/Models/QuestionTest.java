package org.adaptlab.chpir.android.survey.Models;

import java.util.LinkedList;

import org.adaptlab.chpir.android.survey.Models.Question.QuestionType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Instrument.class, Option.class })
public class QuestionTest extends ActiveAndroidTestBase {
	private static final String QUESTION_TEXT = "WHAT WHICH WHO";
	private static final String QUESTION_TYPE_ONE = "SELECT_ONE";
	private static final String QUESTION_ID = "9210TEST";
	private static final Long REMOTE_ID = 910289L;
	private static final String TABLE = "Questions";
	
	private Question question;
	private Instrument instrument;
	private Option option;

	@Override
	public void onSetup() {
		PowerMockito.mockStatic(Question.class);
		question = new Question();
		instrument = mock(Instrument.class);
		option = mock(Option.class);
		when(tableInfo.getTableName()).thenReturn(TABLE);
	}
	
	@Test
	public void shouldSetAndGetQuestionText() throws Exception {
		question.setText(QUESTION_TEXT);
		assertEquals(QUESTION_TEXT, question.getText());
	}
	
	@Test
	public void shouldSetAndGetQuestionType() throws Exception {
		question.setQuestionType(QUESTION_TYPE_ONE);
		assertEquals(QuestionType.valueOf(QUESTION_TYPE_ONE), question.getQuestionType());
	}
	
	@Test
	public void shouldSetAndGetQuestionIdentifier() throws Exception {
		question.setQuestionIdentifier(QUESTION_ID);
		assertEquals(QUESTION_ID, question.getQuestionIdentifier());
	}
	
	@Test
	public void shouldSetAndGetIntrumentOfQuestion() throws Exception {
		question.setInstrument(instrument);
		assertEquals(instrument, question.getInstrument()); 
	}
	
	@Test
	public void shouldSetAndGetRemoteId() throws Exception {
		question.setRemoteId(REMOTE_ID);
		assertEquals(REMOTE_ID, question.getRemoteId());
	}
	
	@Test
	public void shouldFindQuestionByRemoteId() throws Exception {
		question.setRemoteId(REMOTE_ID);
		assertEquals(question, Question.findByRemoteId(REMOTE_ID));
		PowerMockito.verifyStatic();
	}
	
	@Test
	public void shouldFindQuestionByQuestionIdentifier() throws Exception {
		question.setQuestionIdentifier(QUESTION_ID);
		assertEquals(question, Question.findByQuestionIdentifier(QUESTION_ID));
		PowerMockito.verifyStatic();
	}
	
	@Test
	public void shouldReturnListOfOptions() throws Exception {
		assertThat(question.options(), instanceOf(LinkedList.class));
		option.setQuestion(question);
		for(Option option : question.options()) {
			assertEquals(question, option.getQuestion());
		}
	}
	
	@Test
	public void shouldTestIfQuestionHasOptions() throws Exception {
		assertThat(question.hasOptions(), equalTo(false));
		option.setQuestion(question);
		assertThat(question.hasOptions(), equalTo(true)); 
	}
	
	@Test
	public void shouldTestIfQuestionHasSkipPattern() throws Exception {
		assertThat(question.hasSkipPattern(), equalTo(false));
		String nextQuestion = "next quiz id";
		Whitebox.invokeMethod(option, "setNextQuestion", nextQuestion);
		option.setQuestion(question);
		assertThat(question.hasSkipPattern(), equalTo(true)); 
	}
	
	@Test
	public void shouldGetAllQuestions() throws Exception {
		assertThat(Question.getAll(), instanceOf(LinkedList.class));
		Question quiz1 = mock(Question.class);
		verify(quiz1).save();
		Question quiz2 = mock(Question.class);
		verify(quiz2).save();
		assertThat(Question.getAll().size(), equalTo(2));	//TODO FIX ActiveAndroid errors
		PowerMockito.verifyStatic();
	}
	
	@Test //TODO FIX
	public void shouldReturnValidQuestionType() throws Exception {
		Question quiz = PowerMockito.spy(new Question()); 
		boolean bool1 = Whitebox.invokeMethod(quiz, "validQuestionType", "SELECT_ONE");
		assertThat(bool1, equalTo(true));
		boolean bool2 = Whitebox.invokeMethod(quiz, "validQuestionType", "COACH K");
		assertThat(bool2, equalTo(false));
	}
	
	@Test
	public void shouldCreateObjectFromJson() throws Exception {
		//TODO Implement
	}
	
}
