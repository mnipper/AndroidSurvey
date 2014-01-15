package org.adaptlab.chpir.android.survey.Models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.adaptlab.chpir.android.survey.Models.Question.QuestionType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class QuestionTest {
	private static final String QUESTION_TYPE_ONE = "SELECT_ONE";
	private static final String QUESTION_ID = "9210TEST";
	private static final Long REMOTE_ID = 910289L;

	private Question question;
	private Instrument instrument;
	private Option option;
	private List<Option> optionsList;

	@Before
	public void onSetup() {
		question = spy(new Question());
		instrument = mock(Instrument.class);
		option = mock(Option.class);
		optionsList = new ArrayList<Option>();
	}

	@Test
	public void shouldSetAndGetQuestionText() throws Exception {
		//TODO FIX STATIC MOCKING
		//question.setText(QUESTION_TEXT);
		//assertEquals(QUESTION_TEXT, question.getText());
	}

	@Test
	public void shouldSetAndGetQuestionType() throws Exception {
		question.setQuestionType(QUESTION_TYPE_ONE);
		assertEquals(QuestionType.valueOf(QUESTION_TYPE_ONE),
				question.getQuestionType());
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
		// TODO FIX STATIC MOCKING
		/*
		 * PowerMockito.mockStatic(Question.class);
		 * doReturn(question).when(Question.findByRemoteId(REMOTE_ID));
		 * assertEquals(question, Question.findByRemoteId(REMOTE_ID));
		 */
	}

	@Test
	public void shouldFindQuestionByQuestionIdentifier() throws Exception {
		// TODO Fix Static Mocking
		/*
		 * question.setQuestionIdentifier(QUESTION_ID); assertEquals(question,
		 * Question.findByQuestionIdentifier(QUESTION_ID));
		 */
	}

	@Test
	public void shouldReturnListOfOptions() throws Exception {
		doReturn(optionsList).when(question).options();
		assertThat(question.options(), instanceOf(List.class));
		optionsList.add(option);
		when(option.getQuestion()).thenReturn(question);
		for (Option option : question.options()) {
			assertEquals(question, option.getQuestion());
		}
	}

	@Test
	public void shouldTestIfQuestionHasOptions() throws Exception {
		doReturn(optionsList).when(question).options();
		assertThat(question.hasOptions(), equalTo(false));
		optionsList.add(option);
		assertThat(question.hasOptions(), equalTo(true));
	}

	@Test
	public void shouldTestIfQuestionHasSkipPattern() throws Exception {
		optionsList.add(option);
		Question secondQuestion = mock(Question.class);
		doReturn(optionsList).when(question).options();
		when(option.getNextQuestion()).thenReturn(secondQuestion);
		when(secondQuestion.getQuestionIdentifier()).thenReturn(QUESTION_ID);
		assertThat(question.hasSkipPattern(), equalTo(true));
	}

	@Test
	public void shouldGetAllQuestions() throws Exception {
		/*
		 * assertThat(Question.getAll(), instanceOf(List.class)); //TODO FIX
		 * STATIC MOCKING Question quiz1 = mock(Question.class);
		 * verify(quiz1).save(); Question quiz2 = mock(Question.class);
		 * verify(quiz2).save(); assertThat(Question.getAll().size(),
		 * equalTo(2)); PowerMockito.verifyStatic();
		 */
	}

	@Test
	// TODO FIX
	public void shouldReturnValidQuestionType() throws Exception {
		Question quiz = spy(new Question());
		boolean bool1 = Whitebox.invokeMethod(quiz, "validQuestionType","SELECT_ONE");
		assertThat(bool1, equalTo(true));
		boolean bool2 = Whitebox.invokeMethod(quiz, "validQuestionType","COACH K");
		assertThat(bool2, equalTo(false));
	}

	@Test
	public void shouldCreateObjectFromJson() throws Exception {
		// TODO Implement
	}
	
	@Test
	public void shouldSetAndGetRegExpValidation() throws Exception {
		question.setRegExValidation("validate");
		assertEquals("validate", question.getRegExValidation());
	}

}
