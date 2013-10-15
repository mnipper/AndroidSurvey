package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.R;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.EditText;

public class FreeResponseQuestionFragment extends QuestionFragment {

    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        EditText freeText = new EditText(getActivity());
        freeText.setHint(R.string.free_response_edittext);
        freeText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) { 
                getResponse().setResponse(s.toString());
                getResponse().save();
            }
            
            // Required by interface
            public void beforeTextChanged(CharSequence s, int start,
                    int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });
        questionComponent.addView(freeText);
    }
}