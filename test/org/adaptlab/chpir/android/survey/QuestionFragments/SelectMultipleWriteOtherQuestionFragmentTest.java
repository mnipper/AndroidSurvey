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
import org.adaptlab.chpir.android.survey.R;
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
import android.widget.EditText;
import android.widget.LinearLayout;

@RunWith(RobolectricTestRunner.class)
public class SelectMultipleWriteOtherQuestionFragmentTest {
	private static final long REMOTE_ID = 10L;
	private static final long SURVEY_ID = 10L;
	private InstrumentActivity activity;
	private SelectMultipleWriteOtherQuestionFragment qFragment;
	private Question question;
	//private Option option;
	private Instrument instrument;
	private List<Option> options;
	private ViewGroup qComponent;

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(InstrumentActivity.class).create()
				.get();
		qFragment = spy(new SelectMultipleWriteOtherQuestionFragment());
		doNothing().when(qFragment).init();
		setBundleArgs();
		startFragment(qFragment);
		setUpMocks();
	}

	private void startFragment(SelectMultipleWriteOtherQuestionFragment fragment) {
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
		//option = mock(Option.class);
		instrument = mock(Instrument.class);
		options = new ArrayList<Option>();
	}

	private void stubMockedMethods() {
		when(qFragment.getQuestion()).thenReturn(question);
		when(question.options()).thenReturn(options);
		when(qFragment.getInstrument()).thenReturn(instrument);
		when(instrument.getTypeFace(Robolectric.application)).thenReturn(
				Typeface.DEFAULT);
	}

	private void createQuestionComponentView() {
		qComponent = new LinearLayout(Robolectric.application);
		qFragment.beforeAddViewHook(qComponent);
	}

	private void createTestComponents() {
		stubMockedMethods();
		createQuestionComponentView();
	}

	@Test
	public void shouldNotBeNull() throws Exception {
		assertNotNull(qFragment);
	}

	/*@Test //TODO FIX
	public void shouldAddTwoViewsToQuestionComponent() throws Exception {
		createTestComponents();
		assertEquals(qComponent.getChildCount(), 2);
	}*/

	/*
	 * @Test //TODO FIX THIS public void shouldAddViewToSubsequentViewIndex()
	 * throws Exception { //options.add(option); stubMockedMethods(); ViewGroup
	 * qComponent1 = new LinearLayout(Robolectric.application);
	 * qFragment.createQuestionComponent(qComponent1); options.add(option);
	 * ViewGroup qComponent2 = new LinearLayout(Robolectric.application);
	 * qFragment.beforeAddViewHook(qComponent2); CheckBox checkbox = (CheckBox)
	 * qComponent2.getChildAt(0); verify(qComponent, times(1)).addView(checkbox,
	 * 0); //verify(qComponent, times(1)).addView(any(EditText.class)); }
	 */

	/*@Test //TODO FIX 
	public void shouldCallAddOtherResponseView() throws Exception {
		createTestComponents();
		verify(qFragment, times(1)).addOtherResponseView(any(EditText.class));
	}

	@Test
	public void shouldSetCheckBoxText() throws Exception {
		createTestComponents();
		CheckBox checkbox = (CheckBox) qComponent.getChildAt(0);
		assertEquals(activity.getResources().getString(R.string.other_specify),
				checkbox.getText().toString());
	}

	@Test
	public void ShouldHaveOnCheckedChangeListenerSet() throws Exception {
		createTestComponents();
		CheckBox checkbox = (CheckBox) qComponent.getChildAt(0);
		checkbox.setChecked(true);
		assertTrue(((CheckBox) qComponent.getChildAt(0)).isChecked());
		verify(qFragment, times(1)).toggleResponseIndex(0); //saveMultiResponse(0);
	}*/
}
