package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveTable;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Options")
public class Option extends Model implements ReceiveTable {
    private static final String TAG = "Option";

    @Column(name = "Question")
    private Question mQuestion;
    @Column(name = "Text")
    private String mText;
    // https://github.com/pardom/ActiveAndroid/issues/22
    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;

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

    @Override
    public void createObjectFromJSON(JSONObject jsonObject) {
        try {
            setText(jsonObject.getString("text"));
            Long questionId = jsonObject.getLong("question_id");
            setQuestion(Question.findByRemoteId(questionId));
            setRemoteId(jsonObject.getLong("id"));
            this.save();
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }   
    }
}
