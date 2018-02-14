package net.mpolonioli.musicdownloaderyt.playlist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PlaylistManager {
		
	public static Playlist getPlaylistFromFile(File file) throws FileNotFoundException {
		Playlist playlist = new Playlist(file.getName());
		Scanner sc = new Scanner(file);
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			if(line.contains("|"))
			{
				String lineSplit[] = line.split("\\|");
				playlist.addSong(new Song(lineSplit[1], lineSplit[0]));
			}
		}
		sc.close();
		return playlist;
	}
	
	public static void saveToFile(Playlist playlist, File file) throws IOException {
		if(file.exists())
		{
			file.delete();
		}
		file.createNewFile();
		PrintWriter writer = new PrintWriter(file);
		for(Song song : playlist.getSongs()) {
			writer.println(song.getArtist() + "|" + song.getName());
		}
		writer.close();
	}
	
	public static void saveToFile(Playlist playlist, String dest) throws IOException {
		saveToFile(playlist, new File(dest));
	}
	
	/*
	 * Read all files in folder and for each file calls getPlayListFromFile(file).
	 * Returns a list of Playlist.
	 * 
	 * Only file that ends with the string ".playlist" are considered.
	 */
	public static List<Playlist> getPlaylistsInFolder(File folder) {
		List<Playlist> playlists = new ArrayList<>();
		if(folder.isDirectory())
		{
			List<File> files = Arrays.asList(folder.listFiles());
			for(File file : files) {
				if(file.isFile() && file.getName().endsWith(".playlist")) {
					try {
						playlists.add(getPlaylistFromFile(file));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return playlists;
	}
}
