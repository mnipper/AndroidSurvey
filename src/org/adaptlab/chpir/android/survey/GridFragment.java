package org.adaptlab.chpir.android.survey;

import java.util.List;

import org.adaptlab.chpir.android.survey.Models.Grid;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.adaptlab.chpir.android.survey.Models.Survey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.activeandroid.Model;

public abstract class GridFragment extends QuestionFragment {
	public final static String EXTRA_GRID_ID = 
            "org.adaptlab.chpir.android.survey.grid_id";
	public final static String EXTRA_SURVEY_ID =
			"org.adaptlab.chpir.android.survey.survey_id";
	
	protected void createQuestionComponent(ViewGroup questionComponent){};
	protected void deserialize(String reponseText){};
	protected abstract void deserialize(ViewGroup group, String responseText);
	
	@SuppressWarnings("unused")
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
        setHasOptionsMenu(true);
        init();
        super.onCreate(savedInstanceState);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_GRID_ID, mGrid.getRemoteId());
        outState.putLong(EXTRA_GRID_ID, getId());
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
