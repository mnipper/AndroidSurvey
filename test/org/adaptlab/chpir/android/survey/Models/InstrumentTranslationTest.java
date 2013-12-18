package org.adaptlab.chpir.android.survey.Models;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Survey.class, Response.class,
		Question.class, InstrumentTranslation.class })
public class InstrumentTranslationTest extends ActiveAndroidTestBase {

	private static final String TABLE = "InstrumentTranslations";
	private static final String TITLE = "Instrument Translation Title";
	private static final String LANGUAGE = "Gibberish";
	private static final String ALIGNMENT = "Left";
	
	private InstrumentTranslation translation;
	private Instrument instrument;
	
	@Override
	protected void onSetup() {
		when(tableInfo.getTableName()).thenReturn(TABLE);
		translation = new InstrumentTranslation();
		instrument = mock(Instrument.class);
	}
	
	@Test
	public void shouldSetAndGetTitle() throws Exception {
		translation.setTitle(TITLE);
		assertEquals(TITLE, translation.getTitle());
	}
	
	@Test
	public void shouldSetAndGetLanguage() throws Exception {
		translation.setLanguage(LANGUAGE);
		assertEquals(LANGUAGE, translation.getLanguage());
	}
	
	@Test
	public void shouldSetAndGetAlignment() throws Exception {
		translation.setAlignment(ALIGNMENT);
		assertEquals(ALIGNMENT, translation.getAlignment());
	}
	
	@Test
	public void shouldSetAndGetInstrument() throws Exception {
		translation.setInstrument(instrument);
		assertEquals(instrument, translation.getInstrument());
	}
	
	@Test	//TODO fix static mocking
	public void shouldReturnInstrumentTranslationBasedOnLanguage() throws Exception {
		translation.setLanguage(LANGUAGE);
		//assertEquals(translation, InstrumentTranslation.findByLanguage(LANGUAGE));
	}
	
}
