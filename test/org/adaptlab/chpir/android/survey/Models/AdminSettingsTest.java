package org.adaptlab.chpir.android.survey.Models;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.util.Log;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AdminSettings.class, Log.class })
public class AdminSettingsTest extends ActiveAndroidTestBase {

	private static final String API_URL = "www.did.test:3000";
	private static final String DEVICE_ID = "device id";
	private static final int SYNC_INTERVAL = 1;
	private static final String TABLE = "AdminSettings";
	private AdminSettings adminSettings;

	@Before
	@Override
	public void onSetup() {
		adminSettings = new AdminSettings();	// TODO Use getInstance() instead of new
		when(tableInfo.getTableName()).thenReturn(TABLE);
		PowerMockito.mockStatic(AdminSettings.class);
		PowerMockito.mockStatic(Log.class);
	}

	@Test
	public void shouldGetAdminSettingsInstance() throws Exception {
		assertNotNull(adminSettings);
		assertThat(adminSettings, instanceOf(AdminSettings.class));
	}

	@Test
	public void shouldSetAndGetApiUrl() throws Exception {
		adminSettings.setApiUrl(API_URL);	//TODO Fails because of Log - Need to import real implementation of Log
		assertEquals(API_URL, adminSettings.getApiUrl());
	}

	@Test
	public void shouldSetAndGetDeviceIdentifier() throws Exception {
		adminSettings.setDeviceIdentifier(DEVICE_ID);
		assertEquals(DEVICE_ID, adminSettings.getDeviceIdentifier());
	}

	@Test
	public void shouldSetAndGetSyncInternval() throws Exception {
		adminSettings.setSyncInterval(SYNC_INTERVAL);
		assertEquals(SYNC_INTERVAL * (60 * 1000), adminSettings.getSyncInterval());
		assertEquals(SYNC_INTERVAL, adminSettings.getSyncIntervalInMinutes());
	}
}
