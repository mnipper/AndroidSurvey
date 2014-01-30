package org.adaptlab.chpir.android.survey.Models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class SurveyTest { 
	private static final String RESPONSE_TEXT = "This is the response";
	private static final Long REMOTE_ID = 12382903L;
	private static final String DEVICE_ID = "ThisIsTheDeviceId";
	private static final Integer VERSION_NUMBER = 34234;
	
	private Survey survey;
	private Instrument instrument;
	private Response response;
	private Question question;
	private Survey spySurvey;
	private JSONObject json1;
	private JSONObject json2;
	
	@Before
	public void onSetup() {
		survey = new Survey();
		instrument = mock(Instrument.class);
		response = mock(Response.class);
		question = mock(Question.class);
		spySurvey = spy(new Survey());
	}

	@Test
	public void shouldNotBeSent() throws Exception {
		assertThat(survey.isSent(), equalTo(false));
	}
	
	@Test 
	public void shouldNotBeReadyToSend() throws Exception {
		assertThat(survey.readyToSend(), equalTo(false));
	}
	
	@Test
	public void shouldGetUniqueUUID() throws Exception {
		String mUUID = UUID.randomUUID().toString();
		assertThat(survey.getUUID(), not(mUUID));
	}

	@Test
	public void shouldReturnUUID() throws Exception {
		assertNotNull(survey.getUUID());
	}

	@Test
	public void shouldSetAsSent() throws Exception {
		survey.setAsSent();
		assertThat(survey.isSent(), equalTo(true));
	}

	@Test
	public void shouldSetAsComplete() throws Exception {
		survey.setAsComplete();
		assertThat(survey.readyToSend(), equalTo(true));
	}
	
	@Test
	public void shouldSetAndGetSameInstrument() throws Exception {
		survey.setInstrument(instrument);
		assertEquals(instrument, survey.getInstrument());
	}
	
	@Test
	public void shouldSetResponsesAndGetListOfResponse() throws Exception {
		Response resp = mock(Response.class);
		when(resp.getText()).thenReturn(RESPONSE_TEXT);
		LinkedList<Response> list = new LinkedList<Response>();
		list.add(resp);
		Survey sur = spy(new Survey());
		resp.setSurvey(sur);
		doReturn(list).when(sur).responses();
		for(Response r : sur.responses()) {
			assertThat(r.getText(), equalTo(RESPONSE_TEXT));
		}
	}
	
	private void setUpJson() throws JSONException {
		doReturn(instrument).when(spySurvey).getInstrument();
		when(instrument.getRemoteId()).thenReturn(REMOTE_ID);
		when(instrument.getVersionNumber()).thenReturn(VERSION_NUMBER);
		doReturn(DEVICE_ID).when(spySurvey).getAdminInstanceDeviceIdentifier();
		json1 = spySurvey.toJSON();
		String js = json1.getString("survey");
		json2 = new JSONObject(js);
	}
	
	@Test
	public void jsonShouldNotBeNull() throws Exception {
		setUpJson();
		assertNotNull(json2);
	}
	
	@Test
	public void shouldHaveJsonStringInstrumentId() throws Exception {
		setUpJson();
		assertEquals(REMOTE_ID.toString(), json2.getString("instrument_id"));
	}
	
	@Test
	public void shouldHaveJsonStringInstrumentVersionNumber() throws Exception {
		setUpJson();
		assertEquals(VERSION_NUMBER.toString(), json2.getString("instrument_version_number"));
	}
	
	@Test
	public void shouldHaveJsonStringDeviceIdentifier() throws Exception {
		setUpJson();
		assertEquals(DEVICE_ID, json2.getString("device_uuid"));
	}
	
	@Test
	public void shouldHaveJsonStringUuid() throws Exception {
		setUpJson();
		assertNotNull(json2.getString("uuid"));
	}
	
   @Test
    public void shouldHaveJsonStringInstrumentTitle() throws Exception {
        setUpJson();
        assertEquals(DEVICE_ID, json2.getString("instrument_title"));
    }
}
