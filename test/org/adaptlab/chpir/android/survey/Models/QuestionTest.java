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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Instrument.class, Question.class, Option.class })
public class QuestionTest {
	private static final String QUESTION_TEXT = "WHAT WHICH WHO";
	private static final String QUESTION_TYPE_ONE = "SELECT_ONE";
	private static final String QUESTION_ID = "9210TEST";
	private static final Long REMOTE_ID = 910289L;
	
	private Question question;
	private Instrument instrument;
	private Option option;

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(Question.class);
		question = mock(Question.class);
		instrument = mock(Instrument.class);
		option = mock(Option.class);
	}
	
	@Test
	public void shouldSetAndGetQuestionText() throws Exception {
		question.setText(QUESTION_TEXT);
		when(question.getText()).thenReturn(QUESTION_TEXT);
	}
	
	@Test
	public void shouldSetAndGetQuestionType() throws Exception {
		question.setQuestionType(QUESTION_TYPE_ONE);
		when(question.getQuestionType()).thenReturn(QuestionType.valueOf(QUESTION_TYPE_ONE));
	}
	
	@Test
	public void shouldSetAndGetQuestionIdentifier() throws Exception {
		question.setQuestionIdentifier(QUESTION_ID);
		when(question.getQuestionIdentifier()).thenReturn(QUESTION_ID);
	}
	
	@Test
	public void shouldSetAndGetIntrumentOfQuestion() throws Exception {
		question.setInstrument(instrument);
		when(question.getInstrument()).thenReturn(instrument);
		assertThat(question.getInstrument(), equalTo(instrument)); //Just checking
	}
	
	@Test
	public void shouldSetAndGetRemoteId() throws Exception {
		question.setRemoteId(REMOTE_ID);
		when(question.getRemoteId()).thenReturn(REMOTE_ID);
	}
	
	@Test
	public void shouldFindQuestionByRemoteId() throws Exception {
		question.setRemoteId(REMOTE_ID);
		when(Question.findByRemoteId(REMOTE_ID)).thenReturn(question);
	}
	
	@Test
	public void shouldFindQuestionByQuestionIdentifier() throws Exception {
		question.setQuestionIdentifier(QUESTION_ID);
		when(Question.findByQuestionIdentifier(QUESTION_ID)).thenReturn(question);
	}
	
	@Test
	public void shouldReturnListOfOptions() throws Exception {
		assertThat(question.options(), instanceOf(LinkedList.class));
		option.setQuestion(question);
		for(Option option : question.options()) {
			when(option.getQuestion()).thenReturn(question);
		}
	}
	
	@Test
	public void shouldTestIfQuestionHasOptions() throws Exception {
		assertThat(question.hasOptions(), equalTo(false));
		option.setQuestion(question);
		assertThat(question.hasOptions(), equalTo(true)); //TODO WHY FAILS???
	}
	
	@Test
	public void shouldTestIfQuestionHasSkipPattern() throws Exception {
		assertThat(question.hasSkipPattern(), equalTo(false));
		//Question nextQuestion = mock(Question.class);
		String nextQuestion = "next quiz id";
		Whitebox.invokeMethod(option, "setNextQuestion", nextQuestion);
		option.setQuestion(question);
		assertThat(question.hasSkipPattern(), equalTo(true)); //TODO WHY FAILS?
	}
	
	@Test
	public void shouldGetAllQuestions() throws Exception {
		assertThat(Question.getAll(), instanceOf(LinkedList.class));
		Question quest1 = mock(Question.class);
		Question quest2 = mock(Question.class);
		assertThat(Question.getAll().size(), equalTo(2));	//TODO FIX
	}
	
	@Test //TODO ARRRGGGRRGGRGR!
	public void shouldReturnValidQuestionType() throws Exception {
		Question quiz = mock(Question.class);
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
