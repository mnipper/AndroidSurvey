package org.adaptlab.chpir.android.survey;

import java.util.List;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Instruments")
public class Instrument extends Model {
	
	@Column(name = "Title")
	private String mTitle;
	
	public Instrument() {
		super();
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}
	
	public List<Question> questions() {
		return getMany(Question.class, "Instrument");
	}
	
	public static List<Instrument> getAll() {
		return new Select()
			.from(Instrument.class)
			.orderBy("Id ASC")
			.execute();
	}
	
	@Override
	public String toString() {
		return mTitle;
	}
}
