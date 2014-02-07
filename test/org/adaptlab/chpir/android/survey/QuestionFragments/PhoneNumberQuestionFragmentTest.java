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

import android.text.InputType;
import android.widget.EditText;

@RunWith(RobolectricTestRunner.class)
public class PhoneNumberQuestionFragmentTest extends QuestionFragmentBaseTest {
	private SurveyActivity mActivity;
	private PhoneNumberQuestionFragment mFragment; 
	
	@Override
	public void setUp() throws Exception {
		mActivity = Robolectric.buildActivity(SurveyActivity.class).create().get();
		mFragment = spy(new PhoneNumberQuestionFragment());
		doNothing().when(mFragment).init();
		setBundleArgs(mFragment);
		startFragment(mActivity, mFragment);		
	}
	
	@Test
	public void shouldNotBeNull() throws Exception {
		assertNotNull(mFragment);
	}
	
	@Test
	public void beforeAddViewHookShouldSetInputType() throws Exception {
		EditText editText = new EditText(mActivity);
		mFragment.beforeAddViewHook(editText);
		assertEquals(editText.getInputType(), (InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_PHONE));
	}

}

