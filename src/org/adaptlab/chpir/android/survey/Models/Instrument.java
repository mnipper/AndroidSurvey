package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveTable;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Instruments")
public class Instrument extends Model implements ReceiveTable {
    private static final String TAG = "Instrument";

    @Column(name = "Title")
    private String mTitle;

    public Instrument() {
        super();
        ActiveRecordCloudSync.addReceiveTable("instruments", Instrument.class);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
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

    @Override
    public void createObjectFromJSON(JSONObject jsonObject) {
        try {
            setTitle(jsonObject.getString("title"));
            this.save();
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }
    }
}
