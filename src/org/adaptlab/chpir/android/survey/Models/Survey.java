package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Surveys")
public class Survey extends Model {
	
	@Column(name = "Instrument")
	private Instrument mInstrument;
	
	public Survey() {
		super();
	}

	public Instrument getInstrument() {
		return mInstrument;
	}

	public void setInstrument(Instrument instrument) {
		mInstrument = instrument;
	}
	
	public List<Response> responses() {
		return getMany(Response.class, "Survey");
	}
}
