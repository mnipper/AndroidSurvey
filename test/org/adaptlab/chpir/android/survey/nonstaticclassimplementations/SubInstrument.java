package org.adaptlab.chpir.android.survey.nonstaticclassimplementations;

import java.util.List;

import org.adaptlab.chpir.android.survey.Models.Instrument;

public class SubInstrument extends Instrument {

	public SubInstrument() {
		super();
	}
	
	public List<Instrument> getAllNonStatic() {
		return super.getAll();
	}
	
	public Instrument findByRemoteIdNonStatic(Long id) {
		return super.findByRemoteId(id);
	}
	
	public String getDeviceLanguageNonStatic() {
		return super.getDeviceLanguage();
	}
}
