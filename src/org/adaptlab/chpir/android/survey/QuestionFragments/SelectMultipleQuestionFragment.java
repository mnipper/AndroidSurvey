package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.Models.Option;
import org.adaptlab.chpir.android.survey.Models.Response;

import android.util.Log;
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
                    if (mResponseIndices.contains(optionId)) {
                        mResponseIndices.remove((Integer) optionId);
                    } else {
                        mResponseIndices.add(optionId);
                    }
                    saveResponse();
                }
            });
            mCheckBoxes.add(checkbox);
            questionComponent.addView(checkbox, optionId);
        }
        beforeAddViewHook(questionComponent);
    }

    @Override
    protected String serialize() {
        return mResponseIndices.toString();
    }

    @Override
    protected void deserialize(String responseText) {
        if (responseText.equals("")) return;    
        responseText = responseText.substring(1, responseText.length() - 1);
        String[] listOfIndices = responseText.split(",");
        for (String index : listOfIndices) {
            if (!index.equals("")) {
                Integer indexInteger = Integer.parseInt(index.trim());
                mCheckBoxes.get(indexInteger).setChecked(true);
            }
        }
    }
    
    protected void addResponseIndex(int index) {
        mResponseIndices.add(index);
    }
}
