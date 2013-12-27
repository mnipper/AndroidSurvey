package org.adaptlab.chpir.android.survey.QuestionFragments;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.adaptlab.chpir.android.survey.InstrumentActivity;
import org.adaptlab.chpir.android.survey.QuestionFragmentFactory;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

@RunWith(RobolectricTestRunner.class)
public class SliderQuestionFragmentTest {
	private static final long REMOTE_ID = 10L;
	private static final long SURVEY_ID = 10L;
	private static final int PROGRESS = 50;
	private InstrumentActivity activity;
	private SliderQuestionFragment qFragment;
	private Response response;

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(InstrumentActivity.class).create()
				.get();
		qFragment = spy(new SliderQuestionFragment());
		doNothing().when(qFragment).init();
		setBundleArgs();
		startFragment(qFragment);
	}

	private void startFragment(SliderQuestionFragment fragment) {
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

	@Test
	public void fragmentShouldNotBeNull() throws Exception {
		assertNotNull(qFragment);
	}

	@Test
	public void shouldCallCreateQuestionComponent() throws Exception {
		ViewGroup qComponent = spy(new LinearLayout(Robolectric.application));
		qFragment.createQuestionComponent(qComponent);
		verify(qFragment, times(1)).createQuestionComponent(qComponent);
	}

	@Test
	public void shouldCallAddViewWithSlider() throws Exception {
		ViewGroup qComponent = spy(new LinearLayout(Robolectric.application));
		qFragment.createQuestionComponent(qComponent);
		verify(qComponent, times(1)).addView(any(SeekBar.class));
	}

	@Test
	public void shouldHaveSeekBar() throws Exception {
		ViewGroup qComponent = new LinearLayout(Robolectric.application);
		qFragment.createQuestionComponent(qComponent);
		assertThat(qComponent.getChildAt(0), instanceOf(SeekBar.class));
	}

	@Test
	public void shouldDetectSliderProgressChange() throws Exception {
		response = spy(new Response());
		when(qFragment.getResponse()).thenReturn(response);
		ViewGroup qComponent = new LinearLayout(Robolectric.application);
		qFragment.createQuestionComponent(qComponent);
		SeekBar slider = (SeekBar) qComponent.getChildAt(0);
		slider.setProgress(PROGRESS);
		assertEquals(PROGRESS,
				((ProgressBar) qComponent.getChildAt(0)).getProgress());
	}

}
