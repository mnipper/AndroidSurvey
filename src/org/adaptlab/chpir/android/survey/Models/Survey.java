package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.SendTable;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Surveys")
public class Survey extends Model implements SendTable {
    private static final String TAG = "Survey";

    @Column(name = "Instrument")
    private Instrument mInstrument;

    public Survey() {
        super();
    }

    public Instrument getInstrument() {
        return mInstrument;
    }

    public void setInstrument(Instrument instrument) {
        mInstrument = instrument;
    }

    public List<Response> responses() {
        return getMany(Response.class, "Survey");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("instrument_id", getInstrument().getId());
        } catch (JSONException je) {
            Log.e(TAG, "JSON exception", je);
        }
        return jsonObject;
    }
}
