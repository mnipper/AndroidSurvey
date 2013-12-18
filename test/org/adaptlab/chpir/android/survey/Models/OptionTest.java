package org.adaptlab.chpir.android.survey.Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.util.Log;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Option.class, Question.class, Instrument.class, Log.class, AdminSettings.class })
public class OptionTest extends ActiveAndroidTestBase {
	private static final String TEXT = "this text";
	private static final Long REMOTE_ID = 023121L;
	private static final String TABLE = "Option";
	private static final String LANGUAGE = "en";
	private static final String TAG = "AdminSettings";
	private static final Integer ONE = 1;
	private static final String MESSAGE = "msg";
	
	private Option option;
	private Question question;
	private Instrument instrument;
	private AdminSettings adminSettings;

	@Override
	public void onSetup() {
		Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
		option = new Option();
		question = mock(Question.class);
		instrument = spy(new Instrument());
		adminSettings = spy(new AdminSettings());
		//adminSettings = mock(AdminSettings.class);
		when(tableInfo.getTableName()).thenReturn(TABLE);
		//System.setProperty("dexmaker.dexcache", "/cache");
		//System.setProperty("dexmaker.dexcache", context.getCacheDir().getPath());
	}
	
	@Test
	public void shouldSetAndGetQuestion() throws Exception {
		option.setQuestion(question);
		assertEquals(question, option.getQuestion());
	}
	
	@Test
	public void shouldSetAndGetRemoteId() throws Exception {
		option.setRemoteId(REMOTE_ID);
		assertEquals(REMOTE_ID, option.getRemoteId());
	}
	
	@Test 	
	public void shouldSetAndGetTextIfSameInstrumentLanguage() throws Exception {
		//PowerMock.mockStatic(Instrument.class);
		//PowerMock.mockStatic(Log.class);
		//PowerMockito.spy(AdminSettings.class);
		//PowerMockito.spy(Log.class);
		//PowerMockito.doNothing().when(Log.class);
		//when(Log.i(TAG, "Creating new admin settings instance")).thenReturn(ONE);
		//Log log = PowerMock.createMock(Log.class);
		//when(log.i(TAG, MESSAGE)).thenReturn(ONE);
		//when(adminSettings.getInstance()).thenReturn(new AdminSettings());
		//doNothing().when(adminSettings).logger(TAG, MESSAGE);
		AdminSettings admin = mock(AdminSettings.class);
		doReturn(admin).when(AdminSettings.getInstance());
		when(question.getInstrument()).thenReturn(instrument);
		when(instrument.getLanguage()).thenReturn(LANGUAGE);
		when(Instrument.getDeviceLanguage()).thenReturn(LANGUAGE);
		option.setQuestion(question);
		option.setText(TEXT);
		assertEquals(TEXT, option.getText());
	}
	
	@Test
	public void shouldFindByRemoteId() throws Exception {
		PowerMockito.mockStatic(Option.class);
		when(Option.findByRemoteId(REMOTE_ID)).thenReturn(option);	
		option.setRemoteId(REMOTE_ID);
		assertSame(option, Option.findByRemoteId(REMOTE_ID));
		PowerMockito.verifyStatic();
	}
	
	@Test
	public void shouldCreateObjectFromJson() throws Exception {
		//TODO: Implement
	}
}
