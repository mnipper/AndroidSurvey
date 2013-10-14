package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.R;
import android.view.ViewGroup;
import android.widget.EditText;

public class FreeResponseQuestionFragment extends QuestionFragment {
	
	@Override
	protected void createQuestionComponent(ViewGroup questionComponent) {
		EditText freeText = new EditText(getActivity());
		freeText.setHint(R.string.free_response_edittext);
		questionComponent.addView(freeText);
	}
}