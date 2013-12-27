package org.adaptlab.chpir.android.survey;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.adaptlab.chpir.android.survey.Models.Survey;
import org.adaptlab.chpir.android.survey.QuestionFragments.FreeResponseQuestionFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

@RunWith(RobolectricTestRunner.class)
public class QuestionFragmentTest {
	
	private static final long REMOTE_ID = 10L;
	private static final long SURVEY_ID = 10L;
	private static final String TEXT = "ttext";
	private FreeResponseQuestionFragment qFragment;
	private SurveyActivity activity;
	private Question question;
	private Survey survey;
	private Instrument instrument;
	private Response response;
	
	@Before
	public void onSetup() throws Exception {
		activity = Robolectric.buildActivity(SurveyActivity.class).create().get();
		qFragment = spy(new FreeResponseQuestionFragment());
		doNothing().when(qFragment).init();
		setBundleArgs();
		startFragment(qFragment);
		setUpMocks();
		setFragmentMemberVariables();
	}
	
	private void setFragmentMemberVariables() {
		Whitebox.setInternalState(qFragment, "mQuestion", question);
		Whitebox.setInternalState(qFragment, "mSurvey", survey);
		Whitebox.setInternalState(qFragment, "mResponse", response);
		Whitebox.setInternalState(qFragment, "mInstrument", instrument);
	}

	private void setBundleArgs() {
		Bundle args = new Bundle();
        args.putLong(QuestionFragmentFactory.EXTRA_QUESTION_ID, REMOTE_ID);
        args.putLong(QuestionFragmentFactory.EXTRA_SURVEY_ID, SURVEY_ID);
        qFragment.setArguments(args);
	}
	
	private void setUpMocks() {
		question = mock(Question.class);
		survey = mock(Survey.class);
		instrument = mock(Instrument.class);
		response = mock(Response.class);
	}
	
	private void startFragment(QuestionFragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
	}
	
	@Test
	public void shouldGetQuestion() throws Exception {
		assertEquals(qFragment.getQuestion(), question);
	}
	
	@Test
	public void shouldGetSurvey() throws Exception {
		assertEquals(qFragment.getSurvey(), survey);
	}
	
	@Test
	public void shouldReturnResponse() throws Exception {
		assertEquals(qFragment.getResponse(), response);
	}
	
	@Test
	public void shouldReturnInstrument() throws Exception {
		assertEquals(qFragment.getInstrument(), instrument);
	}
	
	@Test
	public void shouldHaveSameResponseHint() throws Exception {
		EditText otherText = new EditText(Robolectric.application);
		qFragment.addOtherResponseView(otherText);
		assertEquals(otherText.getHint().toString(), activity.getResources().getString(R.string.other_specify_edittext));
	}
	
	@Test
	public void shouldHaveTextChangeListener() throws Exception {
		Response resp = spy(new Response());
		when(qFragment.getResponse()).thenReturn(resp);
		EditText otherText = new EditText(Robolectric.application);
		qFragment.addOtherResponseView(otherText);
		otherText.setText(TEXT);
		verify(qFragment, times(1)).saveOtherResponse(TEXT);
		assertEquals(otherText.getText(), TEXT);
	}
	
	@Test
	public void shouldNotBeEnabled() throws Exception {
		EditText otherText = new EditText(Robolectric.application);
		qFragment.addOtherResponseView(otherText);
		assertEquals(otherText.isEnabled(), false);
	}
	
	@Test
	public void shouldReturnView() throws Exception {
		assertThat(qFragment.onCreateView(LayoutInflater.from(Robolectric.application), new LinearLayout(Robolectric.application), null), instanceOf(View.class));
	}
	
	@Test
	public void shouldCallCreateQuestionComponent() throws Exception {
		View view = qFragment.onCreateView(LayoutInflater.from(Robolectric.application), new LinearLayout(Robolectric.application), null);
		ViewGroup qComponent = (LinearLayout) view.findViewById(R.id.question_component);
		verify(qFragment, times(1)).createQuestionComponent(qComponent);
	}
	
}
