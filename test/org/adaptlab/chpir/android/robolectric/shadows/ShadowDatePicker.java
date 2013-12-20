package org.adaptlab.chpir.android.robolectric.shadows;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowFrameLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.DatePicker;

@Implements(value = DatePicker.class)
public class ShadowDatePicker extends ShadowFrameLayout {
	
	@RealObject private DatePicker datePicker;
	
	int year;
	int month;
	int day;

	public void __constructor__(Context context, AttributeSet attrs, int defStyle) {
	
	}
	
	@Implementation
	public void setCalendarViewShown(boolean shown){
		
	}
	
	@Implementation
	public void init(int year, int monthOfYear, int dayOfMonth, DatePicker.OnDateChangedListener onDateChangedListener) {
		
	}
	
	@Implementation
	public void updateDate(int year, int month, int dayOfMonth) {
		this.year = year;
		this.month = month;
		this.day = dayOfMonth;
	}
	
	@Implementation
	public int getYear() {
		return this.year;
	}
	
	@Implementation 
	public int getMonth() {
		return this.month;
	}
	
	@Implementation
	public int getDayOfMonth() {
		return this.day;
	}
	
}
