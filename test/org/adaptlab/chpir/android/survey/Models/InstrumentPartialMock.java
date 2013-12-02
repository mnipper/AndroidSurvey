package org.adaptlab.chpir.android.survey.Models;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Instrument.class) 
public class InstrumentPartialMock {
	private Instrument privateInstrument;
	
	@Before
	public void setUp() throws Exception {
		//privateInstrument = PowerMockito.createPartialMock(Instrument.class, "setLanguage");
	}
	
}
