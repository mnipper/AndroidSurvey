package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.ArrayList;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.Models.Option;

import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SelectMultipleQuestionFragment extends QuestionFragment {
    private ArrayList<Integer> mResponseIndices;
    private ArrayList<CheckBox> mCheckBoxes;
    
    // This is used to add additional UI components in subclasses.
    protected void beforeAddViewHook(ViewGroup questionComponent) {
    }

    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        mCheckBoxes = new ArrayList<CheckBox>();
        mResponseIndices = new ArrayList<Integer>();
        for (Option option : getQuestion().options()) {
            final int optionId = getQuestion().options().indexOf(option);
            CheckBox checkbox = new CheckBox(getActivity());
            checkbox.setText(option.getText());
            checkbox.setTypeface(getInstrument().getTypeFace(getActivity().getApplicationContext()));
            checkbox.setId(optionId);     
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    toggleResponseIndex(optionId);
                }
            });
            mCheckBoxes.add(checkbox);
            questionComponent.addView(checkbox, optionId);
        }
        beforeAddViewHook(questionComponent);
    }

    @Override
    protected String serialize() {
        String serialized = "";
        for (int i = 0; i < mResponseIndices.size(); i++) {
            serialized += mResponseIndices.get(i);
            if (i <  mResponseIndices.size() - 1) serialized += LIST_DELIMITER;
        }
        return serialized;
    }

	@Override
	protected void deserialize(String responseText) {
		if (responseText.equals("")) {
			for (CheckBox box : mCheckBoxes) {
				if (box.isChecked()) {
					box.setChecked(false);
				}
			}
		} else {
			String[] listOfIndices = responseText.split(LIST_DELIMITER);
			for (String index : listOfIndices) {
				if (!index.equals("")) {
					Integer indexInteger = Integer.parseInt(index);
					mCheckBoxes.get(indexInteger).setChecked(true);
				}
			}
		}
	}
    
    protected void toggleResponseIndex(int index) {
        if (mResponseIndices.contains(index)) {
            mResponseIndices.remove((Integer) index);
        } else {
            mResponseIndices.add(index);
        }
        saveResponse();
    }
    
    protected void addCheckBox(CheckBox checkbox) {
        mCheckBoxes.add(checkbox);
    }
    
}
