package net.mpolonioli.musicdownloaderyt.playlist;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
	
	private List<Song> songs;
	
	private String name;
	
	public Playlist(String name) {
		setSongs(new ArrayList<Song>());
	}
	
	public Playlist(String name, List<Song> songs) {
		setSongs(songs);
	}
	
	public void addSong(Song song) {
		if(!contains(song))
		{
			songs.add(song);
		}
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}
	
	public boolean isEmpty() {
		return songs.isEmpty();
	}
	
	public boolean contains(Song song) {
		for(Song localSong : getSongs()) {
			if(localSong.equals(song)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
