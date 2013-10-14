package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.Models.Option;

import android.view.ViewGroup;
import android.widget.CheckBox;

public class SelectMultipleQuestionFragment extends QuestionFragment {
	protected void beforeAddViewHook(ViewGroup questionComponent) { }
	
	@Override
	protected void createQuestionComponent(ViewGroup questionComponent) {
		for (Option option : getQuestion().options()) {
		    int optionId = getQuestion().options().indexOf(option);
			CheckBox checkbox = new CheckBox(getActivity());
		    checkbox.setText(option.getText());
		    checkbox.setId(optionId);
		    questionComponent.addView(checkbox, optionId);
		}
		beforeAddViewHook(questionComponent);
	}
}

