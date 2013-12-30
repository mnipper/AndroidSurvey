package org.adaptlab.chpir.android.survey.QuestionFragments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
import android.widget.CheckBox;
import android.widget.LinearLayout;

@RunWith(RobolectricTestRunner.class)
public class SelectMultipleQuestionFragmentTest {
	private static final long REMOTE_ID = 10L;
	private static final long SURVEY_ID = 10L;
	private static final String TEXT = "text";
	private InstrumentActivity activity;
	private SelectMultipleQuestionFragment qFragment;
	private Question question;
	private Option option;
	private Instrument instrument;
	private List<Option> options;
	private ViewGroup qComponent;

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(InstrumentActivity.class).create()
				.get();
		qFragment = spy(new SelectMultipleQuestionFragment());
		doNothing().when(qFragment).init();
		setBundleArgs();
		startFragment(qFragment);
		setUpMocks();
	}

	private void startFragment(SelectMultipleQuestionFragment fragment) {
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

	private void setUpMocks() {
		question = mock(Question.class);
		option = mock(Option.class);
		instrument = mock(Instrument.class);
		options = new ArrayList<Option>();
	}

	private void stubMockMethods() {
		when(qFragment.getQuestion()).thenReturn(question);
		when(question.options()).thenReturn(options);
		when(option.getText()).thenReturn(TEXT);
		when(qFragment.getInstrument()).thenReturn(instrument);
		when(instrument.getTypeFace(Robolectric.application)).thenReturn(
				Typeface.DEFAULT);
	}

	private void createQuestionComponentView() {
		qComponent = new LinearLayout(Robolectric.application);
		qFragment.createQuestionComponent(qComponent);
	}

	private void setTestComponents() {
		options.add(option);
		stubMockMethods();
		createQuestionComponentView();
	}

	@Test
	public void fragmentShouldNotBeNull() throws Exception {
		assertNotNull(qFragment);
	}

	@Test
	public void shouldCallBeforeAddViewHook() throws Exception {
		setTestComponents();
		verify(qFragment, times(1)).beforeAddViewHook(qComponent);
	}

	@Test
	public void shouldAddViewToQuestionComponent() throws Exception {
		options.add(option);
		options.add(option);
		stubMockMethods();
		ViewGroup qComponent2 = spy(new LinearLayout(Robolectric.application));
		qFragment.createQuestionComponent(qComponent2);
		verify(qComponent2, times(2)).addView(any(CheckBox.class),
				any(int.class));
	}

	@Test
	public void shouldSetOptionText() throws Exception {
		setTestComponents();
		CheckBox checkbox = (CheckBox) qComponent.getChildAt(0);
		assertEquals(TEXT, checkbox.getText().toString());
	}

	@Test
	public void shouldSetOptionId() throws Exception {
		setTestComponents();
		CheckBox checkbox = (CheckBox) qComponent.getChildAt(0);
		assertEquals(0, checkbox.getId());
	}

	@Test
	public void shouldSetTypeFace() throws Exception {
		setTestComponents();
		CheckBox checkbox = (CheckBox) qComponent.getChildAt(0);
		assertEquals(Typeface.DEFAULT, checkbox.getTypeface());
	}

	/*@Test //TODO FIX
	public void ShouldHaveOnCheckedChangeListener() throws Exception {
		setTestComponents();
		CheckBox checkbox = (CheckBox) qComponent.getChildAt(0);
		checkbox.setChecked(true);
		assertTrue(((CheckBox) qComponent.getChildAt(0)).isChecked());
	}

	@Test
	public void shouldCallSaveMultiResponse() throws Exception {
		setTestComponents();
		CheckBox checkbox = (CheckBox) qComponent.getChildAt(0);
		checkbox.setChecked(true);
		verify(qFragment, times(1)).toggleResponseIndex(0);	//.saveMultiResponse(0);
	}*/

}
