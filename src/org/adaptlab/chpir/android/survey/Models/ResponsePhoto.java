package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.SendModel;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "ResponsePhotos")
public class ResponsePhoto extends SendModel {
	private static final String TAG = "ResponsePhoto";
	@Column(name = "SentToRemote")
	private boolean mSent;
	@Column(name = "Survey")
	private Survey mSurvey;
	@Column(name = "Response")
	private Response mResponse;
	@Column(name = "Bitmap")
	private Bitmap mBitmap;
	
	//TODO Is mSurvey redundant?
	
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
            jsonObject.put("picture", getBitmap());
           
            json.put("response_image", jsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "JSON exception", je);
        }
        return json;
	}

	@Override
	public boolean isSent() {
		return mSent;
	}

	@Override
	public boolean readyToSend() {
		return getSurvey().readyToSend();
	}
	
	public void setSurvey(Survey survey) {
		mSurvey = survey;
	}

	public Survey getSurvey() {
		return mSurvey;
	}
	
	public void setResponse(Response response) {
		mResponse = response;
	}
	
	public Response getResponse() {
		return mResponse;
	}
	
	public void setBitmap(Bitmap photo) {
		mBitmap = photo;
	}
	
	public Bitmap getBitmap() {
		return mBitmap;
	}

	@Override
	public void setAsSent() {
		mSent = true;
        this.delete();
        if (mSurvey.responses().size() == 0) {
            mSurvey.delete();
        }
	}
	
	public static List<Response> getAll() {
        return new Select().from(ResponsePhoto.class).orderBy("Id ASC").execute();
    }

}
