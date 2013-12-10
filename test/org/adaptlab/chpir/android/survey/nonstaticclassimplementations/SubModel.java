package org.adaptlab.chpir.android.survey.nonstaticclassimplementations;

import com.activeandroid.Model;

public class SubModel extends Model {
	public SubModel(){
		super();
	}
	
	public <T extends Model> T loadNonStatic(Class<T> type, long id) {
		return super.load(type, id);
	}
}
