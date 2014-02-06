package org.adaptlab.chpir.android.survey.QuestionFragments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

import org.adaptlab.chpir.android.survey.SurveyActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.text.InputType;
import android.widget.EditText;

@RunWith(RobolectricTestRunner.class)
public class AddressQuestionFragmentTest extends QuestionFragmentBaseTest {
	private SurveyActivity mActivity;
	private AddressQuestionFragment mFragment;

	@Before
	public void setUp() throws Exception {
		mActivity = Robolectric.buildActivity(SurveyActivity.class).create().get();
		mFragment = spy(new AddressQuestionFragment());
		doNothing().when(mFragment).init();
		setBundleArgs(mFragment);
		startFragment(mActivity, mFragment);
	}
	
	@Test
	public void shouldNotBeNull() throws Exception {
		assertNotNull(mFragment);
	}

	@Test
	public void editableTextShouldHaveInputType() throws Exception {
		EditText editText = new EditText(mActivity);
		mFragment.beforeAddViewHook(editText);
		assertEquals(
				editText.getInputType(),
				(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS | InputType.TYPE_TEXT_FLAG_MULTI_LINE));
	}
}
