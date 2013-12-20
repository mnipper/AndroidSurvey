package org.adaptlab.chpir.android.survey.QuestionFragments;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
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
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.RatingBar;

@RunWith(RobolectricTestRunner.class)
@PrepareForTest({ RatingQuestionFragment.class, Response.class })
public class RatingQuestionFragmentTest {

	private static final long REMOTE_ID = 10L;
	private static final long SURVEY_ID = 10L;
	private SurveyActivity activity;
	private RatingQuestionFragment qFragment;
	private ViewGroup qComponent;
	private Response response;
	
	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(SurveyActivity.class).create().get();
		qFragment = spy(new RatingQuestionFragment());
		doNothing().when(qFragment).init();
		setBundleArgs();
		startFragment(qFragment);
		qComponent = new FrameLayout(Robolectric.application);
		response = spy(new Response());
		doReturn(response).when(qFragment).getResponse();
	}

	private void startFragment(RatingQuestionFragment fragment) {
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
	public void shouldHaveRatingBarView() throws Exception {
		qFragment.createQuestionComponent(qComponent);
		assertThat(qComponent.getChildAt(0), instanceOf(RatingBar.class));
	}
	
	@Test
	public void shouldHaveFiveRatingStars() throws Exception {
		qFragment.createQuestionComponent(qComponent);
		RatingBar bar = (RatingBar) qComponent.getChildAt(0);
		assertEquals(bar.getNumStars(), 5);
	}
	
	@Test
	public void shouldHaveOnRatingsChanged() throws Exception {
		qFragment.createQuestionComponent(qComponent);
		RatingBar bar = (RatingBar) qComponent.getChildAt(0);
		float rating = 3;
		bar.setRating(rating);
		assertThat(((RatingBar) qComponent.getChildAt(0)).getRating(), equalTo(rating));
	}
	
}
