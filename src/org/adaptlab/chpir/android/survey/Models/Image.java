package org.adaptlab.chpir.android.survey.Models;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import android.util.Log;

@Table(name = "Images")
public class Image extends ReceiveModel {
    private static final String TAG = "Image";
    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;
    @Column(name = "PhotoUrl")
    private String mPhotoUrl; 
    @Column(name = "Question")
    private Question mQuestion;
    
    public Image() {
    	super();
    }

	@Override
	public void createObjectFromJSON(JSONObject jsonObject) {
			Log.e(TAG, jsonObject.toString());
		try {
            Long remoteId = jsonObject.getLong("id");
            Image image = Image.findByRemoteId(remoteId);
            if (image == null) {
            	image = this;
            }
            image.setRemoteId(remoteId);
            image.setQuestion(Question.findByRemoteId(jsonObject.getLong("question_id")));
            image.setPhotoUrl(jsonObject.getString("photo_url"));
            image.save();
		} catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }   
	}

	public void setPhotoUrl(String filename) {
		mPhotoUrl = filename;
	}
	
	public String getPhotoUrl() {
		return mPhotoUrl;
	}

	public void setRemoteId(Long remoteId) {
		mRemoteId = remoteId;
	}
	
	public Long getRemoteId() {
		return mRemoteId;
	}

	public void setQuestion(Question question) {
		mQuestion = question;
	}
	
	public Question getQuestion() {
		return mQuestion;
	}

	private static Image findByRemoteId(Long remoteId) {
		return new Select().from(Image.class).where("RemoteId = ?", remoteId).executeSingle();
	}

}
