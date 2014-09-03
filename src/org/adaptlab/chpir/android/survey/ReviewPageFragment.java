package org.adaptlab.chpir.android.survey;

import java.util.ArrayList;

import org.adaptlab.chpir.android.survey.Models.Question;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ReviewPageFragment extends ListFragment {
	public final static String EXTRA_SKIPPED_QUESTIONS_IDS = "org.adaptlab.chpir.android.survey.skipped_questions_ids";
	private ArrayList<Question> mSkippedQuestions;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSkippedQuestions = new ArrayList<Question>();
        ArrayList<String> skippedQuestionsIds = new ArrayList<String>();
    	skippedQuestionsIds = getActivity().getIntent().getExtras().getStringArrayList(EXTRA_SKIPPED_QUESTIONS_IDS);
    	for (String id : skippedQuestionsIds) {
    		Question q = Question.findByQuestionIdentifier(id);
    		if (q != null) {
    			mSkippedQuestions.add(q);
    		}
    	}
        setListAdapter(new QuestionAdapter(mSkippedQuestions));
        getActivity().setTitle("You Skipped These Questions");
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {}
	
	private class QuestionAdapter extends ArrayAdapter<Question> {
		public QuestionAdapter(ArrayList<Question> questions) {
			super(getActivity(), 0, questions);
		}
		
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.review_page, null);
            }
            
            Question question = getItem(position);
            
            TextView questionNumberTextView = (TextView) convertView.findViewById(R.id.review_question_number);
            questionNumberTextView.setText(String.valueOf(question.getNumberInInstrument()));
            questionNumberTextView.setTextColor(Color.BLACK);
            
            TextView questionTextView = (TextView) convertView.findViewById(R.id.review_question_text);           
            questionTextView.setText(question.getText());

            return convertView;
        }
		
	}
}
