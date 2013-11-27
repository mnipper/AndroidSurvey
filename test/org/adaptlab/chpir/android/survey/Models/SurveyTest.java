package org.adaptlab.chpir.android.survey.Models;

import static org.junit.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.not;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import cz.sigler.android.aavalidation.ModelSupportFactory;
import cz.sigler.android.aavalidation.api.model.IModelSupport;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class SurveyTest {
	private Survey survey;
	private Instrument instrument;

	@Before
	public void setUp() {
		survey = new Survey();
		instrument = new Instrument();
		//survey = mock(Survey.class);
		//instrument = mock(Instrument.class);
	}
	
	@Test
	public void testInitialState() throws Exception {
		assertThat(survey.isSent(), equalTo(false));
		assertThat(survey.readyToSend(), equalTo(false));
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
	
	@Test	//TODO Currently failing
	public void shouldSetAndGetSameInstrument() throws Exception {
		/*Survey testSurvey = new Survey();
		IModelSupport<Survey> survey = ModelSupportFactory.wrapModel(testSurvey);
		Instrument testInstrument = new Instrument();
		IModelSupport<Instrument> instrument = ModelSupportFactory.wrapModel(testInstrument);
		//survey.setInstrument(instrument);
		survey.setFieldValue("Instrument", instrument);
		survey.save();
		assertThat((Instrument)survey.getFieldValue("Instrument"), equalTo(testInstrument));*/
		
		//survey.setInstrument(instrument);
		//Instrument inst = new Instrument();
		//assertThat(instrument, equalTo(survey.getInstrument()));
		//when(survey.getInstrument()).thenReturn(inst);
		
		Survey testSurvey = new Survey();
		Instrument testInstrument = new Instrument();
		testSurvey.setInstrument(testInstrument);
		testSurvey.save();
		assertThat(testSurvey.getInstrument(), equalTo(testInstrument));
	}
	
	@Test
	public void shouldReturnListOfResponses() throws Exception {
		//TODO Implement
		//assertThat(survey.responses(), instanceOf(List.class));
	}
	
	@Test
	public void shouldGetResponseByQuestion() throws Exception {
		//TODO Implement
	}
	
	@Test
	public void shouldMakeJsonObject() throws Exception {
		//TODO Implement
	}
}
