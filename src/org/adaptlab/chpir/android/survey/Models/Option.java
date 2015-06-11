package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.adaptlab.chpir.android.survey.AppUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Options")
public class Option extends ReceiveModel {
    private static final String TAG = "Option";

    @Column(name = "Question")
    private Question mQuestion;
    @Column(name = "Text")
    private String mText;
    // https://github.com/pardom/ActiveAndroid/issues/22
    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;
    @Column(name = "NextQuestion")
    private String mNextQuestion;
    @Column(name = "NumberInQuestion")
    private int mNumberInQuestion;
    @Column(name = "InstrumentVersion")
    private int mInstrumentVersion;
    @Column(name = "Deleted")
    private boolean mDeleted;

    public Option() {
        super();
    }
    
    /*
     * If the language of the instrument is the same as the language setting on the
     * device (or through the admin settings), then return the default option text.
     * 
     * If another language is requested, iterate through option translations to
     * find translated text.
     * 
     * If the language requested is not available as a translation, return the non-translated
     * text for the option.
     */
    public String getText() {
        if (getQuestion().getInstrument().getLanguage().equals(getDeviceLanguage())) return mText;
        for(OptionTranslation translation : translations()) {
            if (translation.getLanguage().equals(getDeviceLanguage())) {
                return translation.getText();
            }
        }
        
        // Fall back to default
        return mText;
    }

	public String getDeviceLanguage() {
		return Instrument.getDeviceLanguage();
	}
    
    
    /*
     * Find an exisiting translation, or return a new OptionTranslation
     * if a translation does not yet exist.
     */ 
    public OptionTranslation getTranslationByLanguage(String language) {
        for(OptionTranslation translation : translations()) {
            if (translation.getLanguage().equals(language)) {
                return translation;
            }
        }
        
        OptionTranslation translation = new OptionTranslation();
        translation.setLanguage(language);
        return translation;
    }
    

    @Override
    public void createObjectFromJSON(JSONObject jsonObject) {
        try {
            Long remoteId = jsonObject.getLong("id");
            
            // If an option already exists, update it from the remote
            Option option = Option.findByRemoteId(remoteId);
            if (option == null) {
                option = this;
            }
            
            if (AppUtil.DEBUG) Log.i(TAG, "Creating object from JSON Object: " + jsonObject);
            option.setText(jsonObject.getString("text"));
            option.setQuestion(Question.findByRemoteId(jsonObject.getLong("question_id")));
            option.setRemoteId(remoteId);
            option.setNextQuestion(jsonObject.getString("next_question"));
            if (!jsonObject.isNull("number_in_question")) {
            	option.setNumberInQuestion(jsonObject.getInt("number_in_question"));
            }
            option.setInstrumentVersion(jsonObject.getInt("instrument_version"));
            if (!jsonObject.isNull("deleted_at")) {
            	option.setDeleted(true);
            }
            option.save();
            
            // Generate translations
            JSONArray translationsArray = jsonObject.getJSONArray("translations");
            for(int i = 0; i < translationsArray.length(); i++) {
                JSONObject translationJSON = translationsArray.getJSONObject(i);
                OptionTranslation translation = option.getTranslationByLanguage(translationJSON.getString("language"));
                translation.setOption(option);
                translation.setText(translationJSON.getString("text"));
                translation.save();
            }
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }   
    }    
    
    // Used for skip patterns
    public Question getNextQuestion() {
        return findByQuestionIdentifier(mNextQuestion);
    }

	public Question findByQuestionIdentifier(String question) {
		return Question.findByQuestionIdentifier(question);
	}
    
    private void setNextQuestion(String nextQuestion) {
        mNextQuestion = nextQuestion;
    }
    
    /*
     * Finders
     */
    public static List<Option> getAll() {
        return new Select().from(Option.class).where("Deleted != ?", 1).orderBy("Id ASC").execute();
    }
    
    public static Option findByRemoteId(Long id) {
        return new Select().from(Option.class).where("RemoteId = ?", id).executeSingle();
    }
      
    /*
     * Relationships
     */ 
    public List<OptionTranslation> translations() {
        return getMany(OptionTranslation.class, "Option");
    }
    
    public List<Skip> skips() {
    	return getMany(Skip.class, "Option");
    }
    
    public List<Question> questionsToSkip() {
    	return new Select("Questions.*").from(Question.class).innerJoin(Skip.class).on("Questions.Id = Skips.Question AND Skips.Option =?", getId()).execute();
    }
    
    /*
     * Getters/Setters
     */
    public Question getQuestion() {
        return mQuestion;
    }

    public void setQuestion(Question question) {
        mQuestion = question;
    }

    public void setText(String text) {
        mText = text;
    }
    
    public Long getRemoteId() {
        return mRemoteId;
    }
    
    public void setRemoteId(Long id) {
        mRemoteId = id;
    }
    
    public int getNumberInQuestion() {
        return mNumberInQuestion;
    }
    
    public int getInstrumentVersion() {
        return mInstrumentVersion;
    }
    
    private void setNumberInQuestion(int number) {
        mNumberInQuestion = number;
    }
    
    private void setInstrumentVersion(int version) {
        mInstrumentVersion = version;
    }
    
    private void setDeleted(boolean deleted) {
    	mDeleted = deleted;
    }
 
}
