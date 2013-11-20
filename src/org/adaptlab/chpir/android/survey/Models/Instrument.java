package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Instruments")
public class Instrument extends ReceiveModel {
    private static final String TAG = "Instrument";

    @Column(name = "Title")
    private String mTitle;
    // https://github.com/pardom/ActiveAndroid/issues/22
    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;

    public Instrument() {
        super();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
    
    public Long getRemoteId() {
        return mRemoteId;
    }
    
    public void setRemoteId(Long id) {
        mRemoteId = id;
    }

    public List<Question> questions() {
        return getMany(Question.class, "Instrument");
    }

    public static List<Instrument> getAll() {
        return new Select().from(Instrument.class).orderBy("Id ASC").execute();
    }

    @Override
    public String toString() {
        return mTitle;
    }
    
    public static Instrument findByRemoteId(Long id) {
        return new Select().from(Instrument.class).where("RemoteId = ?", id).executeSingle();
    }
    
    public List<Survey> surveys() {
        return getMany(Survey.class, "Instrument");
    }

    @Override
    public void createObjectFromJSON(JSONObject jsonObject) {
        try {
            Long remoteId = jsonObject.getLong("id");
                        
            // If an instrument already exists, update it from the remote
            Instrument instrument = Instrument.findByRemoteId(remoteId);
            if (instrument == null) {
                instrument = this;
            }
            
            Log.i(TAG, "Creating object from JSON Object: " + jsonObject);
            instrument.setRemoteId(remoteId);
            instrument.setTitle(jsonObject.getString("title"));
            instrument.save();
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }  
    }
}
