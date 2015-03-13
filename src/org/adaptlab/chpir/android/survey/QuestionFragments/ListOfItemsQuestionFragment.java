package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.ArrayList;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.Models.Option;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public abstract class ListOfItemsQuestionFragment extends QuestionFragment {
    private ArrayList<EditText> mResponses;
    protected abstract EditText createEditText();
    
    protected void createQuestionComponent(ViewGroup questionComponent) {
        mResponses = new ArrayList<EditText>();
        for (Option option : getQuestion().options()) {
            final TextView optionText = new TextView(getActivity());
            optionText.setText(option.getText());
            questionComponent.addView(optionText);
            EditText editText = createEditText();
            editText.setHint(R.string.free_response_edittext);
            editText.setTypeface(getInstrument().getTypeFace(getActivity().getApplicationContext()));
            questionComponent.addView(editText);
            mResponses.add(editText);
            editText.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before,
                        int count) { 
                    saveResponse();
                }
                
                // Required by interface
                public void beforeTextChanged(CharSequence s, int start,
                        int count, int after) { }
                public void afterTextChanged(Editable s) { }
            });
        }
    }

    @Override
    protected String serialize() {
        String serialized = "";
        for (int i = 0; i < mResponses.size(); i++) {
            serialized += mResponses.get(i).getText().toString();
            if (i <  mResponses.size() - 1) serialized += LIST_DELIMITER;
        }
        return serialized;
    }

    @Override
    protected void deserialize(String responseText) {
        if (responseText.equals("")) return;    
        String[] listOfResponses = responseText.split(LIST_DELIMITER);
        for (int i = 0; i < listOfResponses.length; i++) {
            if (mResponses.size() > i)
                mResponses.get(i).setText(listOfResponses[i]);
        }
    }
}
