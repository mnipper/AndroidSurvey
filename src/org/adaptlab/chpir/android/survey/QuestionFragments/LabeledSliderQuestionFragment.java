package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.Models.Option;

import android.view.ViewGroup;
import android.widget.TextView;

public class LabeledSliderQuestionFragment extends SliderQuestionFragment {
    
    @Override
    public void beforeAddViewHook(ViewGroup questionComponenet) {
        if (getQuestion().hasOptions()) {
            for (Option option : getQuestion().options()) {
                TextView optionText = new TextView(getActivity());
                optionText.setText(option.getText());
                questionComponenet.addView(optionText);
            }
        }
    }
}
