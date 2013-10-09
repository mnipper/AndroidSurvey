package org.adaptlab.chpir.android.survey;

import android.support.v4.app.Fragment;

public class SurveyListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new SurveyListFragment();
	}
}