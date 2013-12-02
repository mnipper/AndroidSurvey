package org.adaptlab.chpir.android.survey.Models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import android.view.Gravity;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Survey.class, Instrument.class, Response.class,
		Question.class, AdminSettings.class })
public class InstrumentMock {
	private static final Long REMOTE_ID = 12382903L;
	private static final String TITLE = "This is the title";

	private Instrument instrument;
	private Question question;
	private Survey survey;

	@Before
	public void setUp() throws Exception {
		instrument = mock(Instrument.class);
		PowerMockito.mockStatic(Instrument.class);
		question = mock(Question.class);
		survey = mock(Survey.class);
	}

	@Test
	public void shouldReturnInstrumentBasedOnRemoteId() throws Exception {
		instrument.setRemoteId(REMOTE_ID);
		when(Instrument.findByRemoteId(REMOTE_ID)).thenReturn(instrument);
	}

	@Test
	public void shouldReturnListOfQuestions() throws Exception {
		question.setInstrument(instrument);
		assertThat(instrument.questions(), instanceOf(LinkedList.class));
		for (Question q : instrument.questions()) {
			assertThat(q.getInstrument(), equalTo(instrument));
		}
	}

	@Test
	public void shouldReturnListOfInstruments() throws Exception {
		assertThat(Instrument.getAll(), instanceOf(LinkedList.class));
	}

	@Test
	// TODO FIX
	public void shouldReturnAllInstruments() throws Exception {
		mock(Instrument.class);
		// assertThat(Instrument.getAll().size(), equalTo(2));
		// when(Instrument.getAll().size()).thenReturn(2);
	}

	@Test
	public void shouldReturnString() throws Exception {
		instrument.setTitle(TITLE);
		when(instrument.toString()).thenReturn(TITLE);
	}

	@Test
	public void shouldReturnListOfSurveys() throws Exception {
		survey.setInstrument(instrument);
		assertThat(instrument.surveys(), instanceOf(LinkedList.class));
		for (Survey s : instrument.surveys()) {
			assertThat(s.getInstrument(), equalTo(instrument));
		}
	}

	@Test
	// TODO FIX
	public void shouldSetAlignmentAndGetGravity() throws Exception {
		// Instrument privateInstrument =
		// PowerMock.createPartialMock(Instrument.class, "setAlignment",
		// String.class);
		Instrument privateInstrument = mock(Instrument.class);
		Whitebox.invokeMethod(privateInstrument, "setAlignment", "left");
		assertThat(privateInstrument.getAlignment(), equalTo("left"));
		assertThat(privateInstrument.getDefaultGravity(), equalTo(Gravity.LEFT));
		Whitebox.invokeMethod(privateInstrument, "setAlignment", "right");
		assertThat(privateInstrument.getDefaultGravity(),
				equalTo(Gravity.RIGHT));
	}

	@Test	//TODO Fix json Stub Exception
	public void shouldCreateInstrumentFromJsonObject() throws Exception {
		JSONObject json = new JSONObject();
		try {
			json.put("remote_id", 0121214L);
			json.put("title", "My Title");
			json.put("language", "English");
			json.put("alignment", "left");
		} catch (JSONException je) {
		}
		Instrument fromJson = mock(Instrument.class);
		fromJson.createObjectFromJSON(json);
		assertThat(fromJson.getAlignment(), equalTo("left"));
		assertThat(fromJson.getRemoteId(), equalTo(0121214L));
		assertThat(fromJson.getTitle(), equalTo("My Title"));
		assertThat(fromJson.getLanguage(), equalTo("English"));
	}

}
