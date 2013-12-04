package org.adaptlab.chpir.android.survey.Models;

import java.util.ArrayList;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.activeandroid.Cache;
import com.activeandroid.TableInfo;
import com.activeandroid.content.ContentProvider;

@PrepareForTest({ Cache.class, TableInfo.class, Context.class, ContentResolver.class, ContentProvider.class, ContentValues.class })
@RunWith(PowerMockRunner.class)
//@SuppressStaticInitializationFor("com.activeandroid.content.ContentProvider")
public class ActiveAndroidTestBase {

	/** Testing column name. */
    protected static final String COLUMN_NAME = "Column";
    /** Dummy string. */
    protected static final String DUMMY = "dummy";
    /** Dummy error message. */
    protected static final String ERROR_MESSAGE = "Error";
    /** Dummy id. */
    protected static final Long ID = 1234L;
    /** Dummy table name. */
    protected static final String TABLE = "TestTable";
    /** Mocked android context - to be able to inject stuff into ActiveAndroid internals. */
    protected Context context;
    /** Mocked sqlite database. */
    protected SQLiteDatabase sqliteDb;
    /** Mocked table info. */
    protected TableInfo tableInfo;

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
            when(tableInfo.getTableName()).thenReturn(TABLE);
            PowerMockito.when(Cache.getTableInfo(any(Class.class))).thenReturn(tableInfo);
            PowerMockito.when(Cache.getContext()).thenReturn(context);
            //PowerMockito.when(ContentProvider.createUri((Class<ValidatingModel>) anyObject(), anyLong())).thenReturn(null);
            onSetup();
    }
	
}
