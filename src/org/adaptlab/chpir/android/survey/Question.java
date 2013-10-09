package org.adaptlab.chpir.android.survey;

import java.util.ArrayList;

public class Question {
	public class QuestionTypes {
		public final static int SELECT_ONE = 0;
		public final static int SELECT_MULTIPLE = 1;
		public final static int SELECT_ONE_WRITE_OTHER = 2;
		public final static int SELECT_MULTIPLE_WRITE_OTHER = 3;
		public final static int FREE_RESPONSE = 4;
		public final static int SLIDER = 5;
		public final static int FRONT_PICTURE = 6;
		public final static int REAR_PICTURE = 7;
	}
	
	private String mText;
	private int mQuestionType;
	private ArrayList<String> mOptions;

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text;
	}

	public int getQuestionType() {
		return mQuestionType;
	}

	public void setQuestionType(int questionType) {
		mQuestionType = questionType;
	}
	
	public boolean hasOptions() {
		return mOptions != null;
	}
	
	public ArrayList<String> getOptions() {
		return mOptions;
	}
	
	public void addOption(String option) {
		if (mOptions == null) {
			mOptions = new ArrayList<String>();
		}
		
		mOptions.add(option);
	}
}
