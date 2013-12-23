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
@PrepareForTest({ Option.class, OptionTranslation.class })
public class OptionTranslationTest {
	private static final String LANGUAGE = "Gibberish";
	private static final String TEXT = "Aliens";
	
	private OptionTranslation translation;
	private Option option;
	
	@Before
	public void onSetup() throws Exception {
		translation = spy(new OptionTranslation());
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
