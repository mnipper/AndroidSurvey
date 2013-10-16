package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.Models.Response;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class SelectMultipleWriteOtherQuestionFragment extends
        SelectMultipleQuestionFragment {

    @Override
    protected void beforeAddViewHook(ViewGroup questionComponent) {
        CheckBox checkbox = new CheckBox(getActivity());
        final EditText otherText = new EditText(getActivity());
        checkbox.setText(R.string.other_specify);
        final int otherId = getQuestion().options().size();
        checkbox.setId(otherId);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                otherText.setEnabled(isChecked);
                
                if (isChecked) {
                    // Ignore generated response object so that
                    // multiple responses can be recorded
                    Response mResponse = new Response();
                    mResponse.setQuestion(getQuestion());
                    mResponse.setSurvey(getSurvey());
                    mResponse.setResponse(otherId+"");
                    mResponse.save();
                }
            }
         });
        questionComponent.addView(checkbox, otherId);

        otherText.setHint(R.string.other_specify_edittext);
        otherText.setEnabled(false);
        otherText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) { 
                getResponse().setOtherResponse(s.toString());
                getResponse().save();
            }
            
            // Required by interface
            public void beforeTextChanged(CharSequence s, int start,
                    int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });
        
        questionComponent
                .addView(otherText, getQuestion().options().size() + 1);
    }
}
