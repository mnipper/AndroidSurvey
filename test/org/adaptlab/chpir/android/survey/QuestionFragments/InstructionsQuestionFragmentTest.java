package org.adaptlab.chpir.android.survey.QuestionFragments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

import org.adaptlab.chpir.android.survey.SurveyActivity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.view.ViewGroup;
import android.widget.LinearLayout;

@RunWith(RobolectricTestRunner.class)
public class InstructionsQuestionFragmentTest extends QuestionFragmentBaseTest {
	private SurveyActivity mActivity;
	private InstructionsQuestionFragment mFragment;
	@Override
	public void setUp() throws Exception {
		mActivity = Robolectric.buildActivity(SurveyActivity.class).create().get();
		mFragment = spy(new InstructionsQuestionFragment());
		doNothing().when(mFragment).init();
		setBundleArgs(mFragment);
		startFragment(mActivity, mFragment);
	}
	
	@Test
	public void shouldNotBeNull() throws Exception {
		assertNotNull(mFragment);
	}
	
	@Test
	public void createQuestionComponentShouldAddView() throws Exception {
		ViewGroup mComponent = new LinearLayout(Robolectric.application);
		mFragment.createQuestionComponent(mComponent);
		assertEquals(mComponent.getChildCount(), 0);
	}
	
	@Test
	public void serializeShouldReturnEmptyString() throws Exception {
		assertEquals(mFragment.serialize(), "");
	}
	
}
