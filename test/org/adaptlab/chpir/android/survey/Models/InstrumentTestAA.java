package org.adaptlab.chpir.android.survey.Models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InstrumentTestAA extends ActiveAndroidTestBase {

	private Instrument instrument;
	
	@Override
	@Before
	public void onSetup() {
		instrument = mock(Instrument.class);
	}
	
	@Test
	public void shouldReturnAllInstruments() throws Exception {
		Instrument inst = mock(Instrument.class);
		inst.save();
		assertEquals(Instrument.getAll().size(), 2);
		// when(Instrument.getAll().size()).thenReturn(2);
	}
}
