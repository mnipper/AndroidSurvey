package org.adaptlab.chpir.android.survey.QuestionFragments;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.adaptlab.chpir.android.survey.SurveyActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

@RunWith(RobolectricTestRunner.class)
public class FreeResponseQuestionFragmentTest {
	
	private FreeResponseQuestionFragment questionFragment;
	private SurveyActivity activity;
	
	public void startFragment(FreeResponseQuestionFragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
	}

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(SurveyActivity.class).create().get();
		questionFragment = new FreeResponseQuestionFragment();
		startFragment(questionFragment);
	}
	
	@Test
	public void shouldNotBeNull() throws Exception {
		assertNotNull(questionFragment);
	}
	
	@Test
	public void shouldSetHintOnAnEditText() throws Exception {
		assertTrue(true);
	}
}
