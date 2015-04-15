package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.ArrayList;
import java.util.List;

import org.adaptlab.chpir.android.survey.AppUtil;
import org.adaptlab.chpir.android.survey.GridFragment;
import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.Models.GridLabel;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SingleSelectGridFragment extends GridFragment {
	
	private static int OPTION_COLUMN_WIDTH = 400;
	private static int QUESTION_COLUMN_WIDTH = 700;
	private static final String TAG = "SingleSelectGridFragment";
	
	private int mIndex;
	private List<Question> mQuestions;
	private List<RadioGroup> mRadioGroups;
	
	@Override
	protected void deserialize(String responseText) {
		if (!TextUtils.isEmpty(responseText)) {
			mRadioGroups.get(mIndex).check(Integer.parseInt(responseText));
		}
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_table_question, parent, false);        
		
		TableLayout headerTableLayout = (TableLayout) v.findViewById(R.id.header_table_view);
		TableRow headerRow= new TableRow(getActivity());
		TextView questionTextHeader = new TextView(getActivity());
        questionTextHeader.setText("Question Text");
        questionTextHeader.setWidth(QUESTION_COLUMN_WIDTH);
        headerRow.addView(questionTextHeader);
        
        for (GridLabel label : getGrid().labels()) {
        	TextView textView = new TextView(getActivity());
        	textView.setText(label.getLabelText());
        	textView.setWidth(OPTION_COLUMN_WIDTH);
        	headerRow.addView(textView);
        }
        headerTableLayout.addView(headerRow, 0);
		
		TableLayout bodyTableLayout = (TableLayout) v.findViewById(R.id.body_table_view);    
        mQuestions = getQuestions();
        mRadioGroups = new ArrayList<RadioGroup>();
        for (int k = 0; k < mQuestions.size(); k++) {
	        final Question q = mQuestions.get(k);        
        	TableRow questionRow= new TableRow(getActivity());
            TextView questionText = new TextView(getActivity());
            questionText.setText(q.getText());
            questionText.setWidth(QUESTION_COLUMN_WIDTH);
            questionRow.addView(questionText);
            
            RadioGroup radioButtons = new RadioGroup(getActivity());
            radioButtons.setOrientation(RadioGroup.HORIZONTAL);           
            for (GridLabel label : getGrid().labels()) {
            	int id = getGrid().labels().indexOf(label);
            	RadioButton button = new RadioButton(getActivity());
            	button.setId(id);
            	button.setWidth(OPTION_COLUMN_WIDTH);
            	radioButtons.addView(button, id);
            }
            questionRow.addView(radioButtons);           
            bodyTableLayout.addView(questionRow, k);
            radioButtons.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					setResponseIndex(q, checkedId);
				}        	
            });
            mRadioGroups.add(radioButtons);
            mIndex = k;
            deserialize(getSurvey().getResponseByQuestion(q).getText());
        }
        return v;
    }  

	@Override
	protected String serialize() { return null; }

	private void setResponseIndex(Question q, int checkedId) {
		Response response = getSurvey().getResponseByQuestion(q);
		response.setResponse(String.valueOf(checkedId));
		response.save();
		if (AppUtil.DEBUG) Log.i(TAG, "For Question: " + q.getQuestionIdentifier() + " Picked Response: " + response.getText());
	}

}
