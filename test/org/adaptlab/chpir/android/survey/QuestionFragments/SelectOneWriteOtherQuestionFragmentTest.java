package org.adaptlab.chpir.android.survey.QuestionFragments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.adaptlab.chpir.android.survey.InstrumentActivity;
import org.adaptlab.chpir.android.survey.QuestionFragmentFactory;
import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Option;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

@RunWith(RobolectricTestRunner.class)
public class SelectOneWriteOtherQuestionFragmentTest {
	private static final long REMOTE_ID = 10L;
	private static final long SURVEY_ID = 10L;
	private SelectOneWriteOtherQuestionFragment qFragment;
	private InstrumentActivity activity;
	private Instrument instrument;
	private Question question;
	private Option option;
	private RadioGroup radioGroup;

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(InstrumentActivity.class).create()
				.get();
		qFragment = spy(new SelectOneWriteOtherQuestionFragment());
		doNothing().when(qFragment).init();
		setBundleArgs();
		startFragment(qFragment);
	}

	private void startFragment(SelectOneWriteOtherQuestionFragment fragment) {
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
		qFragment.setArguments(args);
	}

	private void setQuestionComponentView() {
		ViewGroup qComponent = new LinearLayout(Robolectric.application);
		qFragment.beforeAddViewHook(qComponent);
	}

	private void setUpMocks() {
		instrument = mock(Instrument.class);
		question = mock(Question.class);
		option = mock(Option.class);
		radioGroup = mock(RadioGroup.class);
	}

	private void stubMockMethods() {
		when(qFragment.getInstrument()).thenReturn(instrument);
		when(instrument.getTypeFace(Robolectric.application)).thenReturn(
				Typeface.DEFAULT);
		when(qFragment.getQuestion()).thenReturn(question);
		List<Option> list = new ArrayList<Option>();
		list.add(option);
		when(question.options()).thenReturn(list);
		when(qFragment.getRadioGroup()).thenReturn(radioGroup);
		when(radioGroup.getChildCount()).thenReturn(1);
	}

	@Test
	public void fragmentShouldNotBeNull() throws Exception {
		assertNotNull(qFragment);
	}

	@Test
	public void radioGroupShouldHaveRadioButton() throws Exception {
		setUpMocks();
		stubMockMethods();
		setQuestionComponentView();
		assertEquals(qFragment.getRadioGroup().getChildCount(), 1);
	}

	@Test
	public void shouldCallAddOtherResponseView() throws Exception {
		setUpMocks();
		stubMockMethods();
		setQuestionComponentView();
		verify(qFragment, times(1)).addOtherResponseView(any(EditText.class));
	}

	@Test
	public void shouldCallAddView() throws Exception {
		setUpMocks();
		stubMockMethods();
		ViewGroup qComponent = spy(new LinearLayout(Robolectric.application));
		qFragment.beforeAddViewHook(qComponent);
		verify(qComponent, times(1)).addView(any(EditText.class));
	}

	// TODO Maybe test added radioButton

}
