package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.List;

import org.adaptlab.chpir.android.survey.GridFragment;
import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.Models.GridLabel;
import org.adaptlab.chpir.android.survey.Models.Option;
import org.adaptlab.chpir.android.survey.Models.Question;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GridSelectSingleFragment extends GridFragment {
	
	private static final String TAG = "GridSelectSingleFragment";
	private static int QUESTION_COLUMN_WIDTH = 700;
	private static int OPTION_COLUMN_WIDTH = 400;
	
	private List<Question> mQuestions;
	
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
        for (int k = 0; k < mQuestions.size(); k++) {
	        Question q = mQuestions.get(k);        
        	TableRow questionRow= new TableRow(getActivity());
            TextView questionText = new TextView(getActivity());
            questionText.setText(q.getText());
            questionText.setWidth(QUESTION_COLUMN_WIDTH);
            questionRow.addView(questionText);
            
            RadioGroup radioButtons = new RadioGroup(getActivity());
            radioButtons.setOrientation(RadioGroup.HORIZONTAL);           
            for (GridLabel label : getGrid().labels()) {
            	RadioButton button = new RadioButton(getActivity());
            	button.setWidth(OPTION_COLUMN_WIDTH);
            	radioButtons.addView(button, getGrid().labels().indexOf(label));
            }
            questionRow.addView(radioButtons);
            
            bodyTableLayout.addView(questionRow, k);
        
        }
        return v;
    }

}
