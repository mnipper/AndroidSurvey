package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.view.ViewGroup;
import android.widget.EditText;

public class SelectMultipleImageQuestionFragment extends QuestionFragment {

	@Override
	protected void createQuestionComponent(ViewGroup questionComponent) {
		EditText text = new EditText(getActivity());
		text.setText("TO BE IMPLEMENTED");
		questionComponent.addView(text);
	}

	@Override
	protected String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void deserialize(String responseText) {
		// TODO Auto-generated method stub
		
	}

}
