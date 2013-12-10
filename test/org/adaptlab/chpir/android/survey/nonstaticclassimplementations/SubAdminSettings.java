package org.adaptlab.chpir.android.survey.nonstaticclassimplementations;

import org.adaptlab.chpir.android.survey.Models.AdminSettings;

public class SubAdminSettings extends AdminSettings {
	
	public SubAdminSettings() {
		super();
	}
	
	public AdminSettings getInstanceNonStatic() {
		return super.getInstance();
	}

}
