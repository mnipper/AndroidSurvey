package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.R;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SelectOneWriteOtherQuestionFragment extends
        SelectOneQuestionFragment {

    @Override
    protected void beforeAddViewHook(ViewGroup questionComponent) {
        RadioButton radioButton = new RadioButton(getActivity());
        final EditText otherText = new EditText(getActivity());
        
        radioButton.setText(R.string.other_specify);
        final int otherId = getQuestion().options().size();
        radioButton.setId(otherId);
        getRadioGroup().setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                otherText.setEnabled(checkedId == otherId);
            }
        });
        getRadioGroup().addView(radioButton, otherId);
        
        otherText.setHint(R.string.other_specify_edittext);
        otherText.setEnabled(false);
        questionComponent.addView(otherText);
    }
}
