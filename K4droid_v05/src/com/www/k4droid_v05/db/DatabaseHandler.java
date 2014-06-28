package com.www.k4droid_v05.db;

import java.io.File;
import java.io.IOException;

import com.www.k4droid_v05.util._Log;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * {@link DatabaseHandler} is in charge of handling whether creating database or
 * not ups to database has existed or not and updating depends on database has
 * expired or not. It also handles the connecting process by provides a API like
 * getConnection(), and close().
 * 
 * @author DUC QUYNH
 * 
 */
public class DatabaseHandler {

	private final static String TAG = "DatabaseHandler";
	private static _SQLiteOpenHelper sqLiteOpenHelper;
	private final Context context;
	private SQLiteDatabase db = null;
	private static SQLiteDatabase conn;

	public DatabaseHandler(Context context) {
		this.context = context;
		sqLiteOpenHelper = new _SQLiteOpenHelper(this.context);
		switch (checkDBState()) {
		case 0: {
			_Log.w("sw", "copying database ");
			File databaseFile = new File(_SQLiteOpenHelper.getDatabasePath());
			if (!databaseFile.exists())
				databaseFile.mkdirs();
			sqLiteOpenHelper.createDatabase();
		}
			break;
		case 1: {
			// getWritableDatabase();// củng gọi onUpgrade();
			// close();
			sqLiteOpenHelper.onUpgrade(sqLiteOpenHelper.getWritableDatabase(),
					_SQLiteOpenHelper.CURRENT_DB_VERSION,
					_SQLiteOpenHelper.NEW_DB_VERSION);
		}
			break;
		case 2:
			return;
		}

	}

	private static final int STATE_NOT_EXIST = 0;
	private static final int STATE_EXPIRED = 1;
	private static final int STATE_UP_TO_DATE = 2;

	private int checkDBState() {
		if (databaseExist()) {

			SQLiteDatabase db = SQLiteDatabase.openDatabase(
					_SQLiteOpenHelper.getDatabasePath()
							+ _SQLiteOpenHelper.DATABASE_NAME, null,
					SQLiteDatabase.CREATE_IF_NECESSARY
//							| SQLiteDatabase.ENABLE_WRITE_AHEAD_LOGGING
							);
			this.db = db;
			if (_SQLiteOpenHelper.NEW_DB_VERSION > (_SQLiteOpenHelper.CURRENT_DB_VERSION = db
					.getVersion())) {

				_Log.i("DB version", "Current: "
						+ _SQLiteOpenHelper.CURRENT_DB_VERSION + " - new vs: "
						+ _SQLiteOpenHelper.NEW_DB_VERSION);
				db.close();
				SQLiteDatabase.releaseMemory();
				db = null;
				_Log.w("", "databases already exists --- DB is expired");
				return STATE_EXPIRED;
			} else {
				_Log.w("", "databases already exists --- DB is up to date");
				return STATE_UP_TO_DATE;
			}
		} else {
			_Log.w("", "databases not exists");
			return STATE_NOT_EXIST;
		}

	}

	/**
	 * Checks the database file whether existed or not.
	 * 
	 * @return
	 */
	private boolean databaseExist() {
		File dbFile = new File(_SQLiteOpenHelper.getDatabasePath()
				+ _SQLiteOpenHelper.DATABASE_NAME);
		return dbFile.exists();
	}

	public static SQLiteDatabase getDBConnection() {
		if (conn == null)
			conn = sqLiteOpenHelper.getWritableDatabase();
		_Log.v(TAG, "getDBConnection()");
		return conn;
	}

	public void close(SQLiteDatabase _db) {
		_Log.v(TAG, "onClose()");
		if (_db != null) {
			_db.close();
			_db = null;
		}
		db.close();
		db = null;
		conn.close();
		conn = null;
		sqLiteOpenHelper.close();
	}
}
