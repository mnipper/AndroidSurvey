package org.adaptlab.chpir.android.survey;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.adaptlab.chpir.android.survey.Models.Survey;
import org.adaptlab.chpir.android.survey.QuestionFragments.FreeResponseQuestionFragment;
import org.adaptlab.chpir.android.survey.nonstaticclassimplementations.SubModel;
import org.adaptlab.chpir.android.survey.nonstaticclassimplementations.SubQuestion;
import org.adaptlab.chpir.android.survey.nonstaticclassimplementations.SubQuestionFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.activeandroid.Model;

@RunWith(RobolectricTestRunner.class)
@PrepareForTest({ Fragment.class, Question.class, SubQuestion.class, Model.class, SubModel.class, Survey.class, Instrument.class, Response.class })
public class QuestionFragmentTest {
	
	private static final long REMOTE_ID = 10L;
	private static final long SURVEY_ID = 10L;
	private SubQuestionFragment qFragment;
	private SurveyActivity activity;
	private SubQuestion question;
	private Survey survey;
	private Instrument instrument;
	private Response response;
	
	@Before
	public void onSetup() throws Exception {
		activity = Robolectric.buildActivity(SurveyActivity.class).create().get();
		setUpMocks();
		qFragment = new SubQuestionFragment();
        Bundle args = new Bundle();
        args.putLong(QuestionFragmentFactory.EXTRA_QUESTION_ID, REMOTE_ID);
        args.putLong(QuestionFragmentFactory.EXTRA_SURVEY_ID, SURVEY_ID);
        qFragment.setArguments(args);
		startFragment(qFragment);
	}
	
	private void setUpMocks() {
		question = mock(SubQuestion.class);
		survey = mock(Survey.class);
		instrument = mock(Instrument.class);
		survey = mock(Survey.class);
		instrument = mock(Instrument.class);
		response = mock(Response.class);
		when(question.findByRemoteIdNonStatic(REMOTE_ID)).thenReturn(question);
		SubModel model = mock(SubModel.class);
		when(model.loadNonStatic(Survey.class, REMOTE_ID)).thenReturn(survey);
		when(survey.getInstrument()).thenReturn(instrument);
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
		assertEquals(otherText.getEditableText(), R.string.other_specify_edittext);
	}
	
	@Test
	public void shouldHaveTextChangeListener() throws Exception {
		EditText otherText = new EditText(Robolectric.application);
		qFragment.addOtherResponseView(otherText);
		assertEquals(otherText.hasOnClickListeners(), true);
	}
	
	@Test
	public void shouldNotBeEnabled() throws Exception {
		EditText otherText = new EditText(Robolectric.application);
		qFragment.addOtherResponseView(otherText);
		assertEquals(otherText.isEnabled(), false);
	}
	
	@Test
	public void shouldReturnView() throws Exception {
		//View view = LayoutInflater.from(new SurveyActivity()).inflate(R.layout.fragment_survey, null);
		View v = qFragment.onCreateView(LayoutInflater.from(Robolectric.application), new LinearLayout(Robolectric.application), null);
		assertEquals(v.getId(), R.layout.fragment_question_factory);
	}
	
}
