package org.adaptlab.chpir.android.survey.Models;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Skips")
public class Skip extends ReceiveModel {
	private static final String TAG = "Skip";
	@Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;
	@Column(name = "Option")
	private Option mOption;
	@Column(name = "Question")
	private Question mQuestion;
	
	@Override
	public void createObjectFromJSON(JSONObject jsonObject) {
		try {
            Long remoteId = jsonObject.getLong("id");
            Skip skip = Skip.findByRemoteId(remoteId);
            if (skip == null) {
            	skip = this;
            }
            skip.setRemoteId(remoteId);
            skip.setOption(Option.findByRemoteId(jsonObject.getLong("option_id")));
            skip.setQuestion(Question.findByQuestionIdentifier(jsonObject.getString("question_identifier")));
            if (jsonObject.isNull("deleted_at")) {
            	skip.save();
            } else {
            	Skip deletedSkip = Skip.findByRemoteId(remoteId);
            	if (deletedSkip != null) {
            		deletedSkip.delete();
            	}
            }
		} catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }  
	}

	private void setQuestion(Question question) {
		mQuestion = question;
	}

	private void setOption(Option option) {
		mOption = option;
	}

	private void setRemoteId(Long remoteId) {
		mRemoteId = remoteId;
	}

	public static Skip findByRemoteId(Long remoteId) {
		return new Select().from(Skip.class).where("RemoteId = ?", remoteId).executeSingle();
	}

}
