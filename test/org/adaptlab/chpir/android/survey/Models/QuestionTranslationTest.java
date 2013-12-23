package org.adaptlab.chpir.android.survey.Models;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
@PrepareForTest({ Question.class, QuestionTranslation.class })
public class QuestionTranslationTest {
	private static final String LANGUAGE = "Gibberish";
	private static final String TEXT = "Aliens";
	
	private QuestionTranslation translation;
	private Question question;
	
	@Before
	public void onSetup() throws Exception {
		translation = spy(new QuestionTranslation());
		question = mock(Question.class);
	}
	
	@Test
	public void shouldSetAndGetQuestion() throws Exception {
		translation.setQuestion(question);
		assertEquals(question, translation.getQuestion());
	}
	
	@Test
	public void shouldSetAndGetLanguage() throws Exception {
		translation.setLanguage(LANGUAGE);
		assertEquals(LANGUAGE, translation.getLanguage());
	}
	
	@Test
	public void shouldSetAndGetText() throws Exception {
		translation.setText(TEXT);
		assertEquals(TEXT, translation.getText());
	}
	
	@Test
	public void shouldFindQuestionTranslationBasedOnLanguage() throws Exception {
		translation.setLanguage(LANGUAGE);
		//assertEquals(translation, QuestionTranslation.findByLanguage(LANGUAGE));
		//TODO FIX STATIC MOCKING
	}
}
