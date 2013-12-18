package org.adaptlab.chpir.android.survey.Models;

import static org.powermock.api.mockito.PowerMockito.when;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Option.class})
public class OptionTranslationTest extends ActiveAndroidTestBase {
	private static final String TABLE = "OptionTranslations";
	private static final String LANGUAGE = "Gibberish";
	private static final String TEXT = "Aliens";
	
	private OptionTranslation translation;
	private Option option;
	
	@Override
	protected void onSetup() {
		when(tableInfo.getTableName()).thenReturn(TABLE);
		translation = new OptionTranslation();
		option = mock(Option.class);
	}
	
	@Test
	public void shouldSetAndGetOption() throws Exception {
		translation.setOption(option);
		assertEquals(option, translation.getOption());
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
	
	@Test	//TODO fix static mocking
	public void shouldFindTranslationBasedOnLanguage() throws Exception {
		translation.setLanguage(LANGUAGE);
		//assertEquals(translation, OptionTranslation.findByLanguage(LANGUAGE));
	}
	
}
