package org.adaptlab.chpir.android.survey.Models;

import java.util.List;
import java.util.Locale;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
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

    public Option() {
        super();
    }

    public Question getQuestion() {
        return mQuestion;
    }

    public void setQuestion(Question question) {
        mQuestion = question;
    }

    public String getText() {
        if (getQuestion().getInstrument().getLanguage().equals(Locale.getDefault().getLanguage())) return mText;
        for(OptionTranslation translation : translations()) {
            if (translation.getLanguage().equals(Locale.getDefault().getLanguage())) {
                return translation.getText();
            }
        }
        
        // Fall back to default
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
    
    public static List<Option> getAll() {
        return new Select().from(Option.class).orderBy("Id ASC").execute();
    }
    
    public Long getRemoteId() {
        return mRemoteId;
    }
    
    public void setRemoteId(Long id) {
        mRemoteId = id;
    }
    
    public Question getNextQuestion() {
        return Question.findByQuestionIdentifier(mNextQuestion);
    }
    
    private void setNextQuestion(String nextQuestion) {
        mNextQuestion = nextQuestion;
    }
    
    public static Option findByRemoteId(Long id) {
        return new Select().from(Option.class).where("RemoteId = ?", id).executeSingle();
    }
    
    public List<OptionTranslation> translations() {
        return getMany(OptionTranslation.class, "Option");
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
            
            Log.i(TAG, "Creating object from JSON Object: " + jsonObject);
            option.setText(jsonObject.getString("text"));
            option.setQuestion(Question.findByRemoteId(jsonObject.getLong("question_id")));
            option.setRemoteId(remoteId);
            option.setNextQuestion(jsonObject.getString("next_question"));
            option.save();
            
            // Generate translations
            JSONArray translationsArray = jsonObject.getJSONArray("translations");
            for(int i = 0; i < translationsArray.length(); i++) {
                JSONObject translationJSON = translationsArray.getJSONObject(i);
                OptionTranslation translation = new OptionTranslation();
                translation.setOption(this);
                translation.setLanguage(translationJSON.getString("language"));
                translation.setText(translationJSON.getString("text"));
                translation.save();
            }
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }   
    }
}
