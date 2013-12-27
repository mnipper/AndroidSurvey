package org.adaptlab.chpir.android.survey;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Option;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Question.QuestionType;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.adaptlab.chpir.android.survey.Models.Survey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

@RunWith(RobolectricTestRunner.class)
public class SurveyFragmentTest {
	private static final String TEXT = "text";
	private static final QuestionType QUESTION_TYPE = QuestionType
			.valueOf("SELECT_ONE");
	private static final String FOLLOW_UP_TEXT = "text";
	private SurveyActivity activity;
	private SurveyFragment sFragment;
	private Question question;
	private Question followupQuestion;
	private Instrument instrument;
	private Option option;
	private Response response;
	private Survey survey;
	private Survey survey1;
	private LayoutInflater inflater;
	private ViewGroup parent;
	private List<Question> questionList;
	private List<Option> optionList;

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(SurveyActivity.class).create()
				.get();
		sFragment = spy(new SurveyFragment());
		setUpMocks();
		setUpPrivateMemberVariables();
		stubMockedObjectsMethods();
		startFragment(sFragment);
		setUpOnCreateViewParameters();
	}

	private void setUpMocks() {
		questionList = new LinkedList<Question>();
		optionList = new LinkedList<Option>();
		question = mock(Question.class);
		followupQuestion = mock(Question.class);
		instrument = mock(Instrument.class);
		survey = spy(new Survey());
		survey1 = mock(Survey.class);
		option = mock(Option.class);
		response = mock(Response.class);
	}

	private void setUpPrivateMemberVariables() {
		Whitebox.setInternalState(sFragment, "mQuestion", question);
		Whitebox.setInternalState(sFragment, "mSurvey", survey);
		Whitebox.setInternalState(sFragment, "mInstrument", instrument);
	}

	private void stubMockedObjectsMethods() {
		when(question.getQuestionType()).thenReturn(QUESTION_TYPE);
		doNothing().when(sFragment).createQuestionFragment();
		when(instrument.questions()).thenReturn(questionList);
		when(question.getText()).thenReturn(TEXT);
	}

	private void setUpOnCreateViewParameters() {
		parent = new LinearLayout(Robolectric.application);
		inflater = LayoutInflater.from(activity);
	}

	private void startFragment(SurveyFragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(fragment, null);
		fragmentTransaction.commit();
	}

	@Test
	public void fragmentShouldNotBeNull() {
		assertNotNull(sFragment);
	}

	@Test
	public void shouldReturnViewOnCreateView() throws Exception {
		assertThat(sFragment.onCreateView(inflater, parent, null),
				instanceOf(View.class));
	}

	@Test
	public void shouldHaveRightQuestionTextView() throws Exception {
		View view = sFragment.onCreateView(inflater, parent, null);
		TextView mQuestionText = (TextView) Whitebox.getInternalState(
				sFragment, "mQuestionText");
		assertEquals(view.findViewById(R.id.question_text), mQuestionText);
	}

	/*@Test //TODO Changed
	public void shouldHaveNextButtonView() throws Exception {
		View view = sFragment.onCreateView(inflater, parent, null);
		Button mNextButton = (Button) Whitebox.getInternalState(sFragment,"mNextButton");
		assertEquals(mNextButton, view.findViewById(R.string.next_button));
	}*/

	/*@Test //TODO Changed
	public void buttonShouldHaveFinishWhenOneQuestion() throws Exception {
		questionList.add(question);
		sFragment.onCreateView(inflater, parent, null);
		Button mNextButton = (Button) Whitebox.getInternalState(sFragment,"mNextButton");
		assertEquals(mNextButton.getText().toString(), activity.getResources()
				.getString(R.string.finish_button));
	}*/

	/*@Test //TODO CHANGED
	public void shouldHideSurveyActivityWhenFinishButtonPressedWhenOneQuestion()
			throws Exception {
		questionList.add(question);
		sFragment.onCreateView(inflater, parent, null);
		Button finishButton = (Button) Whitebox.getInternalState(sFragment,"mNextButton");
		finishButton.performClick();
		assertTrue(activity.isFinishing());
		verify(survey, times(1)).setAsComplete();
	}*/
	
	@Test
	public void innerIfStateOnClick() throws Exception {
		//TODO Write test
	}

	@Test
	public void shouldSetQuestionText() throws Exception {
		View v = sFragment.onCreateView(inflater, parent, null);
		TextView questionText = (TextView) v.findViewById(R.id.question_text);
		Whitebox.invokeMethod(sFragment, "setQuestionText", questionText);
		assertEquals(TEXT, questionText.getText().toString());
	}

	@Test
	public void shouldSetQuestionTextFollowUpQuestion() throws Exception {
		View v = sFragment.onCreateView(inflater, parent, null);
		TextView questionText = (TextView) v.findViewById(R.id.question_text);
		when(question.getFollowingUpQuestion()).thenReturn(followupQuestion);
		when(question.getFollowingUpText(survey)).thenReturn(FOLLOW_UP_TEXT);
		Whitebox.invokeMethod(sFragment, "setQuestionText", questionText);
		assertEquals(FOLLOW_UP_TEXT, questionText.getText().toString());
	}

	@Test
	public void shouldGetNextQuestion() throws Exception {
		questionList.add(question);
		questionList.add(followupQuestion);
		Question quest = Whitebox.invokeMethod(sFragment, "getNextQuestion", 0);
		assertEquals(quest, followupQuestion);
	}

	@Test
	public void shouldGetNextQuestionHasSkipPattern() throws Exception {
		optionList.add(option);
		setMockStateForSkipPattern("0");
		Question quest = Whitebox.invokeMethod(sFragment, "getNextQuestion", 0);
		assertEquals(quest, followupQuestion);
	}

	@Test
	public void shouldGetNextQuestionSkipPatternOther() throws Exception {
		optionList.add(option);
		questionList.add(question);
		questionList.add(followupQuestion);
		setMockStateForSkipPattern("1");
		Question quest = Whitebox.invokeMethod(sFragment, "getNextQuestion", 0);
		assertEquals(quest, followupQuestion);
	}

	private void setMockStateForSkipPattern(String index) {
		Whitebox.setInternalState(sFragment, "mSurvey", survey1);
		when(question.hasSkipPattern()).thenReturn(true);
		when(survey1.getResponseByQuestion(question)).thenReturn(response);
		when(response.getText()).thenReturn(index);
		when(question.options()).thenReturn(optionList);
		when(option.getNextQuestion()).thenReturn(followupQuestion);
	}

}
