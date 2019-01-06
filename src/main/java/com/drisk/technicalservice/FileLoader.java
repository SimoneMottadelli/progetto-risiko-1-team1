package com.drisk.technicalservice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.drisk.domain.Difficulty;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class FileLoader {
	
	public JsonObject readDefaultMapFile(Difficulty difficulty) throws FileNotFoundException {
		BufferedReader bufferedReader;
		FileReader file;
		String difficultyStr = difficulty.toString().toLowerCase();
		try {
			file = new FileReader("default_map_" + difficultyStr + ".json");
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("FileNotFound: The map " + difficultyStr + "can't be loaded");
		}
		bufferedReader = new BufferedReader(file);
		Gson json = new Gson();
		return json.fromJson(bufferedReader, JsonObject.class); 
	}

}
