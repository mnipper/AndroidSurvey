package org.adaptlab.chpir.android.survey.Models;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "ConsentTexts")
public class ConsentText extends ReceiveModel {
    private static final String TAG = "ConsentText";

	@Column(name = "Text")
    private String mText;
	@Column(name = "ProjectId")
    private Long mProjectId;
	@Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;
	
	@Override
	public void createObjectFromJSON(JSONObject jsonObject) {
		try {
			Long remoteId = jsonObject.getLong("id");
			ConsentText consent = ConsentText.findByRemoteId(remoteId);
			if (consent == null) {
				consent = this;
			}
			consent.setRemoteId(remoteId);
			consent.setProjectId(jsonObject.getLong("project_id"));
			consent.setText(jsonObject.getString("text"));
			consent.save();
			
		} catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }  
	}

	private void setRemoteId(Long remoteId) {
		mRemoteId = remoteId;
	}
	
	private void setProjectId(Long id) {
		mProjectId = id;
	}
	
	private void setText(String text) {
		mText = text;
	}

	public static ConsentText findByRemoteId(Long remoteId) {
		return new Select().from(ConsentText.class).where("RemoteId = ?", remoteId).executeSingle();
	}
	
	public static ConsentText findByProjectId(Long projectId) {
		return new Select().from(ConsentText.class).where("ProjectId =?", projectId).executeSingle();
	}
	
	public String getText() {
		return mText;
	}

}
