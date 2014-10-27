package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Sections")
public class Section extends ReceiveModel {

	private static final String TAG = "Section";
	
	@Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;
	@Column(name = "Instrument")
    private Instrument mInstrument;
	@Column(name = "Title")
    private String mTitle;
	@Column(name = "StartQuestionIdentifier")
    private String mStartQuestionIdentifier;

	@Override
	public void createObjectFromJSON(JSONObject jsonObject) {
		try {
            Long remoteId = jsonObject.getLong("id");
            Section section = Section.findByRemoteId(remoteId);
            if (section == null) {
            	section = this;
            }
            section.setRemoteId(remoteId);
            section.setInstrument(Instrument.findByRemoteId(jsonObject.getLong("instrument_id")));
            section.setTitle(jsonObject.getString("title"));
            section.setStartQuestionIdentifier(jsonObject.getString("start_question_identifier"));
            if (jsonObject.isNull("deleted_at")) {
            	section.save();
            } else {
            	Section deletedSection = Section.findByRemoteId(remoteId);
                if (deletedSection != null) {
                	deletedSection.delete();
                }
            }
            
            //Generate translations
            JSONArray translationsArray = jsonObject.getJSONArray("translations");
            for(int i = 0; i < translationsArray.length(); i++) {
                JSONObject translationJSON = translationsArray.getJSONObject(i);
                SectionTranslation translation = section.getTranslationByLanguage(translationJSON.getString("language"));
                translation.setSection(section);
                translation.setText(translationJSON.getString("text"));
                translation.save();
            }
            
		} catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }  
	}
	
	/*
     * Find an existing translation, or return a new SectionTranslation
     * if a translation does not yet exist.
     */
    public SectionTranslation getTranslationByLanguage(String language) {
        for(SectionTranslation translation : translations()) {
            if (translation.getLanguage().equals(language)) {
                return translation;
            }
        }
        
        SectionTranslation translation = new SectionTranslation();
        translation.setLanguage(language);
        return translation;
    }
    
    public List<SectionTranslation> translations() {
    	return getMany(SectionTranslation.class, "Section");
    }

	public void setInstrument(Instrument instrument) {
		mInstrument = instrument;
	}
	
	public Instrument getInstrument() {
		return mInstrument;
	}

	public void setRemoteId(Long remoteId) {
		mRemoteId = remoteId;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public void setStartQuestionIdentifier(String beginId) {
		mStartQuestionIdentifier = beginId;
	}
	
	public String getStartQuestionIdentifier() {
		return mStartQuestionIdentifier;
	}
	
	/*
     * If the language of the instrument is the same as the language setting on the
     * device (or through the Admin settings), then return the section title.
     * 
     * If another language is requested, iterate through section translations to
     * find translated title.
     * 
     * If the language requested is not available as a translation, return the non-translated
     * text for the section.
     */
	public String getTitle() {
		if (getInstrument().getLanguage().equals(Instrument.getDeviceLanguage())) return mTitle;
        for (SectionTranslation translation : translations()) {
            if (translation.getLanguage().equals(Instrument.getDeviceLanguage())) {
                return translation.getText();
            }
        }
		//Default
		return mTitle;
	}

	public static Section findByRemoteId(Long remoteId) {
		return new Select().from(Section.class).where("RemoteId = ?", remoteId).executeSingle();
	}
	
	public static List<Section> getAll() {
		return new Select().from(Section.class).orderBy("Id ASC").execute();
	}

}
