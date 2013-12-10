package org.adaptlab.chpir.android.survey.nonstaticclassimplementations;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragmentFactory;
import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.adaptlab.chpir.android.survey.Models.Survey;
import org.adaptlab.chpir.android.survey.QuestionFragments.FreeResponseQuestionFragment;

import android.os.Bundle;
import android.view.ViewGroup;

public class SubQuestionFragment extends QuestionFragment {
	private Question mQuestion;
    private Survey mSurvey;
    private Response mResponse;
    private Instrument mInstrument;
    private SubModel model;
    private SubQuestion question;
    
	@Override
	protected void createQuestionComponent(ViewGroup questionComponent) {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		long questionId = getArguments().getLong(QuestionFragmentFactory.EXTRA_QUESTION_ID, -1);
        long surveyId = getArguments().getLong(QuestionFragmentFactory.EXTRA_SURVEY_ID, -1);

        if (questionId != -1 && surveyId != -1) {
        	model = new SubModel(); 
        	question = new SubQuestion();	//TODO This's illogical!
            mQuestion = (Question) question.findByRemoteIdNonStatic(questionId);
            mSurvey = new Survey(); //model.loadNonStatic(Survey.class, surveyId);
            mResponse = new Response();
            mResponse.setQuestion(mQuestion);
            mResponse.setSurvey(mSurvey);
            mInstrument = mSurvey.getInstrument();
        }
	}

}
