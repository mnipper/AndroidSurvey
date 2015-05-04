package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adaptlab.chpir.android.survey.GridFragment;
import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.Models.GridLabel;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MultipleSelectGridFragment extends GridFragment {

	private static int OPTION_COLUMN_WIDTH = 400;
	private static int QUESTION_COLUMN_WIDTH = 700;
	
	private Map<String, List<CheckBox>> mCheckBoxes;
	private Question mQuestion;
	private List<Question> mQuestions;
	private Map<String, List<Integer>> mResponseIndices;
	
	@Override
	protected void deserialize(String responseText) {
		if (responseText.equals("")) {
			for (List<CheckBox> checkBoxList : mCheckBoxes.values()) {
				for (CheckBox box : checkBoxList) {
					if (box.isChecked()) {
						box.setChecked(false);
					}
				}
			}
		} else {
	        String[] listOfIndices = responseText.split(LIST_DELIMITER);
	        for (String index : listOfIndices) {
	            if (!index.equals("")) {
	                Integer indexInteger = Integer.parseInt(index);
	                mCheckBoxes.get(mQuestion.getQuestionIdentifier()).get(indexInteger).setChecked(true);
	            }
	        }
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_table_question, parent, false);
		
		TableLayout headerTable = (TableLayout) v.findViewById(R.id.header_table_view);
		TableRow headerRow = new TableRow(getActivity());
		headerRow.setBackground(getResources().getDrawable(R.drawable.table_border));
		TextView questionTextHeader = new TextView(getActivity());
		questionTextHeader.setText("Question Text");
		questionTextHeader.setWidth(QUESTION_COLUMN_WIDTH);
		questionTextHeader.setTypeface(Typeface.DEFAULT_BOLD);
		headerRow.addView(questionTextHeader);
		for (GridLabel label : getGrid().labels()) {
        	TextView textView = new TextView(getActivity());
        	textView.setText(label.getLabelText());
        	textView.setWidth(OPTION_COLUMN_WIDTH);
        	textView.setTypeface(Typeface.DEFAULT_BOLD);
        	headerRow.addView(textView);
        }
        headerTable.addView(headerRow, 0);
		
		TableLayout gridTableLayout = (TableLayout) v.findViewById(R.id.body_table_view);
		mQuestions = getQuestions();
		mResponseIndices = new HashMap<String, List<Integer>>();
		mCheckBoxes = new HashMap<String, List<CheckBox>>();
		for (int k = 0; k < mQuestions.size(); k++) {
			final Question q = mQuestions.get(k);			
			TableRow questionRow = new TableRow(getActivity());
			questionRow.setBackground(getResources().getDrawable(R.drawable.table_border));
			TextView questionText = new TextView(getActivity());
			questionText.setText(q.getText());
			questionText.setWidth(QUESTION_COLUMN_WIDTH);
			questionRow.addView(questionText);
			
			List<CheckBox> checkBoxes =  new ArrayList<CheckBox>();
			for (GridLabel label : getGrid().labels()) {
				final int id = getGrid().labels().indexOf(label);
				CheckBox checkbox = new CheckBox(getActivity());
				checkbox.setSaveEnabled(false);
				checkbox.setId(id);
				checkbox.setWidth(OPTION_COLUMN_WIDTH);
				checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						setResponses(q, id);
					}
				});
				questionRow.addView(checkbox);
				checkBoxes.add(checkbox);
			}
			mQuestion = q;
			mCheckBoxes.put(q.getQuestionIdentifier(), checkBoxes);
			deserialize(getSurvey().getResponseByQuestion(q).getText());
			gridTableLayout.addView(questionRow, k);
		}

		return v;
	}
	
	private void saveResponses(Question question, List<Integer> responseIndices) {
		String serialized = "";
		for (int i = 0; i < responseIndices.size(); i++) {
			serialized += responseIndices.get(i);
			if (i < responseIndices.size() - 1)
				serialized += LIST_DELIMITER;
		}
		Response response = getSurvey().getResponseByQuestion(question);
		response.setResponse(serialized);
		if (isAdded() && !response.getText().equals("")) {
			response.setSpecialResponse("");
			ActivityCompat.invalidateOptionsMenu(getActivity());
		}
		response.save();
	}

	@Override
	protected String serialize() { return null; }

	private void setResponses(Question question, Integer responseIndex) {
		if (mResponseIndices.containsKey(question.getQuestionIdentifier())) {
			List<Integer> responses = mResponseIndices.get(question.getQuestionIdentifier());
			if (responses.contains(responseIndex)) {
				responses.remove(responseIndex);
			} else {
				responses.add(responseIndex);
			}
			mResponseIndices.put(question.getQuestionIdentifier(), responses);
		} else {
			List<Integer> list = new ArrayList<Integer>();
			list.add(responseIndex);
			mResponseIndices.put(question.getQuestionIdentifier(), list);
		}
		saveResponses(question, mResponseIndices.get(question.getQuestionIdentifier()));
	}
	
}
