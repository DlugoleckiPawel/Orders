package com.app.converter;

import com.app.exceptions.AppException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public abstract class JsonConverter<T> {

	private final String jsonFilename;
	private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	private final Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	public JsonConverter(String jsonFilename) {
		this.jsonFilename = jsonFilename;
	}

	public void toJson(final T element) {
		try (FileWriter fileWriter = new FileWriter(jsonFilename)) {
			if (element == null) {
				throw new NullPointerException("ELEMENT IS NULL");
			}
			gson.toJson(element, fileWriter);
		} catch (Exception e) {
			throw new AppException("JSON PARSER EXCEPTION");
		}
	}
}