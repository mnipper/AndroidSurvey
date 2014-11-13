package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.view.View;
import android.view.ViewGroup;

/*
 * This question type exist only to add text to a screen.
 * 
 * It does not save a response, only displays the text.
 */
public class InstructionsQuestionFragment extends QuestionFragment {

	@Override
	protected void createQuestionComponent(ViewGroup questionComponent) { 
		questionComponent.setVisibility(View.INVISIBLE);
	}

	@Override
	protected String serialize() {
		return "";
	}

	@Override
	protected void deserialize(String responseText) { }

}
