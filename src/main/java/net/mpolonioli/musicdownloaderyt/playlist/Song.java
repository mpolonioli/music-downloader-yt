package net.mpolonioli.musicdownloaderyt.playlist;

public class Song {
	
	private String name;
	private String artist;
	private String ytUrl;
	
	public Song(String name, String artist, String ytUrl) {
		setName(name);
		setArtist(artist);
		setYtUrl(ytUrl);
	}

	public boolean equals(Song song) {
		if(getYtUrl() != null && song.getYtUrl() != null) 
		{
			if(
					getName().equals(song.getName()) && 
					getArtist().equals(song.getArtist()) &&
					getYtUrl().equals(song.getYtUrl())
					)
			{
				return true;
			} else
			{
				return false;
			}	
		} else
		{
			if(
					getName().equals(song.getName()) && 
					getArtist().equals(song.getArtist())
					)
			{
				return true;
			} else
			{
				return false;
			}
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

	public String getYtUrl() {
		return ytUrl;
	}

	public void setYtUrl(String ytUrl) {
		this.ytUrl = ytUrl;
	}

}
