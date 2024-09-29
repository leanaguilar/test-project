package main.pojos;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader {

	public static ScratchGameConfig readConfigFile(String fileName) {

		// Create ObjectMapper instance from Jackson
		ObjectMapper objectMapper = new ObjectMapper();
		ScratchGameConfig config = null;

		try {
			// Read JSON file and map/deserialize into Java object
			config = objectMapper.readValue(new File(fileName), ScratchGameConfig.class);

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error when loading config class");

		}
	
		return config;

	}
}
