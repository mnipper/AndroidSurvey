package org.adaptlab.chpir.android.survey.Models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricTestRunner;

import android.view.Gravity;

@RunWith(RobolectricTestRunner.class)
public class InstrumentTest {
	private static final Long REMOTE_ID = 12382903L;
	private static final String TITLE = "This is the title";
	private static final String LANGUAGE = "en";
	private static final String ALIGNMENT = "left";
	private static final int VERSION_NUMBER = 02;
	private static final String RIGHT = "right";
	private static final String LEFT = "left";
	
	private Instrument instrument;
	private Instrument spyInstrument;
	private Question question;
	private Survey survey;
	private AdminSettings admin;

	@Before
	public void onSetup() {
		instrument = new Instrument();
		spyInstrument = spy(new Instrument());
		question = mock(Question.class);
		survey = mock(Survey.class);
		admin = spy(new AdminSettings());
	}

	@Test
	public void shouldSetAndReturnRemoteId() throws Exception {
		instrument.setRemoteId(REMOTE_ID);
		assertThat(instrument.getRemoteId(), equalTo(REMOTE_ID));
	}
	
	@Test
	public void shouldReturnString() throws Exception {
		instrument.setTitle(TITLE);
		assertEquals(instrument.toString(), TITLE);
	}
	
	@Test
	public void shouldSetAndReturnVersionNumber() throws Exception {
		Whitebox.setInternalState(instrument, "mVersionNumber", VERSION_NUMBER);
		assertEquals(VERSION_NUMBER, instrument.getVersionNumber());
	}
	
	@Test
	public void shouldSetAndGetLanguage() throws Exception {
		Whitebox.setInternalState(instrument, "mLanguage", LANGUAGE);
		assertEquals(LANGUAGE, instrument.getLanguage());
	}

	@Test
	public void shouldReturnDefaultGravityLeft() throws Exception {
		doReturn(LEFT).when(spyInstrument).getAlignment();
		assertEquals(Gravity.LEFT, spyInstrument.getDefaultGravity());
	}
	
	@Test
	public void shouldReturnDefaultGravityRight() throws Exception {
		doReturn(RIGHT).when(spyInstrument).getAlignment();
		assertEquals(Gravity.RIGHT, spyInstrument.getDefaultGravity());
	}

	@Test 
	public void shouldReturnTitleIfDeviceAndInstrumentLanguagesSame() throws Exception {
		//TODO Implement
	}
	
	@Test
	public void shouldReturnInstrumentTranslationTitleIfLanguageDifferent() throws Exception {
		// TODO Implement
	}
	
	@Test
	public void shouldReturnAlignmentIfDeviceAndInstrumentLanguagesSame() throws Exception {
		// TODO Implement
	}
	
	@Test
	public void shouldReturnInstrumentTranslationAlignmentIfLanguageDifferent() throws Exception {
		// TODO Implement
	}
	
	@Test
	public void shouldReturnListOfQuestions() throws Exception {
		LinkedList<Question> list = new LinkedList<Question>();
		list.add(question);
		when(question.getInstrument()).thenReturn(spyInstrument);
		doReturn(list).when(spyInstrument).questions();
		assertThat(spyInstrument.questions(), instanceOf(LinkedList.class));
	}
	
	@Test
	public void shouldReturnListOfAllInstruments() throws Exception {
		assertThat(Instrument.getAll(), instanceOf(ArrayList.class));
	}
	
	@Test	
	public void shouldReturnInstrumentBasedOnRemoteId() throws Exception {
		//TODO Implement
	}
	
	@Test
	public void shouldReturnListOfSurveys() throws Exception {
		LinkedList<Survey> list = new LinkedList<Survey>();
		list.add(survey);
		doReturn(list).when(spyInstrument).surveys();
		assertThat(spyInstrument.surveys(), instanceOf(LinkedList.class));
		assertThat(spyInstrument.surveys().size(), equalTo(1));
	}
	
	@Test
	public void shouldReturnListOfInstrumentTranslations() throws Exception {
		InstrumentTranslation translation = mock(InstrumentTranslation.class);
		Instrument spy = spy(new Instrument());
		LinkedList<InstrumentTranslation> list = new LinkedList<InstrumentTranslation>();
		list.add(translation);
		doReturn(list).when(spy).translations();
		assertThat(spy.translations(), instanceOf(LinkedList.class));
		assertThat(spy.translations().size(), equalTo(1));
	}
	
	@Test 
	public void shouldReturnTypeFace() throws Exception {
		//TODO Implement
	}

	@Test	
	public void shouldCreateInstrumentFromJsonObject() throws Exception {
		//TODO Implement
	}

}
