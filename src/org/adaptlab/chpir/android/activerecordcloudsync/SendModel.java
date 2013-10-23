package org.adaptlab.chpir.android.activerecordcloudsync;

import org.json.JSONObject;

import com.activeandroid.Model;

public abstract class SendModel extends Model { 
    public abstract JSONObject toJSON();
    public abstract boolean isSent();
    public abstract void setAsSent();
}
