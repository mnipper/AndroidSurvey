package org.adaptlab.chpir.android.activerecordcloudsync;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.adaptlab.chpir.android.survey.Models.Survey;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricTestRunner;
//import org.powermock.api.mockito.PowerMockito.expect;




import android.util.Log;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ HttpFetchr.class, ActiveRecordCloudSync.class, Log.class, ReceiveModel.class })
public class HttpFetchrTest {
    private static final String TAG = "HttpFetchr";
    private static final String END_POINT = "http://127.0.0.1:3000";

	private HttpFetchr httpFetchr;
	private String remoteTableName = "Instruments";
	
	@Before
	public void onSetup() throws Exception {
		remoteTableName = "Instruments";
		httpFetchr = new HttpFetchr(remoteTableName, ReceiveModel.class);
	}
	
	@Test
	public void shouldGetUrlNull() throws Exception {
		/*String urlSpec = END_POINT;
		assertNull(Whitebox.invokeMethod(httpFetchr, "getUrlBytes", urlSpec));
		//TODO FIX
		*/
	}
	
	@Test
	public void shouldReturnUrlBytes() throws Exception {
		//TODO Implement
	}
	
	@Test	//TODO Check if mocking is correct
	public void shouldReturnEndPointNull() throws Exception {
		int ans;
		PowerMockito.mockStatic(ActiveRecordCloudSync.class);
		PowerMockito.mockStatic(Log.class);
		PowerMockito.when(ActiveRecordCloudSync.getEndPoint()).thenReturn(null);
		PowerMockito.when(Log.i(TAG, "ActiveRecordCloudSync end point is not set!")).thenReturn(ans = 1);
		httpFetchr.fetch();
		assertEquals(ans, 1);
	}
	
	@Test	//TODO Complete
	public void shouldCreateObjectFromTable() throws Exception {
		PowerMockito.mockStatic(ActiveRecordCloudSync.class);
		PowerMockito.mockStatic(Log.class);
		PowerMockito.when(ActiveRecordCloudSync.getEndPoint()).thenReturn(END_POINT);
		ReceiveModel model = mock(ReceiveModel.class);
		JSONObject jsonObject = mock(JSONObject.class);
		httpFetchr.fetch();
		//verify(model, times(1)).createObjectFromJSON(jsonObject);
		//assertEquals(END_POINT + remoteTableName, httpFetchr.fetch());
	}
}
