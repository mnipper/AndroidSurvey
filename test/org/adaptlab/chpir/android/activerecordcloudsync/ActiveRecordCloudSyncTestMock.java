package org.adaptlab.chpir.android.activerecordcloudsync;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {ActiveRecordCloudSync.class})
public class ActiveRecordCloudSyncTestMock {
	
	@Test
	public void shouldSyncReceiveTables() throws Exception {
		// TODO Implement
		//HttpFetchr httpFetchr = mock(HttpFetchr.class);
		//ActiveRecordCloudSync sync = mock(ActiveRecordCloudSync.class);
		PowerMockito.mockStatic(ActiveRecordCloudSync.class);
		//expect(ActiveRecordCloudSync.syncReceiveTables());
		
		//when(sync.syncReceiveTables()).thenReturn(httpFetchr.fetch());
	}

}
