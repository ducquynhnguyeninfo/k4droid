package com.www.k4droid_v05.obj;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private List<ObjSong> objects;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ObjSong> getObjects() {
		return objects;
	}

	public void setObjects(List<ObjSong> objects) {
		this.objects = objects;
	}

	public static Data newInstance() {
		Data data = new Data();
		return data;
	}
}