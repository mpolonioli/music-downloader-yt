package net.mpolonioli.musicdownloaderyt;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

public class YTDownloader {
	
	private final ProcessBuilder processBuilder = new ProcessBuilder();
	
	private String youtubedlPath;
	
	public YTDownloader(String youtubedlPath) {
		this.youtubedlPath = youtubedlPath;
	}
	
	public Process downloadAudio(String url, String format, String outputTemplate) throws IOException {
		
		List<String> cmd = new ArrayList<>();
		cmd.add(youtubedlPath);
		cmd.add("-x");
		cmd.add("--audio-format");
		cmd.add(format);
		cmd.add("-o");
		cmd.add(outputTemplate);
		cmd.add(url);

		processBuilder.command(cmd);
		
		processBuilder.redirectError(Redirect.INHERIT);
		processBuilder.redirectOutput(Redirect.INHERIT);
				
		return processBuilder.start();
	}

}
