package org.adaptlab.chpir.android.survey;

import java.util.ArrayList;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Questions")
public class Question extends Model {
	public static class QuestionTypes {
		public static boolean validQuestionType(int questionType) {
			return questionType >= 0 && questionType <= 7;
		}
		public final static int SELECT_ONE = 0;
		public final static int SELECT_MULTIPLE = 1;
		public final static int SELECT_ONE_WRITE_OTHER = 2;
		public final static int SELECT_MULTIPLE_WRITE_OTHER = 3;
		public final static int FREE_RESPONSE = 4;
		public final static int SLIDER = 5;
		public final static int FRONT_PICTURE = 6;
		public final static int REAR_PICTURE = 7;
	}
	
	@Column(name = "Text")
	private String mText;
	@Column(name = "QuestionType")
	private int mQuestionType;
	@Column(name = "QuestionID")
	private String mQuestionID;
	@Column(name = "Instrument")
	private Instrument mInstrument;
	
	private ArrayList<String> mOptions;
	
	public Question() {
		super();
		mOptions = new ArrayList<String>();
	}

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text;
	}

	public int getQuestionType() {
		return mQuestionType;
	}

	// Set question type to free response if invalid question type
	public void setQuestionType(int questionType) {
		if (QuestionTypes.validQuestionType(questionType)) {
			mQuestionType = questionType;
		} else {
			mQuestionType = QuestionTypes.FREE_RESPONSE;
		}
	}
	
	public boolean hasOptions() {
		return mOptions.size() > 0;
	}
	
	public ArrayList<String> getOptions() {
		return mOptions;
	}
	
	public void addOption(String option) {
		mOptions.add(option);
	}

}
