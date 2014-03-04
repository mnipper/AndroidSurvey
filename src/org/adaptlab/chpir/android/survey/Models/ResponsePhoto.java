package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.SendModel;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "ResponsePhotos")
public class ResponsePhoto extends SendModel {
	private static final String TAG = "ResponsePhoto";
	@Column(name = "SentToRemote")
	private boolean mSent;
	@Column(name = "Response")
	private Response mResponse;
	@Column(name = "PicturePath")
	private String mPicturePath;
	@Column(name = "Question")
	private Question mQuestion;
		
	public ResponsePhoto() {
		super();
		mSent = false;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
        
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response_uuid", getResponse().getUUID());
            jsonObject.put("picture", getPicture());
           
            json.put("response_image", jsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "JSON exception", je);
        }
        return json;
	}
	
	public Bitmap getPicture() {
		return BitmapFactory.decodeFile(getPicturePath());
	}

	@Override
	public boolean isSent() {
		return mSent;
	}

	@Override
	public boolean readyToSend() {
		return getResponse().getSurvey().readyToSend();
	}
	
	public void setQuestion(Question question) {
		mQuestion = question;
	}
	
	public Question getQuestion() {
		return mQuestion;
	}
	
	public void setResponse(Response response) {
		mResponse = response;
	}
	
	public Response getResponse() {
		return mResponse;
	}
	
	public void setPicturePath(String path) {
		mPicturePath = path;
	}
	
	public String getPicturePath() {
		return mPicturePath;
	}

	@Override
	public void setAsSent() {  
		mSent = true;
        this.delete();
        if (getResponse().getSurvey().responsePhotos().size() == 0) { 
            getResponse().getSurvey().delete();
        }
	}
	
	public static List<Response> getAll() {
        return new Select().from(ResponsePhoto.class).orderBy("Id ASC").execute();
    }

}
