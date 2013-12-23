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

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricTestRunner;

import android.util.Log;
import android.view.Gravity;

@RunWith(RobolectricTestRunner.class)
@PrepareForTest({ Survey.class, Response.class, Question.class, AdminSettings.class, JSONObject.class, Log.class, Instrument.class, InstrumentTranslation.class })
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
		/*spyInstrument.setTitle(TITLE);
		assertEquals(TITLE, instrument.getTitle());*/
		//TODO Fix static methods mocking
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
		PowerMockito.mockStatic(Instrument.class);
		assertThat(Instrument.getAll(), instanceOf(ArrayList.class));
	}
	
	@Test	
	public void shouldReturnInstrumentBasedOnRemoteId() throws Exception {
		//TODO FIX STATIC MOCKING
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

	/*@Test	//TODO Fix json Stub Exception
	public void shouldCreateInstrumentFromJsonObject() throws Exception {
		PowerMockito.mockStatic(JSONObject.class);
		PowerMockito.mockStatic(Log.class);
		//PowerMockito.mockStatic(Instrument.class);
		//stub(Instrument.getDeviceLanguage()).toReturn(LANGUAGE);
		JSONObject json = mock(JSONObject.class);
		JSONArray jsonArray = mock(JSONArray.class);
		//InstrumentTranslation translation = mock(InstrumentTranslation.class);
		
		when(json.getLong("id")).thenReturn(REMOTE_ID);
		when(json.getString("language")).thenReturn(LANGUAGE);
		when(json.getString("alignment")).thenReturn(ALIGNMENT);
		when(json.getInt("current_version_number")).thenReturn(VERSION_NUMBER);
		when(json.getJSONArray("translations")).thenReturn(jsonArray); //TODO ???
		try {
			json.put("id", 0121214L);
			json.put("title", "My Title");
			json.put("language", "English");
			json.put("alignment", "left");
			json.put("translation", translation);
		} catch (JSONException je) {}
		
		//Instrument fromJson = mock(Instrument.class);
		//Instrument fromJson = new Instrument();
		//PowerMockito.mockStatic(Instrument.class);
		//PowerMockito.when(Instrument.getDeviceLanguage()).thenReturn(LANGUAGE);
		fromJson.createObjectFromJSON(json);
		assertThat(fromJson.getAlignment(), equalTo("left"));
		assertThat(fromJson.getRemoteId(), equalTo(0121214L));
		assertThat(fromJson.getTitle(), equalTo("My Title"));
		assertThat(fromJson.getLanguage(), equalTo("English"));
		
		//Instrument fromJson = PowerMock.createMock(Instrument.class);
		Instrument fromJson = mock(Instrument.class);
		PowerMockito.mockStatic(Instrument.class);
		when(Instrument.getDeviceLanguage()).thenReturn(LANGUAGE);
		doCallRealMethod().when(fromJson).createObjectFromJSON(json);
		when(fromJson.getAlignment()).thenCallRealMethod();
		when(fromJson.getRemoteId()).thenCallRealMethod();
		when(fromJson.getTitle()).thenCallRealMethod();
		when(fromJson.getLanguage()).thenCallRealMethod();
		//doNothing().when(fromJson).save();
		
		fromJson.createObjectFromJSON(json);
		assertThat(fromJson.getAlignment(), equalTo(ALIGNMENT));
		//assertThat(fromJson.getRemoteId(), equalTo(0121214L));
		//assertThat(fromJson.getTitle(), equalTo("My Title"));
		assertThat(fromJson.getLanguage(), equalTo(LANGUAGE));
	}*/

}
