package net.mpolonioli.musicdownloaderyt.playlist;

public class Song {
	
	private String name;
	private String artist;
	
	public Song(String name, String artist) {
		setName(name);
		setArtist(artist);
	}
	
	public boolean equals(Song song) {
		if(getName().equals(song.getName()) && getArtist().equals(song.getArtist()))
		{
			return true;
		} else
		{
			return false;
		}		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

}
