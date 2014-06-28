package com.www.k4droid_v05.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.www.k4droid_v05.util._Log;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
/**
 * Helps us great database if it is not existed.
 * @author DUC QUYNH
 *
 */
public class _SQLiteOpenHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "db_k4droid";
	static final int NEW_DB_VERSION = 6;//
	public static String DATABASE_FILE_PATH_EXTERNAL = "";
	 public static String DATABASE_FILE_PATH_INTERNAL = "/data/data/com.www.k4droid_v05/databases/";
	
	private Context context;

	static int CURRENT_DB_VERSION;

	public _SQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, NEW_DB_VERSION);
		this.context = context;
	}

	public _SQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	public _SQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public _SQLiteOpenHelper(Context context, SQLiteDatabase db) {
		super(context, DATABASE_NAME, null, NEW_DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		try {
			_Log.w("sw", "updating database...");
			copyDatabase();
			_Log.w("sw", "updating completed.");
		} catch (IOException e) {
			throw new Error("Error copying database");
		}

	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDatabase() {
		try {
			_Log.w("sw", "copying database ");
			copyDatabase();
		} catch (IOException e) {
			e.getCause();
			throw new Error("Error copying database");
		}
	}

	/**
	 * 
	 * @return database file's path.
	 */
	static String getDatabasePath() {

		File filename = Environment.getExternalStorageDirectory();

		DATABASE_FILE_PATH_EXTERNAL = filename.getAbsolutePath()
				+ File.separator + ".K4Droid" + File.separator + ".databases"
				+ File.separator;
		File databaseFile = new File(DATABASE_FILE_PATH_EXTERNAL);
		if (!databaseFile.exists())
			databaseFile.mkdirs();

		_Log.i("_Log", ":" + DATABASE_FILE_PATH_EXTERNAL);
		return DATABASE_FILE_PATH_INTERNAL;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDatabase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = context.getAssets().open(DATABASE_NAME);

		// path to the just created empty database
		String outFileName = getDatabasePath()

		+ DATABASE_NAME;

		// open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		long total = 2904064;
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
			// _Log.w("processing ... length", length + "");
			_Log.w("processing ...", (((long) length) / total) * 100 + "%");
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}
}
