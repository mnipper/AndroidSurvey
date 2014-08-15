package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.adaptlab.chpir.android.survey.AppUtil;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Images")
public class Image extends ReceiveModel {
    private static final String TAG = "Image";
    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;
    @Column(name = "PhotoUrl")
    private String mPhotoUrl; 
    @Column(name = "Question")
    private Question mQuestion;
    @Column(name = "BitmapPath")
    private String mBitmapPath;
    
    public Image() {
    	super();
    }
    
    public static List<Image> getAll() {
    	return new Select().from(Image.class).orderBy("Id ASC").execute(); 
    }

	@Override
	public void createObjectFromJSON(JSONObject jsonObject) {
	    if (AppUtil.DEBUG) Log.i(TAG, jsonObject.toString());
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
            if (AppUtil.DEBUG) Log.i(TAG, image.getPhotoUrl());
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
	
	public void setBitmapPath(String imagePath) {
		mBitmapPath = imagePath;
	}
	
	public String getBitmapPath() {
		return mBitmapPath;
	}

	private static Image findByRemoteId(Long remoteId) {
		return new Select().from(Image.class).where("RemoteId = ?", remoteId).executeSingle();
	}

}
