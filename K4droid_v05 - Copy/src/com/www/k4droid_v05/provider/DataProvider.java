package com.www.k4droid_v05.provider;

import java.net.URI;

import com.www.k4droid_v05.db.DatabaseHandler;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DataProvider extends ContentProvider {

	public static final String PROVIDER_NAME = "com.www.k4droid.provider";
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/songs");
	public static final String ID_SONG = "song_id";
	public static final String NAME = "name";
	public static final String AUTHOR = "author";
	public static final String LYRIC = "lyric";
	public static final String FAVOR = "favorite";

	public static final int SONGS = 1;
	public static final int SONG_NAME = 2;
	public static final UriMatcher URI_MATCHER;
	private static final String DATABASE_TABLE = "songs";
	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(PROVIDER_NAME, "songs", SONGS);
		URI_MATCHER.addURI(PROVIDER_NAME, "books/#", SONG_NAME);
	}

	private SQLiteDatabase songDB;
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
//		DatabaseHandler databaseHandler = new DatabaseHandler(context);
//		databaseHandler.open();
		return ((songDB = DatabaseHandler.getDBConnection()) == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(DATABASE_TABLE);
		if (URI_MATCHER.match(uri) == SONG_NAME)
			queryBuilder.appendWhere(ID_SONG + " = "
					+ uri.getPathSegments().get(1));

		if (sortOrder == null || sortOrder == "")
			sortOrder = NAME;
		Cursor c = queryBuilder.query(songDB, projection, selection, selectionArgs, "unsignedname", null, sortOrder);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
