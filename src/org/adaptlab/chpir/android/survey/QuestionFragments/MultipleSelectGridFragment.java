package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.List;

import org.adaptlab.chpir.android.survey.GridFragment;
import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.Models.Option;
import org.adaptlab.chpir.android.survey.Models.Question;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MultipleSelectGridFragment extends GridFragment {

	private static final String TAG = "MultipleSelectGridFragment";
	private static int Q_ID_WIDTH = 200;
	private static int Q_TEXT_WIDTH = 500;
	private static int O_COL_WIDTH = 300;
	private List<Question> mQuestions;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_table_question, parent,false);
		TableLayout gridSelectTableLayout = (TableLayout) v.findViewById(R.id.body_table_view);
		TableRow headerRow = new TableRow(getActivity());
		TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
		headerRow.setLayoutParams(lp);
		headerRow.setBackgroundColor(Color.BLUE);
		mQuestions = getQuestions();
		
		for (int k = 0; k < mQuestions.size(); k++) {
			Question q = mQuestions.get(k);
			TextView qid = new TextView(getActivity());
			qid.setText("Q_ID");
			qid.setWidth(Q_ID_WIDTH);
			headerRow.addView(qid);
			TextView q_text = new TextView(getActivity());
			q_text.setText("Q_TEXT");
			q_text.setWidth(Q_TEXT_WIDTH);
			headerRow.addView(q_text);
			for (Option opt : q.options()) {
				TextView optionCol = new TextView(getActivity());
				optionCol.setText(opt.getText());
				optionCol.setWidth(O_COL_WIDTH);
				headerRow.addView(optionCol);
			}
			gridSelectTableLayout.addView(headerRow, 0);
			
			TableRow questionRow = new TableRow(getActivity());
			TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT);
			questionRow.setLayoutParams(rowLayoutParams);
			TextView questionIdentifier = new TextView(getActivity());
			questionIdentifier.setText(q.getQuestionIdentifier());
			questionIdentifier.setWidth(Q_ID_WIDTH);
			questionRow.addView(questionIdentifier);
			TextView questionText = new TextView(getActivity());
			questionText.setText(q.getText());
			questionText.setWidth(Q_TEXT_WIDTH);
			questionRow.addView(questionText);

			for (Option option : q.options()) {
				CheckBox checkbox = new CheckBox(getActivity());
				checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO save changes
					}
				});
				checkbox.setWidth(O_COL_WIDTH);
				questionRow.addView(checkbox);
			}

			gridSelectTableLayout.addView(questionRow, k);
		}

		return v;
	}

	@Override
	protected void deserialize(ViewGroup group, String responseText) {
		
	}

	@Override
	protected String serialize() { return null; }
	
}
