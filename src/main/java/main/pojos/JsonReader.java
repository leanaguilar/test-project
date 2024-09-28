package main.pojos;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader {
	
	    public ScratchGameConfig readConfigFile(String fileName) {
	       
	    	// Create ObjectMapper instance from Jackson
	        ObjectMapper objectMapper = new ObjectMapper();

	        try {
	            // Read JSON file and map/deserialize into Java object
	        	ScratchGameConfig config = objectMapper.readValue(new File("config.json"), ScratchGameConfig.class);

	            // Print the User object to verify the data
	            System.out.println(config);
	            return config;

	        } catch (IOException e) {
	            e.printStackTrace();
	            //manejar error
	        }
			return null;
			
	    }
	}


