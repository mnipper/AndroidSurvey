package org.adaptlab.chpir.android.survey.QuestionFragments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
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
import org.adaptlab.chpir.android.survey.Models.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;

@RunWith(RobolectricTestRunner.class)
public class SelectOneQuestionFragmentTest {

	private static final long REMOTE_ID = 10L;
	private static final long SURVEY_ID = 10L;
	private static final String TEXT = "text";
	private SelectOneQuestionFragment qFragment;
	private InstrumentActivity activity;
	private Question question;
	private Option option;
	private Instrument instrument;
	private Response response;

	private void startFragment(SelectOneQuestionFragment fragment) {
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

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(InstrumentActivity.class).create()
				.get();
		qFragment = spy(new SelectOneQuestionFragment());
		doNothing().when(qFragment).init();
		setBundleArgs();
		startFragment(qFragment);
		setUpMocks();
	}

	private void setUpMocks() {
		question = mock(Question.class);
		option = mock(Option.class);
		instrument = mock(Instrument.class);
		response = spy(new Response());
	}

	private void stubMockMethods() {
		when(qFragment.getQuestion()).thenReturn(question);
		List<Option> list = new ArrayList<Option>();
		list.add(option);
		when(question.options()).thenReturn(list);
		when(qFragment.getInstrument()).thenReturn(instrument);
		when(instrument.getTypeFace(Robolectric.application)).thenReturn(
				Typeface.DEFAULT);
		when(option.getText()).thenReturn(TEXT);
		when(qFragment.getResponse()).thenReturn(response);
	}

	private void setQuestionComponentView() {
		ViewGroup qComponent = new LinearLayout(Robolectric.application);
		qFragment.createQuestionComponent(qComponent);
	}

	@Test
	public void fragmentShouldNotBeNull() throws Exception {
		assertNotNull(qFragment);
	}

	/*@Test //TODO FIX
	public void shouldReturnRadioGroup() throws Exception {
		RadioGroup radioGroup = new RadioGroup(activity);
		Whitebox.setInternalState(qFragment, "radioGroup", radioGroup);
		assertEquals(radioGroup, qFragment.getRadioGroup());
	}*/

	@Test
	public void shouldSetTextOnRadioButton() throws Exception {
		stubMockMethods();
		setQuestionComponentView();
		RadioButton button = (RadioButton) qFragment.getRadioGroup()
				.getChildAt(0);
		assertEquals(TEXT, button.getText().toString());
	}

	@Test
	public void shouldSetIdOnRadioButton() throws Exception {
		stubMockMethods();
		setQuestionComponentView();
		RadioButton button = (RadioButton) qFragment.getRadioGroup()
				.getChildAt(0);
		assertEquals(0, button.getId());
	}

	@Test
	public void shouldSetTypeFaceOnRadioButton() throws Exception {
		stubMockMethods();
		setQuestionComponentView();
		RadioButton button = (RadioButton) qFragment.getRadioGroup()
				.getChildAt(0);
		assertEquals(Typeface.DEFAULT, button.getTypeface());
	}

	@SuppressWarnings("static-access")
	@Test
	public void shouldSetLayoutParamsInRadioButton() throws Exception {
		stubMockMethods();
		setQuestionComponentView();
		RadioButton button = (RadioButton) qFragment.getRadioGroup()
				.getChildAt(0);
		LayoutParams params = new RadioGroup.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		assertNotNull(button.getLayoutParams());
		assertSame(params.MATCH_PARENT, button.getLayoutParams().MATCH_PARENT);
		assertSame(params.WRAP_CONTENT, button.getLayoutParams().WRAP_CONTENT);
	}

	/*@Test //TODO FIX
	public void radioGroupShouldHaveOnCheckedChangeListener() throws Exception {
		stubMockMethods();
		setQuestionComponentView();
		RadioButton button = (RadioButton) qFragment.getRadioGroup()
				.getChildAt(0);
		button.setChecked(true);
		assertEquals(button.getId(), qFragment.getRadioGroup()
				.getCheckedRadioButtonId());
	}*/

	@Test
	public void shouldAddRadioGroupViewToComponent() throws Exception {
		stubMockMethods();
		ViewGroup qComponent = new LinearLayout(Robolectric.application);
		qFragment.createQuestionComponent(qComponent);
		assertEquals(qComponent.getChildAt(0), qFragment.getRadioGroup());
	}

	@Test
	public void shouldCallBeforeAddViewHook() throws Exception {
		stubMockMethods();
		ViewGroup qComponent = new LinearLayout(Robolectric.application);
		qFragment.createQuestionComponent(qComponent);
		verify(qFragment, times(1)).beforeAddViewHook(qComponent);
	}
}
