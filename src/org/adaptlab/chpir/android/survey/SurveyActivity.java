package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.Response;

import android.support.v4.app.Fragment;

public class SurveyActivity extends AuthorizedActivity implements QuestionFragment.OnSaveResponseListener {

	@Override
	protected Fragment createFragment() {
		return new SurveyFragment();
	}

	@Override
	public void onResponseSaved() {
		SurveyFragment surveyFragment = (SurveyFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer); 
		if (surveyFragment != null) {
			Response response = surveyFragment.mQuestionFragment.getResponse();
			if (response != null) {
				response.setSpecialResponse("");
			}
		} 
	}
}
