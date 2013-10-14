package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.Models.Option;

import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelectOneQuestionFragment extends QuestionFragment {
    private RadioGroup radioGroup;

    protected void beforeAddViewHook(ViewGroup questionComponent) {
    }

    protected RadioGroup getRadioGroup() {
        return radioGroup;
    }

    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        radioGroup = new RadioGroup(getActivity());
        for (Option option : getQuestion().options()) {
            int optionId = getQuestion().options().indexOf(option);
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(option.getText());
            radioButton.setId(optionId);
            radioGroup.addView(radioButton, optionId);
        }
        beforeAddViewHook(questionComponent);
        questionComponent.addView(radioGroup);
    }
}
