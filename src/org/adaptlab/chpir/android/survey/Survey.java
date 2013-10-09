package org.adaptlab.chpir.android.survey;

import java.util.ArrayList;

public class Survey {
	private Instrument mInstrument;
	private ArrayList<Response> mResponses;
	
	public Survey() {
		mResponses = new ArrayList<Response>();
	}

	public Instrument getInstrument() {
		return mInstrument;
	}

	public void setInstrument(Instrument instrument) {
		mInstrument = instrument;
	}

	public ArrayList<Response> getResponses() {
		return mResponses;
	}

	public void addResponse(Response response) {
		mResponses.add(response);
	}
	
}
