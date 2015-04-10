package org.adaptlab.chpir.android.survey.Models;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.adaptlab.chpir.android.survey.AppUtil;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Grids")
public class Grid extends ReceiveModel {
	private static final String TAG = "Grid"; 
	@Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;
	@Column(name = "Instrument")
    private Instrument mInstrument;
	@Column(name = "Name")
    private String mName;
	@Column(name = "QuestionType")
	private String mQuestionType;
	
	@Override
	public void createObjectFromJSON(JSONObject jsonObject) {
		try {
			Long remoteId = jsonObject.getLong("id");
			Grid grid = Grid.findByRemoteId(remoteId);
			if (grid == null) {
				grid = this;
			}
			grid.setRemoteId(remoteId);
			grid.setQuestionType(jsonObject.getString("question_type"));
			grid.setName(jsonObject.getString("name"));
			grid.setInstrument(Instrument.findByRemoteId(jsonObject.getLong("instrument_id")));
			grid.save();
			if (AppUtil.DEBUG) Log.i(TAG, "Created Grid from JSON with ID: " + remoteId);
		} catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }
	}

	public static Grid findByRemoteId(Long remoteId) {
		return new Select().from(Grid.class).where("RemoteId = ?", remoteId).executeSingle();
	}
	
	private void setInstrument(Instrument instrument) {
		mInstrument = instrument;
	}

	private void setName(String name) {
		mName = name;
	}

	private void setQuestionType(String questionType) {
		mQuestionType = questionType;
	}

	private void setRemoteId(Long remoteId) {
		mRemoteId = remoteId;
	}
	
}
