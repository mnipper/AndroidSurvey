package org.adaptlab.chpir.android.survey;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.util.Log;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ Log.class, BuildConfig.class, DatabaseSeed.class })
public class DatabaseSeedTest {
	
	private DatabaseSeed dbSeed;
	@Before
	public void onSetup() {
		dbSeed = new DatabaseSeed();
	}
	
	@Test
	public void shouldSeed() throws Exception {
		PowerMockito.mockStatic(Log.class);
		PowerMockito.mockStatic(DatabaseSeed.class);
		PowerMockito.when(DatabaseSeed.seedDatabase(Robolectric.application)).thenReturn(true);
		DatabaseSeed.seed(Robolectric.application);
		PowerMockito.verifyStatic(times(1));	
	}
	
	// TODO Test Seeding Instrument
	

}
