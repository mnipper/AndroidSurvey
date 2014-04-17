package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Sections")
public class Section extends ReceiveModel {

	private static final String TAG = "Section";
	
	@Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;
	@Column(name = "Instrument")
    private Instrument mInstrument;
	@Column(name = "Title")
    private String mTitle;
	@Column(name = "StartQuestionIdentifier")
    private String mStartQuestionIdentifier;

	@Override
	public void createObjectFromJSON(JSONObject jsonObject) {
		try {
            Long remoteId = jsonObject.getLong("id");
            Section section = Section.findByRemoteId(remoteId);
            if (section == null) {
            	section = this;
            }
            section.setRemoteId(remoteId);
            section.setInstrument(Instrument.findByRemoteId(jsonObject.getLong("instrument_id")));
            section.setTitle(jsonObject.getString("title"));
            section.setStartQuestionIdentifier(jsonObject.getString("start_question_identifier"));
            section.save();
		} catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }  
	}

	public void setInstrument(Instrument instrument) {
		mInstrument = instrument;
	}

	public void setRemoteId(Long remoteId) {
		mRemoteId = remoteId;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public void setStartQuestionIdentifier(String beginId) {
		mStartQuestionIdentifier = beginId;
	}
	
	public String getStartQuestionIdentifier() {
		return mStartQuestionIdentifier;
	}
	
	public String getTitle() {
		return mTitle;
	}

	public static Section findByRemoteId(Long remoteId) {
		return new Select().from(Section.class).where("RemoteId = ?", remoteId).executeSingle();
	}
	
	public static List<Section> getAll() {
		return new Select().from(Section.class).orderBy("Id ASC").execute();
	}

}
