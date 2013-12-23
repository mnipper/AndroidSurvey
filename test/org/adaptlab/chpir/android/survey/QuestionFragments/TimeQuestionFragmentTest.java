package org.adaptlab.chpir.android.survey.QuestionFragments;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import org.adaptlab.chpir.android.survey.QuestionFragmentFactory;
import org.adaptlab.chpir.android.survey.SurveyActivity;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TimePicker;

@RunWith(RobolectricTestRunner.class)
@PrepareForTest({ TimeQuestionFragment.class, Response.class })
public class TimeQuestionFragmentTest {
	private static final long REMOTE_ID = 10L;
	private static final long SURVEY_ID = 10L;
	private SurveyActivity activity;
	private TimeQuestionFragment qFragment;
	private ViewGroup qComponent;
	private Response response;
	
	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(SurveyActivity.class).create().get();
		qFragment = spy(new TimeQuestionFragment());
		doNothing().when(qFragment).init();
		setBundleArgs();
		startFragment(qFragment);
		qComponent = new FrameLayout(Robolectric.application);
		response = spy(new Response());
		doReturn(response).when(qFragment).getResponse();
	}

	private void startFragment(TimeQuestionFragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
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
	public void shouldNotBeNull() throws Exception {
		assertNotNull(qFragment);
	}
	
	@Test
	public void shouldHaveTimePickerView() throws Exception {
		qFragment.createQuestionComponent(qComponent);
		assertThat(qComponent.getChildAt(0), instanceOf(TimePicker.class));
	}
	
	@Test
	public void shouldHaveTimeChangedListener() throws Exception {
		Integer hour = 10;
		qFragment.createQuestionComponent(qComponent);
		TimePicker timePicker = (TimePicker) qComponent.getChildAt(0);
		timePicker.setCurrentHour(hour);
		assertEquals(timePicker.getCurrentHour(), hour);
	}
	
	/*@Test
	public void shouldFormatTime() throws Exception {
		String formatted = Whitebox.invokeMethod(qFragment, "formatTime", 2, 15);
		assertEquals(formatted, "2:15 AM");
		String formatted2 = Whitebox.invokeMethod(qFragment, "formatTime", 16, 25);
		assertEquals(formatted2, "4:25 PM");
	}*/
	
	/*@Test
	public void shouldFormatMinutes() throws Exception {
		String formatted = Whitebox.invokeMethod(qFragment, "formatMinute", 5);
		assertEquals(formatted, "05");
	}*/
}
