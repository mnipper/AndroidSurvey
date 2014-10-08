package org.adaptlab.chpir.android.survey;

import java.util.ArrayList;

import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Survey;
import org.adaptlab.chpir.android.survey.Tasks.SendResponsesTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ReviewPageFragment extends ListFragment {
	public final static String EXTRA_REVIEW_QUESTION_IDS = "org.adaptlab.chpir.android.survey.review_question_ids";
	public final static String EXTRA_REVIEW_SURVEY_ID = "org.adaptlab.chpir.android.survey.review_survey_id";
	private ArrayList<Question> mSkippedQuestions;
	private Survey mSurvey;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mSkippedQuestions = new ArrayList<Question>();
        ArrayList<String> skippedQuestionsIds = new ArrayList<String>();
    	skippedQuestionsIds = getActivity().getIntent().getExtras().getStringArrayList(EXTRA_REVIEW_QUESTION_IDS);
    	mSurvey = Survey.load(Survey.class, getActivity().getIntent().getExtras().getLong(EXTRA_REVIEW_SURVEY_ID));
    	for (String id : skippedQuestionsIds) {
    		Question q = Question.findByQuestionIdentifier(id);
    		if (q != null) {
    			mSkippedQuestions.add(q);
    		}
    	}
        setListAdapter(new QuestionAdapter(mSkippedQuestions));
        getActivity().setTitle("Skipped Questions");
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		Question question = ((QuestionAdapter) getListAdapter()).getItem(position);
		setReturnResults(question.getRemoteId());
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_review, menu);
	}
	
	@Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_item_back).setEnabled(true).setVisible(true);
        menu.findItem(R.id.menu_item_complete).setEnabled(true).setVisible(true);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_back:
			returnToSurvey();
			return true;
		case R.id.menu_item_complete:
			completeSurvey();
			return true;
		default:
			return super.onOptionsItemSelected(item);		
		}
	}
	
	private void returnToSurvey() {
		setReturnResults(mSkippedQuestions.get(0).getRemoteId());
	}
	
	private void completeSurvey() {
		mSurvey.setAsComplete();
		mSurvey.save();
		setReturnResults(Long.MIN_VALUE);
		new SendResponsesTask(getActivity()).execute();
	}
	
	private void setReturnResults(Long id) {
		Intent i = new Intent();
		i.putExtra(SurveyFragment.EXTRA_QUESTION_ID, id);
		getActivity().setResult(Activity.RESULT_OK, i);
		getActivity().finish();
	}
	
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
            questionTextView.setText(Html.fromHtml(question.getText()));
            questionTextView.setMaxLines(2);

            return convertView;
        }		
	}
}
