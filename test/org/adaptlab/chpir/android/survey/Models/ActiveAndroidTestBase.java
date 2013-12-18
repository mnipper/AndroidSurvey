package org.adaptlab.chpir.android.survey.Models;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

@PrepareForTest({ Cache.class, TableInfo.class, Context.class,
		ContentResolver.class, ContentProvider.class, ContentValues.class,
		SQLiteDatabase.class, SQLiteUtils.class, Select.class })
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.activeandroid.content.ContentProvider")
public class ActiveAndroidTestBase {

	protected static final String COLUMN_NAME = "Column";
	protected static final String DUMMY = "dummy";
	protected static final String ERROR_MESSAGE = "Error";
	protected static final Long ID = 1234L;
	//protected static final String TABLE = "TestTable";
	protected Context context;
	protected SQLiteDatabase sqliteDb;
	protected TableInfo tableInfo;
	protected Select select;

	/**
	 * Override this to add further setup code in subclasses.
	 */
	protected void onSetup() {
	}

	/**
	 * Setups a number of mocks, parts of them static, to be able to push things
	 * into ActiveAndroid internals, making simple tests possible.
	 */
	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(Cache.class);
		PowerMockito.mockStatic(ContentProvider.class);
		PowerMockito.mockStatic(SQLiteUtils.class);
		tableInfo = PowerMockito.mock(TableInfo.class);
		context = PowerMockito.mock(Context.class);
		ContentResolver resolver = PowerMockito.mock(ContentResolver.class);
		sqliteDb = PowerMockito.mock(SQLiteDatabase.class);
		ContentValues vals = PowerMockito.mock(ContentValues.class);
		PowerMockito.whenNew(ContentValues.class).withNoArguments().thenReturn(vals);
		PowerMockito.when(Cache.openDatabase()).thenReturn(sqliteDb);
		when(context.getContentResolver()).thenReturn(resolver);
		doNothing().when(resolver).notifyChange(any(Uri.class), any(ContentObserver.class));
		when(tableInfo.getFields()).thenReturn(new ArrayList<Field>());
		//when(tableInfo.getTableName()).thenReturn(TABLE); //Set by each class in the method onSetup
		PowerMockito.when(Cache.getTableInfo(any(Class.class))).thenReturn(tableInfo);
		PowerMockito.when(Cache.getContext()).thenReturn(context);
		PowerMockito.when(ContentProvider.createUri((Class<Model>) anyObject(), anyLong())).thenReturn(null);
		//File cache = new File("/sdcard/");
		//PowerMockito.when(context.getCacheDir()).thenReturn(cache);
		onSetup();
	}

}
