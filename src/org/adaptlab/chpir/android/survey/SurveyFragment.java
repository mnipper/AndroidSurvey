package org.adaptlab.chpir.android.survey;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SurveyFragment extends Fragment {
	private static final String ARG_SURVEY_ID = "SURVEY_ID";

	public static SurveyFragment newInstance(long surveyId) {
		Bundle args = new Bundle();
		args.putLong(ARG_SURVEY_ID, surveyId);
		SurveyFragment sf = new SurveyFragment();
		sf.setArguments(args);
		return sf;
	}
}
