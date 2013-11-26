package org.adaptlab.chpir.android.survey.Models;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class InstrumentTest {
	private Instrument instrument;
	private static final String TITLE = "myTitle";
	private static final Long REMOTE_ID = 12382903L;
	
	@Before
	public void setUp() {
		instrument = new Instrument();
	}
	
	@Test
	public void shouldReturnTitle() throws Exception {
		instrument.setTitle(TITLE);
		assertThat(instrument.getTitle(), equalTo(TITLE));
	}
	
	@Test
	public void shouldReturnRemoteId() throws Exception {
		instrument.setRemoteId(REMOTE_ID);
		assertThat(instrument.getRemoteId(), equalTo(REMOTE_ID));
	}
	
	@Test
	public void shouldReturnInstrumentBasedOnRemoteId() throws Exception {
		Instrument inst = new Instrument();	
		inst.setRemoteId(REMOTE_ID);
		assertThat(Instrument.findByRemoteId(REMOTE_ID), equalTo(inst));
	}
	
	/*@Test
	public void shouldCreateInstrumentFromJsonObject() throws Exception {
		Instrument inst2 = new Instrument();
		inst2.setRemoteId(REMOTE_ID);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("instrument_id", REMOTE_ID);
		Long remoteId = jsonObject.getLong("instrument_id");
		Instrument inst1 = Instrument.findByRemoteId(remoteId);
		assertThat(inst1, instanceOf(Instrument.class)); //Wrong test
	}*/
}
