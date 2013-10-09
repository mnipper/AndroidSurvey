package org.adaptlab.chpir.android.survey;

public class Response {
	private Question mQuestion;
	private String mResponse;

	public Question getQuestion() {
		return mQuestion;
	}

	public void setQuestion(Question question) {
		mQuestion = question;
	}

	public String getResponse() {
		return mResponse;
	}

	public void setResponse(String response) {
		mResponse = response;
	}

}
