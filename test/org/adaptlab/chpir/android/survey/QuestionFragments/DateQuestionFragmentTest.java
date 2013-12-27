package org.adaptlab.chpir.android.survey.QuestionFragments;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import org.adaptlab.chpir.android.robolectric.shadows.ShadowDatePicker;
import org.adaptlab.chpir.android.survey.QuestionFragmentFactory;
import org.adaptlab.chpir.android.survey.SurveyActivity;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;

@Config(shadows = {ShadowDatePicker.class} )
@RunWith(RobolectricTestRunner.class)
public class DateQuestionFragmentTest {
	private static final long REMOTE_ID = 10L;
	private static final long SURVEY_ID = 10L;
	private SurveyActivity activity;
	private DateQuestionFragment qFragment;
	private ViewGroup qComponent;
	private Response response;
	
	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(SurveyActivity.class).create().get();
		qFragment = spy(new DateQuestionFragment());
		doNothing().when(qFragment).init();
		setBundleArgs();
		startFragment(qFragment);
		qComponent = new FrameLayout(Robolectric.application);
		response = spy(new Response());
		doReturn(response).when(qFragment).getResponse();
	}

	private void startFragment(DateQuestionFragment fragment) {
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
	public void shouldHaveDatePickerView() throws Exception {
		qFragment.createQuestionComponent(qComponent);
		assertThat(qComponent.getChildAt(0), instanceOf(DatePicker.class));
	}
	
	@Test
	public void shouldHaveDateChangeListener() throws Exception {
		qFragment.createQuestionComponent(qComponent);
		DatePicker picker = (DatePicker) qComponent.getChildAt(0);
		picker.updateDate(2013, 12, 20);
		assertEquals(picker.getYear(), 2013);
		assertEquals(picker.getMonth(), 12);
		assertEquals(picker.getDayOfMonth(), 20);
	}
	
	/*@Test
	public void shouldFormatDate() throws Exception {
		qFragment.createQuestionComponent(qComponent);
		String date1 = Whitebox.invokeMethod(qFragment, "formatDate", 12, 20, 2013);
		String date2 = "" + " " + 20 + ", " + 2013; //TODO DateFormatSymbols()???
		assertEquals(date1, date2);
	}*/
	
}
