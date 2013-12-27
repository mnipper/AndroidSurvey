package org.adaptlab.chpir.android.survey.QuestionFragments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.adaptlab.chpir.android.survey.QuestionFragmentFactory;
import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.SurveyActivity;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

@RunWith(RobolectricTestRunner.class)
public class FreeResponseQuestionFragmentTest {
	private static final String TEXT = "ALI BABA";
	private static final long REMOTE_ID = 10L;
	private static final long SURVEY_ID = 10L;
	private FreeResponseQuestionFragment questionFragment;
	private SurveyActivity activity;

	private void startFragment(FreeResponseQuestionFragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(fragment, null);
		fragmentTransaction.commit();
	}

	private void setBundleArgs() {
		Bundle args = new Bundle();
		args.putLong(QuestionFragmentFactory.EXTRA_QUESTION_ID, REMOTE_ID);
		args.putLong(QuestionFragmentFactory.EXTRA_SURVEY_ID, SURVEY_ID);
		questionFragment.setArguments(args);
	}

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(SurveyActivity.class).create()
				.get();
		questionFragment = spy(new FreeResponseQuestionFragment());
		doNothing().when(questionFragment).init();
		setBundleArgs();
		startFragment(questionFragment);
	}

	@Test
	public void fragmentShouldNotBeNull() throws Exception {
		assertNotNull(questionFragment);
	}

	@Test
	public void questionComponentShoudHaveOneChildView() throws Exception {
		ViewGroup qComponent = new LinearLayout(Robolectric.application);
		questionFragment.createQuestionComponent(qComponent);
		assertEquals(qComponent.getChildCount(), 1);
	}

	@Test
	public void questionComponentShouldHaveHintString() throws Exception {
		ViewGroup qComponent = new LinearLayout(Robolectric.application);
		questionFragment.createQuestionComponent(qComponent);
		String hintString = activity.getResources().getString(
				R.string.free_response_edittext);
		EditText text = (EditText) qComponent.getChildAt(0);
		assertEquals(hintString, text.getHint().toString());
	}

	@Test
	public void shouldHaveTextChangeListener() throws Exception {
		Response response = spy(new Response());
		when(questionFragment.getResponse()).thenReturn(response);
		ViewGroup qComponent = new LinearLayout(Robolectric.application);
		questionFragment.createQuestionComponent(qComponent);
		EditText text = (EditText) qComponent.getChildAt(0);
		text.setText(TEXT);
		verify(response, times(1)).setResponse(TEXT);
	}
}
