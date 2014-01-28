package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.R;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.EditText;

public class FreeResponseQuestionFragment extends QuestionFragment {
    private String mText = "";
    private EditText mFreeText;
    
    // This is used to restrict allowed input in subclasses.
    protected void beforeAddViewHook(EditText editText) {
    }

    @Override
	public void createQuestionComponent(ViewGroup questionComponent) {
        mFreeText = new EditText(getActivity());
        mFreeText.setHint(R.string.free_response_edittext);
        mFreeText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) { 
                mText = s.toString();
                saveResponse();
            }
            
            // Required by interface
            public void beforeTextChanged(CharSequence s, int start,
                    int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });
        beforeAddViewHook(mFreeText);
        questionComponent.addView(mFreeText);
    }

    @Override
    protected String serialize() {
        return mText;
    }

    @Override
    protected void deserialize(String responseText) {
        mFreeText.setText(responseText);
    }
    
}