package com.hentica.tools;

import com.google.gson.Gson;

import java.util.Map;

/** 对象转map */
public class MapHelper {

	@SuppressWarnings("unchecked")
	public static Map<String, Object> objectToMap(Object obj) {

		Gson gson = new Gson();
		String json = gson.toJson(obj);
		return gson.fromJson(json, Map.class);
	}

}