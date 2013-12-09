package org.adaptlab.chpir.android.survey.Models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.activeandroid.Model;
import com.activeandroid.util.SQLiteUtils;

import android.content.ContentValues;
import android.util.Log;
import android.view.Gravity;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Survey.class, Response.class,
		Question.class, AdminSettings.class, JSONObject.class, Log.class, Instrument.class, SQLiteUtils.class, ContentValues.class })
public class InstrumentTest extends ActiveAndroidTestBase {
	private static final Long REMOTE_ID = 12382903L;
	private static final String TITLE = "This is the title";
	private static final String TABLE = "Instruments";
	private static final String LANGUAGE = "en";
	private static final String ALIGNMENT = "left";
	private static final int VERSION_NUMBER = 02;
	
	private Instrument instrument;
	private Question question;
	private Survey survey;

	@Override
	public void onSetup() {
		instrument = new Instrument();
		question = mock(Question.class);
		survey = mock(Survey.class);
		when(tableInfo.getTableName()).thenReturn(TABLE);
		
	}

	@Test
	public void shouldReturnRemoteId() throws Exception {
		instrument.setRemoteId(REMOTE_ID);
		assertThat(instrument.getRemoteId(), equalTo(REMOTE_ID));
	}

	@Test	//TODO: Fails because of Log - returns Stub!
	public void shouldReturnTitle() throws Exception {
		instrument.setTitle(TITLE);	
		assertThat(instrument.getTitle(), equalTo(TITLE));
	}
	
	@Test 
	// TODO: 
	/* http://eclipsesource.com/blogs/2012/06/15/serious-unit-testing-on-android/
	 * http://grepcode.com/file/repo1.maven.org/maven2/org.robolectric/robolectric/2.1.1/org/robolectric/shadows/ShadowTypeface.java#ShadowTypeface
	*/
	public void shouldReturnTypeFace() throws Exception {
		//assertThat(instrument.getTypeFace(Robolectric.application), equalTo(Typeface.DEFAULT));
	}
	
	@Test
	public void shouldReturnInstrumentBasedOnRemoteId() throws Exception {
		Instrument inst = mock(Instrument.class);
		PowerMockito.mockStatic(Instrument.class);
		when(SQLiteUtils.rawQuerySingle(eq(Instrument.class), anyString(), eq(new String[] {DUMMY}))).thenReturn(inst);
		when(SQLiteUtils.rawQuerySingle(eq(Instrument.class), anyString(), eq(new String[] {}))).thenReturn(inst);
		inst.setRemoteId(REMOTE_ID);
		assertEquals(inst, Instrument.findByRemoteId(REMOTE_ID));
		PowerMockito.verifyStatic();
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
		PowerMockito.mockStatic(Instrument.class);
		//Instrument inst = mock(Instrument.class);
		//when(SQLiteUtils.rawQuerySingle(eq(Instrument.class), anyString(), eq(new String[] {DUMMY}))).thenReturn(inst);
		//when(SQLiteUtils.rawQuerySingle(eq(Instrument.class), anyString(), eq(new String[] {}))).thenReturn(inst);
		assertThat(Instrument.getAll(), instanceOf(LinkedList.class));
		assertEquals(Instrument.getAll().size(), 1);
	}

	@Test
	public void shouldReturnString() throws Exception {
		instrument.setTitle(TITLE);
		assertEquals(instrument.toString(), TITLE);
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
		/*try {
			json.put("id", 0121214L);
			json.put("title", "My Title");
			json.put("language", "English");
			json.put("alignment", "left");
			json.put("translation", translation);
		} catch (JSONException je) {}*/
		
		//Instrument fromJson = mock(Instrument.class);
		//Instrument fromJson = new Instrument();
		//PowerMockito.mockStatic(Instrument.class);
		//PowerMockito.when(Instrument.getDeviceLanguage()).thenReturn(LANGUAGE);
		/*fromJson.createObjectFromJSON(json);
		assertThat(fromJson.getAlignment(), equalTo("left"));
		assertThat(fromJson.getRemoteId(), equalTo(0121214L));
		assertThat(fromJson.getTitle(), equalTo("My Title"));
		assertThat(fromJson.getLanguage(), equalTo("English"));*/
		
		when(sqliteDb.insert(anyString(), anyString(), any(ContentValues.class))).thenReturn(ID);
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
	}

}
