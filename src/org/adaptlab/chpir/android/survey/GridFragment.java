package org.adaptlab.chpir.android.survey;

import java.util.List;

import org.adaptlab.chpir.android.survey.Models.Grid;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.adaptlab.chpir.android.survey.Models.Survey;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.activeandroid.Model;

public abstract class GridFragment extends QuestionFragment {
	public final static String EXTRA_GRID_ID = 
            "org.adaptlab.chpir.android.survey.grid_id";
	public final static String EXTRA_SURVEY_ID =
			"org.adaptlab.chpir.android.survey.survey_id";
	
	protected void createQuestionComponent(ViewGroup questionComponent){};
	
	private static final String TAG = "GridFragment";
	private Grid mGrid;
	private Survey mSurvey;
	private List<Question> mQuestions;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
        	mGrid = Grid.findByRemoteId(savedInstanceState.getLong(EXTRA_GRID_ID));
        	mQuestions = mGrid.questions();
        } else {
            mGrid = Grid.findByRemoteId(getArguments().getLong(EXTRA_GRID_ID));
            mQuestions = mGrid.questions();
        }
        init();
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
	}
	
	@Override
	public void init() {
		long surveyId = getArguments().getLong(EXTRA_SURVEY_ID);
		if (surveyId != -1) {
			mSurvey = Model.load(Survey.class, surveyId);
			createResponses();
		}
	}

	private void createResponses() {
		for (Question question : mGrid.questions()) {
			if (mSurvey.getResponseByQuestion(question) == null) {
				Response response = new Response();
				response.setQuestion(question);
				response.setSurvey(mSurvey);
				response.save();
			}
		}
	}
	
	@Override
	public void saveSpecialResponse(String specialResponse) {
		for (Question question : mGrid.questions()) {
			Response response = mSurvey.getResponseByQuestion(question);
			if (response != null) {
				response.setSpecialResponse(specialResponse);
				response.setDeviceUser(AuthUtils.getCurrentUser());
				response.save();
				if (AppUtil.DEBUG) Log.i(TAG, "Saved special response: " + response.getSpecialResponse() + 
						" for question: " + question.getQuestionIdentifier());
			}
		}
	}
	
	@Override
	public void clearCurrentResponse() {
		for (Question question : mGrid.questions()) {
			Response response = mSurvey.getResponseByQuestion(question);
			if (response != null) {
				response.setResponse("");
				response.save();
			}
		}
		deserialize("");
	}
	
	/*
	 * Return the special response belonging to the first question in the grid since
	 * all the questions in the grid have the same special response
	 */
	@Override
	public String getSpecialResponse() {
		if (mGrid != null && mSurvey != null) {
			Response firstResponse = mSurvey.getResponseByQuestion(mGrid.questions().get(0));
	    	if (firstResponse != null) {
		        return firstResponse.getSpecialResponse();
	    	} else {
		        return "";
	    	}
		} else {
			return "";
		}
    }

	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_GRID_ID, mGrid.getRemoteId());
        outState.putLong(EXTRA_SURVEY_ID, mSurvey.getId());
	}
	
	protected List<Question> getQuestions() {
		return mQuestions;
	}
	
	protected Grid getGrid() {
		return mGrid;
	}
	
	protected Survey getSurvey() {
		return mSurvey;
	}
}
