package net.mpolonioli.musicdownloaderyt;

import java.io.File;
import java.io.IOException;

import net.mpolonioli.musicdownloaderyt.playlist.Playlist;
import net.mpolonioli.musicdownloaderyt.playlist.PlaylistManager;
import net.mpolonioli.musicdownloaderyt.playlist.Song;

public class TestPlaylist {
	
	public static void main(String[] args) throws IOException {
	Song song1 = new Song("nome1", "artista1", null);
	Song song2 = new Song("nome2", "artista2", null);
	Song songEq = new Song("nome1", "artista1", null);
	
	Playlist playlist = new Playlist("playlist1");

	playlist.addSong(song1);
	playlist.addSong(song2);
	playlist.addSong(songEq);
	
	PlaylistManager.saveToFile(playlist, "C:\\Users\\mpolo\\Desktop\\playlist1.playlist");
	for(Song song : PlaylistManager.getPlaylistFromFile(new File("C:\\Users\\mpolo\\Desktop\\playlist1.playlist")).getSongs()) {
		System.out.println(song.getArtist() + " - " + song.getName());
	}
	
	
	}
}
