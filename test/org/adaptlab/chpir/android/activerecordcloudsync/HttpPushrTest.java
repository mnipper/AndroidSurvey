package org.adaptlab.chpir.android.activerecordcloudsync;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.List;

import org.adaptlab.chpir.android.survey.Models.Survey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricTestRunner;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;

@RunWith(RobolectricTestRunner.class)
public class HttpPushrTest {
	private static final String TAG = "HttpPushr";
	private static final String END_POINT = "localhost:3000";
	private HttpPushr pushr;
	private String remoteTableName;
	private Survey survey;
	private From from;
	private Select select;
	private List<? extends SendModel> elements;
	
	@Before
	public void onSetup() throws Exception {
		remoteTableName = "Surveys";
		survey = mock(Survey.class);
		pushr = spy(new HttpPushr(remoteTableName, Survey.class));
	}
	
	@Test
	public void shouldNotBeNull() throws Exception {
		assertNotNull(pushr);
		assertNotNull(Whitebox.getInternalState(pushr, "mSendTableClass"));
		assertNotNull(Whitebox.getInternalState(pushr, "mRemoteTableName"));
	}
	
	/*@Test
	public void shouldReturnIfEndPointIsNull() throws Exception {
		when(ActiveRecordCloudSync.getEndPoint()).thenReturn(null);
		pushr.push(); 
		
	}*/
	
	//TODO Write remaining Unit Tests
	
	/*@Test
	public void shouldCreateListOfAllElements() throws Exception {
		spy(new ActiveRecordCloudSync());
		doReturn(END_POINT).when(ActiveRecordCloudSync.class);
		doReturn(elements).when(pushr).getElements();
		pushr.push();
		verify(pushr, times(1)).getElements();
	}*/
	
	/*@Test
	public void shouldNotGetParamsIfNoHttpClientIsCreated() throws Exception {
		HttpClient client = mock(HttpClient.class);
		pushr.push();
		verify(client, never()).getParams();
	}*/

}
