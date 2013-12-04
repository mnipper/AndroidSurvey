package org.adaptlab.chpir.android.survey.Models;

import static org.powermock.api.mockito.PowerMockito.when;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Question.class})
public class QuestionTranslationTest extends ActiveAndroidTestBase {
	private static final String TABLE = "QuestionTranslations";
	private static final String LANGUAGE = "Gibberish";
	private static final String TEXT = "Aliens";
	
	private QuestionTranslation translation;
	private Question question;
	
	@Override
	protected void onSetup() {
		translation = new QuestionTranslation();
		question = mock(Question.class);
		when(tableInfo.getTableName()).thenReturn(TABLE);
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
		assertEquals(translation, QuestionTranslation.findByLanguage(LANGUAGE));
	}
}
