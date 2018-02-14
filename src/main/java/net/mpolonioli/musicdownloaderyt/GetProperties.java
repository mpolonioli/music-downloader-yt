package net.mpolonioli.musicdownloaderyt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class GetProperties {
		
	public HashMap<String, String> getPropValues() throws IOException {
		
		InputStream inputStream = null;
		HashMap<String, String> result = new HashMap<>();

		try {
			Properties prop = new Properties();
			
			File propFile = new File("./conf/config.properties");
			
			inputStream = new FileInputStream(propFile);
			prop.load(inputStream);

			// get the property value and print it out
			String outputDirectory = prop.getProperty("outputDirectory");
			String youtubedlPath = prop.getProperty("youtubedlPath");
			String inputFile = prop.getProperty("inputFile");
			
			System.out.println(
					"outputDirectory=" + outputDirectory + "\n" + 
					"youtubedlPath=" + youtubedlPath + "\n" + 
					"inputFile=" + inputFile
					);

			// add the property value to the result
			result.put("outputDirectory", outputDirectory);
			result.put("youtubedlPath", youtubedlPath);
			result.put("inputFile" , inputFile);
		}catch(Exception e) 
		{
			e.printStackTrace();
		}finally
		{
			inputStream.close();
		}

		return result;
	}

}
