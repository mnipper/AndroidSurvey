package org.adaptlab.chpir.android.activerecordcloudsync;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ActiveRecordCloudSyncTest {

	@Before
	public void setUp() {
	}

	@Test
	public void shouldIncrementSizeOfReceiveTables() throws Exception {
		int count = ActiveRecordCloudSync.getReceiveTables().size();
		ActiveRecordCloudSync.addReceiveTable("myInstrument", Instrument.class);
		assertThat(ActiveRecordCloudSync.getReceiveTables().size(),
				equalTo(count + 1));
	}

	@Test
	public void shouldIncrementSizeOfSendTables() throws Exception {
		// TODO Implement
	}

	@Test
	public void shouldCheckIfPointsAreSetCorrectly() throws Exception {
		String endPoint1 = "endPoint";
		ActiveRecordCloudSync.setEndPoint(endPoint1);
		String endPoint2 = ActiveRecordCloudSync.getEndPoint();
		assertThat(endPoint1, equalTo(endPoint2));
	}

	@Test
	public void shouldReturnApiAvailability() {
		// TODO Test for when Api is available
		assertThat(false, equalTo(ActiveRecordCloudSync.isApiAvailable()));
	}

	@Test
	public void shouldSyncReceiveTables() throws Exception {
		// TODO Implement
	}

	@Test
	public void shouldSyncSendTables() throws Exception {
		// TODO Implement
	}
}
