package org.adaptlab.chpir.android.survey.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Options")
public class Option extends Model {

	@Column(name = "Question")
	private Question mQuestion;
	@Column(name = "Text")
	private String mText;
	
	public Option() {
		super();
	}
	
	public Question getQuestion() {
		return mQuestion;
	}
	public void setQuestion(Question question) {
		mQuestion = question;
	}
	public String getText() {
		return mText;
	}
	public void setText(String text) {
		mText = text;
	}

}
