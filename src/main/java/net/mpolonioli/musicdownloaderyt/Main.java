package net.mpolonioli.musicdownloaderyt;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

	public static void main(String[] args) {
		/*
		 * 	Get the required parameters from args:
		 *  outputDirectory, inputFile, youtubedlPath.
		 */
		
		Options options = new Options();
		
		Option outDirPath = new Option("o", "output", true, "Output directory (default is './')");
		options.addOption(outDirPath);
		
		Option inFilePath = new Option("p", "playlist", true, "Playlist file (required)");
		inFilePath.setRequired(true);
		options.addOption(inFilePath);
		
		Option dlPath = new Option("y", "youtube-dl", true, "Path to youtube-dl (default is 'youtube-dl')");
		options.addOption(dlPath);
		
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
		
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("music-downloader-yt", options);

            System.exit(1);
            return;
        }
        
        File inFile = new File(cmd.getOptionValue('p'));
        
        File outDir;
        if(cmd.getOptionValue('o') != null) {
        	outDir = new File(cmd.getOptionValue('o'));
        } else {
        	outDir = new File("./");
        }
        
        String youtubeDlPath;
        if(cmd.getOptionValue('y') != null) {
        	youtubeDlPath = cmd.getOptionValue('y');
        } else {
        	youtubeDlPath = "youtube-dl";
        }

        System.out.println(
        		"Launch Parameters\n" +
        		"\nOutput Directory: " + outDir.getAbsolutePath() +
        		"\nPlaylist File: " + inFile.getAbsolutePath() +
        		"\nYoutube-dl path: " + youtubeDlPath + "\n"
        		);

		MusicDownloader downloader = new MusicDownloader(youtubeDlPath);
		
		try {
			downloader.downloadPlaylist(downloader.updatePlaylist(inFile), outDir);
		} catch (IOException e) {
			System.err.println("There was a IOException: " + e.getCause() + " : " + e.getMessage());
		}
	}
}