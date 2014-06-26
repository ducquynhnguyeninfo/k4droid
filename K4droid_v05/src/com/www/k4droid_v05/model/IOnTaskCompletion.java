package com.www.k4droid_v05.model;

import java.util.List;

import com.www.k4droid_v05.obj.ObjSong;

/**
 * An Interface that recognizes when the task of {@link Loader} has completed.
 * 
 * @author DUC QUYNH
 * 
 */
public interface IOnTaskCompletion {
	/**
	 * Called in onPostExecute() method when the task has completed.
	 * 
	 * @param objSongs
	 */
	void onTaskCompleted(List<ObjSong> objSongs);

}