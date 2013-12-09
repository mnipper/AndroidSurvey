package org.adaptlab.chpir.android.survey;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Survey;
import org.adaptlab.chpir.android.survey.QuestionFragments.FreeResponseQuestionFragment;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.activeandroid.Model;

@RunWith(RobolectricTestRunner.class)
@PrepareForTest({ Fragment.class, Question.class, Model.class, Survey.class, Instrument.class })
public class QuestionFragmentTest {
	@Rule
    public PowerMockRule powerMockRule = new PowerMockRule();
	
	private static final long REMOTE_ID = 10L;
	private QuestionFragment qFragment;
	private SurveyActivity activity;
	private Question question;
	private Survey survey;
	private Instrument instrument;
	
	@Before
	public void onSetup() throws Exception {
		activity = Robolectric.buildActivity(SurveyActivity.class).create().get();
		setUpMocks();
		qFragment = new FreeResponseQuestionFragment();
        Bundle args = new Bundle();
        args.putLong(QuestionFragmentFactory.EXTRA_QUESTION_ID, new Long(1));
        args.putLong(QuestionFragmentFactory.EXTRA_SURVEY_ID, new Long(1));
        qFragment.setArguments(args);
		startFragment(qFragment);
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

	private void setUpMocks() {
		question = mock(Question.class);
		survey = mock(Survey.class);
		instrument = mock(Instrument.class);
		question = mock(Question.class);
		survey = mock(Survey.class);
		instrument = mock(Instrument.class);
		PowerMockito.mockStatic(Question.class);
		PowerMockito.when(Question.findByRemoteId(REMOTE_ID)).thenReturn(question);
		PowerMockito.mockStatic(Model.class);
		when(Model.load(Survey.class, REMOTE_ID)).thenReturn(survey);
		when(survey.getInstrument()).thenReturn(instrument);
	}
	
}
