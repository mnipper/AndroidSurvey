package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.Models.Option;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

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
            radioButton.setTypeface(getInstrument().getTypeFace(getActivity().getApplicationContext()));
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
            radioGroup.addView(radioButton, optionId);
        }

        
        getRadioGroup().setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getResponse().setResponse(String.valueOf(checkedId));
                getResponse().save();
            }
        });
        questionComponent.addView(radioGroup);
        beforeAddViewHook(questionComponent);
    }
}
