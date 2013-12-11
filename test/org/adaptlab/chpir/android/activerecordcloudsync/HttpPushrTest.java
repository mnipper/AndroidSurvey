package org.adaptlab.chpir.android.activerecordcloudsync;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.adaptlab.chpir.android.survey.Models.Survey;
import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.activeandroid.query.Sqlable;

import android.util.Log;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ActiveRecordCloudSync.class, Log.class, HttpClient.class, Select.class, From.class, Survey.class, Sqlable.class })
public class HttpPushrTest {
	private static final String TAG = "HttpPushr";
	private HttpPushr pushr;
	private String remoteTableName;
	private Survey survey;
	private From from;
	private Select select;
	
	@Before
	public void onSetup() throws Exception {
		//System.setProperty("dexmaker.dexcache", "/sdcard");
		remoteTableName = "Surveys";
		survey = mock(Survey.class);
		pushr = new HttpPushr(remoteTableName, Survey.class);
		from = PowerMock.createMock(From.class);
		
		select = PowerMock.createMock(Select.class);
		PowerMockito.mockStatic(ActiveRecordCloudSync.class);
		PowerMockito.mockStatic(Log.class);
	}
	
	@Test
	public void shouldReturnIfEndPointIsNull() throws Exception {
		int ans;
		PowerMockito.when(ActiveRecordCloudSync.getEndPoint()).thenReturn(null);
		PowerMockito.when(Log.i(TAG, "ActiveRecordCloudSync end point is not set!")).thenReturn(ans = 1);
		pushr.push(); // TODO
		assertEquals(ans, 1);
	}
	
	@Test
	public void shouldCreateListOfAllElements() throws Exception {
		when(new Select().from(Survey.class)).thenReturn(from);
		pushr.push();
		verify(select, times(1)).from(Survey.class);
	}
	
	@Test
	public void shouldNotGetParamsIfNoHttpClientIsCreated() throws Exception {
		HttpClient client = mock(HttpClient.class);
		pushr.push();
		verify(client, never()).getParams();
	}

}
