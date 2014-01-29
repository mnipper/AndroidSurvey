package org.adaptlab.chpir.android.survey.Tasks;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;

import android.os.AsyncTask;

public class SendResponsesTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        ActiveRecordCloudSync.syncSendTables();
        return null;
    }      
}
