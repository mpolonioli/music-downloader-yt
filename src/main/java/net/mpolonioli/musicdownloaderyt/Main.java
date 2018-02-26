package net.mpolonioli.musicdownloaderyt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		/*
		 * 	Get the required parameters from configuration file.
		 */
		GetProperties propertiesGetter = new GetProperties();
		HashMap<String, String> propHashMap = null;
		try {
			propHashMap = propertiesGetter.getPropValues();
		} catch (IOException e) {
			System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
		}

		File outputDirectory = null;
		String output = propHashMap.get("outputDirectory");
		if(output != null && !output.equals(""))
		{
			outputDirectory = new File(output);
		} else 
		{
			outputDirectory = new File("./");
		}
		File inputFile = new File(propHashMap.get("inputFile"));
		String youtubedlPath = propHashMap.get("youtubedlPath");

		MusicDownloader downloader = new MusicDownloader(youtubedlPath);
		
		try {
			downloader.downloadPlaylist(downloader.updatePlaylist(inputFile), outputDirectory);
		} catch (IOException e) {
			System.err.println("There was a IOException: " + e.getCause() + " : " + e.getMessage());
		}
	}
}