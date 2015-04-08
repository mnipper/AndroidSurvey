package org.adaptlab.chpir.android.activerecordcloudsync;

import org.json.JSONObject;

import android.content.Context;

import com.activeandroid.Model;

public abstract class SendModel extends Model { 
    public abstract JSONObject toJSON();
    public abstract boolean isSent();
    public abstract boolean readyToSend();
    public abstract void setAsSent(Context context);
    public abstract boolean isPersistent();
}