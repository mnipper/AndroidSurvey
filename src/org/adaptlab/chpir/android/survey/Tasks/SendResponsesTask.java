package org.adaptlab.chpir.android.survey.Tasks;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.NetworkNotificationUtils;

import android.content.Context;
import android.os.AsyncTask;

public class SendResponsesTask extends AsyncTask<Void, Void, Void> {
    
    Context mContext;
    
    public SendResponsesTask (Context context){
        mContext = context;
    }
    
    @Override
    protected Void doInBackground(Void... params) {
        if (NetworkNotificationUtils.checkForNetworkErrors(mContext)) {
            ActiveRecordCloudSync.syncSendTables(mContext);
        }
        
        return null;
    }      
}
