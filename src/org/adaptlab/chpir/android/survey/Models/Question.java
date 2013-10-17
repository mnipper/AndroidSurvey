package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveTable;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Questions")
public class Question extends Model implements ReceiveTable {

    private static final String TAG = "QuestionModel";

    public static enum QuestionType {
        SELECT_ONE, SELECT_MULTIPLE, SELECT_ONE_WRITE_OTHER,
        SELECT_MULTIPLE_WRITE_OTHER, FREE_RESPONSE, SLIDER,
        FRONT_PICTURE, REAR_PICTURE;
    }

    @Column(name = "Text")
    private String mText;
    @Column(name = "QuestionType")
    private QuestionType mQuestionType;
    @Column(name = "QuestionID")
    private String mQuestionID;
    @Column(name = "Instrument")
    private Instrument mInstrument;

    public Question() {
        super();
        ActiveRecordCloudSync.addReceiveTable(Question.class);
    }

    public String getText() {
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
            Log.e(TAG, "Received invalid question type: " + questionType);
        }
    }

    public String getQuestionID() {
        return mQuestionID;
    }

    public void setQuestionID(String questionID) {
        mQuestionID = questionID;
    }

    public Instrument getInstrument() {
        return mInstrument;
    }

    public void setInstrument(Instrument instrument) {
        mInstrument = instrument;
    }

    public boolean hasOptions() {
        return !options().isEmpty();
    }

    public List<Option> options() {
        return getMany(Option.class, "Question");
    }
    
    public static List<Question> getAll() {
        return new Select().from(Question.class).orderBy("Id ASC").execute();
    }

    @Override
    public Long lastId() {
        return getAll().get(getAll().size() - 1).getId();
    }

    private static boolean validQuestionType(String questionType) {
        for (QuestionType type : QuestionType.values()) {
            if (type.name().equals(questionType))
                return true;
        }
        return false;
    }

    @Override
    public void createObjectFromJSON(JSONObject jsonObject) {
        // TODO Auto-generated method stub
        
    }
}
