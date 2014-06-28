package com.www.k4droid_v05.db;

import com.www.k4droid_v05.util._Log;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;

public class SQLQueries {

	/**
	 * có thể dùng 1 hàm cho tất cả các loại truy vấn hay không? nếu có thì thay
	 * 2 phần FROM bằng tên_cột và " unsigned"+tên_côt trước khi đó kiểm tra xem
	 * có phải là select by song_id k.
	 */
	public static Cursor selectByName(String clue) {

		String query = "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE name  MATCH  \" "
				+ clue
				+ " \""
				+ " union "
				+ "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE unsignedname MATCH \""
				+ clue + "\"" + " ORDER BY unsignedname;";
		_Log.v(clue, query);

		return DatabaseHandler.getDBConnection().rawQuery(query, null);
	}

	public static Cursor selectByID(String clue) {
		String query = "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE song_id MATCH \""
				+ clue + "\"" + " ORDER BY song_id;";

		return DatabaseHandler.getDBConnection().rawQuery(query, null);
	}

	public static Cursor selectByAuthor(String clue) {
		String query = "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE author  MATCH  \" "
				+ clue
				+ " \""
				+ "union "
				+ "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE unsignedauthor MATCH \""
				+ clue + "\"" + " ORDER BY unsignedauthor;";

		return DatabaseHandler.getDBConnection().rawQuery(query, null);
	}

	public static Cursor selectByLyric(String clue) {
		String query = "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE lyric  MATCH  \" "
				+ clue
				+ " \""
				+ "union "
				+ "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE unsignedlyric MATCH \""
				+ clue + "\"" + " ORDER BY unsignedlyric;";

		return DatabaseHandler.getDBConnection().rawQuery(query, null);
	}

	public static Cursor selectFavorite(String clue) {
		String query = "";
		if (clue.equals("")) {
			query = "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE favorite MATCH 1 "
					+ " ORDER BY unsignedname;";
		} else {
			query = " SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE favorite = 1 AND unsignedname MATCH \'"
					+ clue
					+ " \' "
					+ " union "
					+ " SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs  WHERE favorite = 1 AND name MATCH \'"
					+ clue + " \' " + " ORDER BY unsignedname;";

		}

		return DatabaseHandler.getDBConnection().rawQuery(query, null);
	}

	public static Cursor selectFavoriteByName(String clue) {
		String query = "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE favorite=1 AND name  MATCH  \" "
				+ clue
				+ " \""
				+ "union "
				+ "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE favorite=1 AND unsignedname MATCH \""
				+ clue + "\"" + " ORDER BY unsignedname;";

		return DatabaseHandler.getDBConnection().rawQuery(query, null);
	}
	public static Cursor selectFavoriteByID(String clue) {
		String query = "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE favorite=1 AND song_id MATCH \""
				+ clue + "\"" + " ORDER BY song_id;";

		return DatabaseHandler.getDBConnection().rawQuery(query, null);
	}
	 
	public static Cursor selectFavoriteByAuthor(String clue) {
		String query = "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE favorite=1 AND name  MATCH  \" "
				+ clue
				+ " \""
				+ "union "
				+ "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE favorite=1 AND unsignedauthor MATCH \""
				+ clue + "\"" + " ORDER BY unsignedname;";

		return DatabaseHandler.getDBConnection().rawQuery(query, null);
	}
	public static Cursor selectFavoriteByLyric(String clue) {
		String query = "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE favorite=1 AND name  MATCH  \" "
				+ clue
				+ " \""
				+ "union "
				+ "SELECT song_id, name, unsignedname, author , unsignedauthor, lyric, unsignedlyric, favorite FROM songs WHERE favorite=1 AND unsignedlyric MATCH \""
				+ clue + "\"" + " ORDER BY unsignedname;";

		return DatabaseHandler.getDBConnection().rawQuery(query, null);
	}
	public static void addFavorite(int rowId) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("favorite", 1);
		try {

			DatabaseHandler.getDBConnection().update("songs", contentValues,
					" song_id = ? ", new String[] { String.valueOf(rowId) });
			_Log.i(SQLQueries.class.getName(), "Add favorite at item " + rowId
					+ " successful");
		} catch (SQLiteException sqliteEx) {
			_Log.i(SQLQueries.class.getName(), sqliteEx.toString());
		}
	}

	public static void unFavorite(String songId) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("favorite", 0);
		try {

			DatabaseHandler.getDBConnection().update("songs", contentValues,
					" song_id = ? ", new String[] { String.valueOf(songId) });
			_Log.i(SQLQueries.class.getName(), "Remove favorite at item "
					+ songId + " successful");
		} catch (SQLiteException sqliteEx) {
			_Log.i(SQLQueries.class.getName(), sqliteEx.toString());
		}
	}
}
