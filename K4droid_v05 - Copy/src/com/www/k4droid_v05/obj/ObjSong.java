package com.www.k4droid_v05.obj;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents for a song and its attributes. Supports both
 * {@link Serializable} and {@link Parcelable}.
 * 
 * @author DUC QUYNH
 * 
 */
public class ObjSong implements Serializable, Parcelable {

	public static String SONG_ID = "song_id";
	public static String SONG_NAME = "song_name";
	public static String SONG_AUTHOR = "song_author";
	public static String SONG_LYRIC = "song_lyric";
	public static String SONG_IS_FAVORITE = "song_is_favorite";

	private static final long serialVersionUID = 1L;
	private int row_Id;
	private String songId;
	private String name;
	private String author;
	private String lyric;
	private int favorite;

	/**
	 * Constructor, we can use this to set a song properties quickly.
	 * 
	 * @param songId
	 * @param name
	 * @param author
	 * @param lyric
	 * @param favorite
	 */
	public ObjSong(String songId, String name, String author, String lyric,
			int favorite) {
		this.setSongId(songId);
		this.setSongName(name);
		this.setSongAuthor(author);
		this.setSongLyric(lyric);
		this.setSongIsFavorite(favorite);
	}

	public ObjSong() {
	}

	/**
	 * Set id row for a song. Id row is the position of song in a query result.
	 * 
	 * @param row_Id
	 */
	public void setRow_Id(int row_Id) {
		this.row_Id = row_Id;
	}

	/**
	 * 
	 * @return The position of song in a query result.
	 */
	public int getRow_Id() {
		return row_Id;
	}

	/**
	 * 
	 * @return The song'id in database.
	 */
	public String getSongId() {
		return songId;
	}

	/**
	 * Set id for the song we has got from database.
	 * 
	 * @param songId
	 */
	public void setSongId(String songId) {
		this.songId = songId;
	}

	/**
	 * Set a name for the song we has got from database.
	 * 
	 * @param songName
	 */
	public void setSongName(String songName) {
		this.name = songName;
	}

	/**
	 * 
	 * @return The song's name.
	 */
	public String getSongName() {
		return name;
	}

	/**
	 * Set author for the song we has got from database.
	 * 
	 * @param songAuthor
	 */
	public void setSongAuthor(String songAuthor) {
		this.author = songAuthor;
	}

	/**
	 * 
	 * @return The song's author.
	 */
	public String getSongAuthor() {
		return author;
	}

	/**
	 * Set lyric for the song we has got from database.
	 * 
	 * @param songLyric
	 */
	public void setSongLyric(String songLyric) {
		this.lyric = songLyric;
	}

	/**
	 * 
	 * @return The song's lyric.
	 */
	public String getSongLyric() {
		return lyric;
	}

	/**
	 * Set a song is being favorite or not that we has got from database.
	 * 
	 * @param isFavorite
	 */
	public void setSongIsFavorite(int isFavorite) {
		this.favorite = isFavorite;
	}

	// public void setSongIsFavorite(boolean isFavorite) {
	// if(isFavorite == true) {
	// this.favorite = 1;
	// }
	// if(isFavorite == false) {
	// this.favorite = 0;
	// }
	// }
	//
	/**
	 * 
	 * @return the song favorite state.
	 */
	public int getSongIsFavorite() {
		return favorite;
	}

	/**
	 * Set song's author or lyric depend on case.
	 * 
	 * @param string
	 */
	public void setSongAuthorOrLyric(String string) {
		this.author = string;
		this.lyric = string;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(songId);
		out.writeString(name);
		out.writeString(author);
		out.writeString(lyric);
		out.writeInt(favorite);
	}
}
