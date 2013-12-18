package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.Models.Option;
import org.adaptlab.chpir.android.survey.Models.Response;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SelectMultipleQuestionFragment extends QuestionFragment {
    
    // This is used to add additional UI components in subclasses.
    protected void beforeAddViewHook(ViewGroup questionComponent) {
    }

    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        for (Option option : getQuestion().options()) {
            final int optionId = getQuestion().options().indexOf(option);
            CheckBox checkbox = new CheckBox(getActivity());
            checkbox.setText(option.getText());
            checkbox.setTypeface(getInstrument().getTypeFace(getActivity().getApplicationContext()));
            checkbox.setId(optionId);     
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        saveMultiResponse(optionId);
                    }
                }
             });
            questionComponent.addView(checkbox, optionId);
        }
        beforeAddViewHook(questionComponent);
    }
    
    protected void saveMultiResponse(int id) {
        // Ignore generated response object so that
        // multiple responses can be recorded
        Response mResponse = new Response();
        mResponse.setQuestion(getQuestion());
        mResponse.setSurvey(getSurvey());
        mResponse.setResponse(String.valueOf(id));
        mResponse.save();
    }
}
