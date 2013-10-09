package org.adaptlab.chpir.android.survey;

import java.util.ArrayList;

public class Instrument {
	private ArrayList<Question> mQuestions;
	private String mTitle;
	
	public Instrument() {
		mQuestions = new ArrayList<Question>();
	}

	public ArrayList<Question> getQuestions() {
		return mQuestions;
	}

	public void addQuestion(Question question) {
		mQuestions.add(question);
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}
	
}
