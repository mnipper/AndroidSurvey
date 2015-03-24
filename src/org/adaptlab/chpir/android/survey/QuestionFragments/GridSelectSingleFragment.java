package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.Models.Option;
import org.adaptlab.chpir.android.survey.Models.Question;

import android.graphics.Color;
import android.os.Bundle;
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

public class GridSelectSingleFragment extends QuestionFragment {

	private static final String TAG = "GridSelectSingleFragment";
	private static int Q_ID_WIDTH = 200;
	private static int Q_TEXT_WIDTH = 500;
	private static int O_COL_WIDTH = 300;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        
		View v = inflater.inflate(R.layout.fragment_table_question, parent, false);        
		TableLayout gridSelectTableLayout = (TableLayout) v.findViewById(R.id.table_view);    
        TableRow headerRow= new TableRow(getActivity());

        Question q = getQuestion();    
        TextView qid = new TextView(getActivity());
        qid.setText("Q_ID");
        headerRow.addView(qid);
        TextView q_text = new TextView(getActivity());
        q_text.setText("Q_TEXT");
        headerRow.addView(q_text);
        
        gridSelectTableLayout.addView(headerRow, 0);
        
        //TODO get number of questions to be show on the table
        
        for (int k=1; k<4; k++) {
        	TableRow questionRow= new TableRow(getActivity());
            TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            questionRow.setLayoutParams(rowLayoutParams);
            TextView questionIdentifier = new TextView(getActivity());
            questionIdentifier.setText(q.getQuestionIdentifier());
            questionIdentifier.setWidth(Q_ID_WIDTH);
            questionRow.addView(questionIdentifier);
            TextView questionText = new TextView(getActivity());
            questionText.setText(q.getText());
            questionText.setWidth(Q_TEXT_WIDTH);
            questionRow.addView(questionText);
            
            RadioGroup radioButtons = new RadioGroup(getActivity());
            radioButtons.setOrientation(RadioGroup.HORIZONTAL);
            
            for(Option option : q.options()) {
            	RadioButton button = new RadioButton(getActivity());
            	TableRow.LayoutParams buttonRowParams = new TableRow.LayoutParams(
            			TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            	button.setLayoutParams(buttonRowParams);
            	button.setText(option.getText());
            	radioButtons.addView(button, q.options().indexOf(option));
            }
            questionRow.addView(radioButtons);
            
            gridSelectTableLayout.addView(questionRow, k);
        }

//        ViewGroup questionComponent = (LinearLayout) v.findViewById(R.id.question_component);        
//        mValidationTextView = (TextView) v.findViewById(R.id.validation_text);
//
//        // Overridden by subclasses to place their graphical elements on the fragment.
//        createQuestionComponent(questionComponent);
//        deserialize(mResponse.getText());
        
        return v;
    }
	
	@Override
	protected void createQuestionComponent(ViewGroup questionComponent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void deserialize(String responseText) {
		// TODO Auto-generated method stub
		
	}

}
