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

@Table(name = "Questions")
public class Question extends ReceiveModel {

    private static final String TAG = "QuestionModel";
    public static final String FOLLOW_UP_TRIGGER_STRING = "\\[followup\\]";

    public static enum QuestionType {
        SELECT_ONE, SELECT_MULTIPLE, SELECT_ONE_WRITE_OTHER,
        SELECT_MULTIPLE_WRITE_OTHER, FREE_RESPONSE, SLIDER,
        FRONT_PICTURE, REAR_PICTURE, DATE, RATING;
    }

    @Column(name = "Text")
    private String mText;
    @Column(name = "QuestionType")
    private QuestionType mQuestionType;
    @Column(name = "QuestionIdentifier")
    private String mQuestionIdentifier;
    @Column(name = "Instrument")
    private Instrument mInstrument;
    @Column(name = "FollowingUpQuestion")
    private Question mFollowingUpQuestion;
    // https://github.com/pardom/ActiveAndroid/issues/22
    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;

    public Question() {
        super();
    }

    public String getText() {
        if (getInstrument().getLanguage().equals(Instrument.getDeviceLanguage())) return mText;
        for(QuestionTranslation translation : translations()) {
            if (translation.getLanguage().equals(Instrument.getDeviceLanguage())) {
                return translation.getText();
            }
        }
        
        // Fall back to default
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public QuestionType getQuestionType() {
        return mQuestionType;
    }

    public void setQuestionType(String questionType) {
        if (validQuestionType(questionType)) {
            mQuestionType = QuestionType.valueOf(questionType);
        } else {
            // This should never happen
            // We should prevent syncing data unless app is up to date
            Log.wtf(TAG, "Received invalid question type: " + questionType);
        }
    }

    public String getQuestionIdentifier() {
        return mQuestionIdentifier;
    }

    public void setQuestionIdentifier(String questionIdentifier) {
        mQuestionIdentifier = questionIdentifier;
    }

    public Instrument getInstrument() {
        return mInstrument;
    }

    public void setInstrument(Instrument instrument) {
        mInstrument = instrument;
    }
    
    public Question getFollowingUpQuestion() {
        return mFollowingUpQuestion;
    }
    
    public void setFollowingUpQuestion(Question question) {
        mFollowingUpQuestion = question;
    }

    public boolean hasOptions() {
        return !options().isEmpty();
    }

    public List<Option> options() {
        return getMany(Option.class, "Question");
    }
    
    public String getOptionTextByResponse(Response response) {
        String text = response.getText();
        try {
            if (Integer.parseInt(text) == options().size()) {
                return response.getOtherResponse();
            } else {
                return options().get(Integer.parseInt(text)).getText();
            }
        } catch (NumberFormatException nfe) {
            Log.e(TAG, text + " is not an option number");
            return text;
        } catch (IndexOutOfBoundsException iob) {
            Log.e(TAG, text + " is an out of range option number");
            return text;
        }
    }
    
    public boolean hasSkipPattern() {
        for (Option option : options()) {
            if (option.getNextQuestion() != null && 
                    !option.getNextQuestion().getQuestionIdentifier().equals("")) {
                return true;
            }
        }
        return false;
    }
    
    public static List<Question> getAll() {
        return new Select().from(Question.class).orderBy("Id ASC").execute();
    }
    
    public List<QuestionTranslation> translations() {
        return getMany(QuestionTranslation.class, "Question");
    }

    private static boolean validQuestionType(String questionType) {
        for (QuestionType type : QuestionType.values()) {
            if (type.name().equals(questionType)) {
                return true;
            }
        }
        return false;
    }
    
    public Long getRemoteId() {
        return mRemoteId;
    }
    
    public void setRemoteId(Long id) {
        mRemoteId = id;
    }
    
    public static Question findByRemoteId(Long id) {
        return new Select().from(Question.class).where("RemoteId = ?", id).executeSingle();
    }
    
    public static Question findByQuestionIdentifier(String identifier) {
        return new Select().from(Question.class).where("QuestionIdentifier = ?", identifier).executeSingle();
    }
    
    public String getFollowingUpText(Survey survey) {
        Response followUpResponse = survey.getResponseByQuestion(getFollowingUpQuestion());
        if (getFollowingUpQuestion().getQuestionType().equals(QuestionType.FREE_RESPONSE) ||
                getFollowingUpQuestion().getQuestionType().equals(QuestionType.DATE)) {
            return getText().replaceAll(FOLLOW_UP_TRIGGER_STRING, followUpResponse.getText());
        } else {
            return getText().replaceAll(
                    FOLLOW_UP_TRIGGER_STRING,
                    getFollowingUpQuestion().getOptionTextByResponse(followUpResponse)               
            );
        }
    }
    
    
    public QuestionTranslation getTranslationByLanguage(String language) {
        for(QuestionTranslation translation : translations()) {
            if (translation.getLanguage().equals(language)) {
                return translation;
            }
        }
        QuestionTranslation translation = new QuestionTranslation();
        translation.setLanguage(language);
        return translation;
    }

    @Override
    public void createObjectFromJSON(JSONObject jsonObject) {
        try {
            Long remoteId = jsonObject.getLong("id");
            
            // If a question already exists, update it from the remote
            Question question = Question.findByRemoteId(remoteId);
            if (question == null) {
                question = this;
            }
            
            Log.i(TAG, "Creating object from JSON Object: " + jsonObject);
            question.setText(jsonObject.getString("text"));
            question.setQuestionType(jsonObject.getString("question_type"));
            question.setQuestionIdentifier(jsonObject.getString("question_identifier"));            
            question.setInstrument(Instrument.findByRemoteId(jsonObject.getLong("instrument_id")));
            question.setFollowingUpQuestion(Question.findByQuestionIdentifier(
                    jsonObject.getString("following_up_question_identifier")
                )
            );
            question.setRemoteId(remoteId);
            question.save();
            
            // Generate translations
            JSONArray translationsArray = jsonObject.getJSONArray("translations");
            for(int i = 0; i < translationsArray.length(); i++) {
                JSONObject translationJSON = translationsArray.getJSONObject(i);
                QuestionTranslation translation = question.getTranslationByLanguage(translationJSON.getString("language"));
                translation.setQuestion(question);
                translation.setText(translationJSON.getString("text"));
                translation.save();
            }
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        } 
    }
}
