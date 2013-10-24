package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
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
    
    public static Option findByRemoteId(Long id) {
        return new Select().from(Option.class).where("RemoteId = ?", id).executeSingle();
    }

    @Override
    public void createObjectFromJSON(JSONObject jsonObject) {
        try {
            Long remoteId = jsonObject.getLong("id");
            if (Option.findByRemoteId(remoteId) == null) {
                Log.i(TAG, "Creating object from JSON Object: " + jsonObject);
                setText(jsonObject.getString("text"));
                setQuestion(Question.findByRemoteId(jsonObject.getLong("question_id")));
                setRemoteId(remoteId);
                this.save();
            }
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }   
    }
}
