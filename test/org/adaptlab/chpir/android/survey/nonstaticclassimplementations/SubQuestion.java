package org.adaptlab.chpir.android.survey.nonstaticclassimplementations;

import java.util.List;

import org.adaptlab.chpir.android.survey.Models.Question;

public class SubQuestion extends Question {

	public SubQuestion() {
		super();
	}
	
	public List<Question> getAllNonStatic() {
		return super.getAll();
	}
	
	public Question findByRemoteIdNonStatic(Long id) {
		return super.findByRemoteId(id);
	}
	
	public Question findByQuestionIdentifierNonStatic(String identifier) {
		return super.findByQuestionIdentifier(identifier);
	}
}
