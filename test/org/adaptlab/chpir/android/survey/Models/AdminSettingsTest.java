package org.adaptlab.chpir.android.survey.Models;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.instanceOf;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Option.class, Question.class })
public class AdminSettingsTest {
	
	private static final String DEVICE_ID = "device id";
	private static final int SYNC_INTERVAL = 1;
	
	private AdminSettings adminSettings;
	
	@Before
	public void setUp() throws Exception {
		adminSettings = mock(AdminSettings.class);
	}
	
	@Test	//TODO Fix ActiveAndroid error
	public void shouldGetAdminSettingsInstance() throws Exception {
		assertNotNull(AdminSettings.getInstance());
		assertThat(AdminSettings.getInstance(), instanceOf(AdminSettings.class));
	}

	@Test
	public void shouldSetAndGetDeviceIdentifier() throws Exception {
		when(adminSettings.getDeviceIdentifier()).thenReturn(DEVICE_ID);
		adminSettings.setDeviceIdentifier(DEVICE_ID);
		verify(adminSettings, times(1)).setDeviceIdentifier(DEVICE_ID);
		assertEquals(DEVICE_ID, adminSettings.getDeviceIdentifier());
	}
	
	@Test
	public void shouldSetAndGetSyncInternval() throws Exception {
		when(adminSettings.getSyncInterval()).thenReturn(SYNC_INTERVAL);
		adminSettings.setSyncInterval(SYNC_INTERVAL);
		verify(adminSettings, times(1)).setSyncInterval(SYNC_INTERVAL);
		assertEquals(SYNC_INTERVAL, adminSettings.getSyncInterval());
		assertEquals(SYNC_INTERVAL, adminSettings.getSyncIntervalInMinutes());
	}
}
