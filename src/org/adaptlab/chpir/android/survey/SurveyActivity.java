package org.adaptlab.chpir.android.survey;

import android.support.v4.app.Fragment;

public class SurveyActivity extends SingleFragmentActivity {
	public static final String EXTRA_SURVEY_ID =
			"com.adaptlab.chpir.android.survey.survey_id";
	
	@Override
	protected Fragment createFragment() {
		long surveyId = getIntent().getLongExtra(EXTRA_SURVEY_ID, -1);
		if (surveyId != -1) {
			return SurveyFragment.newInstance(surveyId);
		} else {
			return new SurveyFragment();
		}
	}
	
}
