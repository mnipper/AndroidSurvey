package org.adaptlab.chpir.android.survey;

import java.util.ArrayList;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Instruments")
public class Instrument extends Model {
	private ArrayList<Question> mQuestions;
	
	@Column(name = "Title")
	private String mTitle;
	
	public Instrument() {
		super();
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
