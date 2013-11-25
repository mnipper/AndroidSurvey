/*package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.SurveyActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

@RunWith(RobolectricTestRunner.class)
public class FreeResponseQuestionFragmentTest {
	
	private FreeResponseQuestionFragment questionFragment;
	private SurveyActivity activity;

	@Before
	public void setUp() {
		activity = Robolectric.buildActivity(SurveyActivity.class).create().get();
		questionFragment = new FreeResponseQuestionFragment();
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    fragmentTransaction.add(questionFragment, null );
	    fragmentTransaction.commit();
	}
	
	@Test
	public void shouldAddQuestionComponentToViewGroup() throws Exception {
		ViewGroup original = (ViewGroup) activity.findViewById(R.id.fragmentContainer).getRootView(); 
		questionFragment.createQuestionComponent(original);
		//assertThat(questionFragment.getView(), instanceOf(EditText.class));
	}
}
*/