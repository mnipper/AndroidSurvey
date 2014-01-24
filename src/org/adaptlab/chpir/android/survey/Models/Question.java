package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.adaptlab.chpir.android.survey.FormatUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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
        FRONT_PICTURE, REAR_PICTURE, DATE, RATING, TIME;
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
    @Column(name = "RegExValidation")
    private String mRegExValidation;
    @Column(name = "RegExValidationMessage")
    private String mRegExValidationMessage;
    @Column(name = "OptionCount")
    private int mOptionCount;
    @Column(name = "InstrumentVersion")
    private int mInstrumentVersion;
    @Column(name = "NumberInInstrument")
    private int mNumberInInstrument;
    // https://github.com/pardom/ActiveAndroid/issues/22
    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;

    public Question() {
        super();
    }

    /*
     * If the language of the instrument is the same as the language setting on the
     * device (or through the admin settings), then return the question text.
     * 
     * If another language is requested, iterate through question translations to
     * find translated text.
     * 
     * If the language requested is not available as a translation, return the non-translated
     * text for the question.
     */
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
    
    
    public String getRegExValidationMessage() {
        if (getInstrument().getLanguage().equals(Instrument.getDeviceLanguage())) return mRegExValidationMessage;
        for(QuestionTranslation translation : translations()) {
            if (translation.getLanguage().equals(Instrument.getDeviceLanguage())) {
                return translation.getRegExValidationMessage();
            }
        }
        
        // Fall back to default
        return mRegExValidationMessage;
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
 
    /*
     * Map a response represented as an index to its corresponding
     * option text.  If this is an "other" response, return the
     * text specified in the other response.
     */
    public String getOptionTextByResponse(Response response, Context context) {
        String text = response.getText();
        
        try {
            if (hasMultipleResponses()) {
                return FormatUtils.unformatMultipleResponses(options(), text, context);
            } else if (Integer.parseInt(text) == options().size()) {
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
    
    /*
     * Return the processed string for a following up question.
     * 
     * Replace the follow up trigger string token with the appropriate
     * response.  If this is a question with options, then map the option
     * number to the option text.  If not, then return the text response.
     * 
     * If the question that is being followed up on was skipped by the user,
     * then return nothing.  This question will be skipped in that case.
     */
    public String getFollowingUpText(Survey survey, Context context) {
        Response followUpResponse = survey.getResponseByQuestion(getFollowingUpQuestion());
        if (followUpResponse == null || followUpResponse.getSpecialResponse().equals(Response.SKIPPED)) return null;
        
        if (followUpWithOptionText()) {
            return getText().replaceAll(
                    FOLLOW_UP_TRIGGER_STRING,
                    getFollowingUpQuestion().getOptionTextByResponse(followUpResponse, context)               
            );
        } else {
            return getText().replaceAll(FOLLOW_UP_TRIGGER_STRING, followUpResponse.getText());
        }
    }
    
    /*
     * Question types which must have their responses (represented as indices)
     * mapped to the original option text.
     */
    public boolean followUpWithOptionText() {
        return getFollowingUpQuestion().getQuestionType().equals(QuestionType.SELECT_MULTIPLE) ||
                getFollowingUpQuestion().getQuestionType().equals(QuestionType.SELECT_ONE) ||
                getFollowingUpQuestion().getQuestionType().equals(QuestionType.SELECT_ONE_WRITE_OTHER) ||
                getFollowingUpQuestion().getQuestionType().equals(QuestionType.SELECT_MULTIPLE_WRITE_OTHER);
    }
    
    /*
     * Return true if this response can be an array of multiple options.
     */
    public boolean hasMultipleResponses() {
        return getQuestionType().equals(QuestionType.SELECT_MULTIPLE) ||
                getQuestionType().equals(QuestionType.SELECT_MULTIPLE_WRITE_OTHER);
    }
    
    /*
     * Find an exisiting translation, or return a new QuestionTranslation
     * if a translation does not yet exist.
     */
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
    
    /*
     * Check that all of the options are loaded and that the instrument version
     * numbers of the question components match the expected instrument version
     * number.
     */
    public boolean loaded() { 
        return getOptionCount() == options().size() &&
                getInstrumentVersion() == getInstrument().getVersionNumber();
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
            question.setRegExValidation(jsonObject.getString("reg_ex_validation"));
            question.setRegExValidationMessage(jsonObject.getString("reg_ex_validation_message"));
            question.setOptionCount(jsonObject.getInt("option_count"));
            question.setInstrumentVersion(jsonObject.getInt("instrument_version"));
            question.setNumberInInstrument(jsonObject.getInt("number_in_instrument"));
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
                translation.setRegExValidationMessage(translationJSON.getString("reg_ex_validation_message"));
                translation.save();
            }
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        } 
    }

    /*
     * Finders
     */   
    public static List<Question> getAll() {
        return new Select().from(Question.class).orderBy("Id ASC").execute();
    }
    
    public static Question findByRemoteId(Long id) {
        return new Select().from(Question.class).where("RemoteId = ?", id).executeSingle();
    }
    
    public static Question findByQuestionIdentifier(String identifier) {
        return new Select().from(Question.class).where("QuestionIdentifier = ?", identifier).executeSingle();
    }
    
    public boolean isFollowUpQuestion() {
        return (getFollowingUpQuestion() != null);
    }
    
    /*
     * Relationships
     */
    public boolean hasOptions() {
        return !options().isEmpty();
    }

    public List<Option> options() {
        return new Select().from(Option.class)
                .where("Question = ?", getId())
                .orderBy("NumberInQuestion ASC")
                .execute();
    }
    
    public List<QuestionTranslation> translations() {
        return getMany(QuestionTranslation.class, "Question");
    }

    
    /*
     * Getters/Setters
     */
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
    
    public void setRegExValidation(String validation) {
        mRegExValidation = validation;
    }
    
    public String getRegExValidation() {
        if (mRegExValidation.equals("") || mRegExValidation.equals("null"))
            return null;
        else
            return mRegExValidation;
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
  
    public Long getRemoteId() {
        return mRemoteId;
    }
    
    public void setRemoteId(Long id) {
        mRemoteId = id;
    }
    
    public int getInstrumentVersion() {
        return mInstrumentVersion;
    }
    
    public int getNumberInInstrument() {
        return mNumberInInstrument;
    }
    
    /*
     * Private
     */
    private static boolean validQuestionType(String questionType) {
        for (QuestionType type : QuestionType.values()) {
            if (type.name().equals(questionType)) {
                return true;
            }
        }
        return false;
    }
    
    private void setOptionCount(int num) {
        mOptionCount = num;
    }
    
    private int getOptionCount() {
        return mOptionCount;
    }
    
    private void setInstrumentVersion(int version) {
        mInstrumentVersion = version;
    }
    
    private void setNumberInInstrument(int number) {
        mNumberInInstrument = number;
    }
    
    private void setRegExValidationMessage(String message) {
        if (message.equals("null") || message.equals(""))
           mRegExValidationMessage = null;
        else
            mRegExValidationMessage = message;
    }
}
