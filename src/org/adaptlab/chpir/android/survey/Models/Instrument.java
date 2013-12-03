package org.adaptlab.chpir.android.survey.Models;

import java.util.List;
import java.util.Locale;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Instruments")
public class Instrument extends ReceiveModel {
    private static final String TAG = "Instrument";
    public static final String KHMER_LANGUAGE_CODE = "km";

    @Column(name = "Title")
    private String mTitle;
    // https://github.com/pardom/ActiveAndroid/issues/22
    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;
    @Column(name = "Language")
    private String mLanguage;
    @Column(name = "Alignment")
    private String mAlignment;
    @Column(name = "VersionNumber")
    private int mVersionNumber;

    public Instrument() {
        super();
    }

    public String getTitle() {
        if (getLanguage().equals(getDeviceLanguage())) return mTitle;
        for(InstrumentTranslation translation : translations()) {
            if (translation.getLanguage().equals(getDeviceLanguage())) {
                return translation.getTitle();
            }
        }
        
        // Fall back to default
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
    
    public String getLanguage() {
        return mLanguage;
    }
    
    private void setLanguage(String language) {
        mLanguage = language;
    }
    
    public String getAlignment() {
        if (getLanguage().equals(getDeviceLanguage())) return mAlignment;
        for(InstrumentTranslation translation : translations()) {
            if (translation.getLanguage().equals(getDeviceLanguage())) {
                return translation.getAlignment();
            }
        }

        // Fall back to default
        return mAlignment;
    }
    
    private void setAlignment(String alignment) {
        mAlignment = alignment;
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
    
    public List<InstrumentTranslation> translations() {
        return getMany(InstrumentTranslation.class, "Instrument");
    }
    
    private void setVersionNumber(int version) {
        mVersionNumber = version;
    }
    
    public int getVersionNumber() {
        return mVersionNumber;
    }

    public InstrumentTranslation getTranslationByLanguage(String language) {
        for(InstrumentTranslation translation : translations()) {
            if (translation.getLanguage().equals(language)) {
                return translation;
            }
        }
        InstrumentTranslation translation = new InstrumentTranslation();
        translation.setLanguage(language);
        return translation;
    }
    
    public Typeface getTypeFace(Context context) {
        if (getDeviceLanguage().equals(KHMER_LANGUAGE_CODE)) {
            return Typeface.createFromAsset(context.getAssets(), "fonts/khmerOS.ttf"); 
        } else {
            return Typeface.DEFAULT;
        }
    }
    
    public int getDefaultGravity() {
        if (getAlignment().equals("left")) {
            return Gravity.LEFT;
        } else {
            return Gravity.RIGHT;
        }
    }
    
    public static String getDeviceLanguage() {
        if (!AdminSettings.getInstance().getCustomLocaleCode().equals("")) {
            return AdminSettings.getInstance().getCustomLocaleCode();
        }
        return Locale.getDefault().getLanguage();
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
            instrument.setLanguage(jsonObject.getString("language"));
            instrument.setAlignment(jsonObject.getString("alignment"));
            instrument.setVersionNumber(jsonObject.getInt("current_version_number"));
            instrument.save();
            
            // Generate translations
            JSONArray translationsArray = jsonObject.getJSONArray("translations");
            for(int i = 0; i < translationsArray.length(); i++) {
                JSONObject translationJSON = translationsArray.getJSONObject(i);
                InstrumentTranslation translation = instrument.getTranslationByLanguage(translationJSON.getString("language"));
                translation.setInstrument(instrument);
                translation.setAlignment(translationJSON.getString("alignment"));
                translation.setTitle(translationJSON.getString("title"));
                translation.save();
            }
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }  
    }
}
