package net.mpolonioli.musicdownloaderyt;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import net.mpolonioli.musicdownloaderyt.playlist.Playlist;
import net.mpolonioli.musicdownloaderyt.playlist.Song;

public class MusicDownloader {

	private static final long DEFAULT_MAX_RESULTS = 5;
	private final YTDownloader YT_DOWNLOADER;

	public MusicDownloader(String youtubedlPath) {
		YT_DOWNLOADER = new YTDownloader(youtubedlPath);
	}

	public void downloadSong(Song song, File outputDirectory, boolean classify, long maxResults) {
		String artist = song.getArtist();
		String title = song.getName();
		String ytUrl = song.getYtUrl();
		
		// define the query to search.
		String queryTerm = artist + " - " + title;

		// create the artist directory if not exist and if classify == true.
		File songFile;
		if(classify) {
			File artistDir = new File(outputDirectory.getAbsolutePath() + "/" + artist);
			artistDir.mkdirs();
			songFile = new File(artistDir.getAbsolutePath() + "/" + queryTerm + ".mp3");
		} else {
			songFile = new File(outputDirectory.getAbsolutePath() + "/" + queryTerm + ".mp3");
		}

		if(!songFile.exists())
		{
			// search the video, print the result and download the audio.
			if(ytUrl == null) {
				System.out.println(" No YouTube URI specified for song [" + queryTerm + "]");
			} else {
				try {
					// define the file path without extension.
					Process downloadProcess = YT_DOWNLOADER.downloadAudio(
							ytUrl,
							"mp3",
							songFile.getAbsolutePath() + " (untagged).%(ext)s");
					try {
						downloadProcess.waitFor();
					} catch (InterruptedException e) {
						System.err.println("There was an InterruptedException: " + e.getCause() + " : " + e.getMessage());
					}
				} catch (IOException e) {
					System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
				}
				try {
					Mp3Tagger.setID3V1Tag(songFile.getAbsolutePath() + " (untagged).mp3", songFile.getAbsolutePath(), title, artist, true);
				} catch (UnsupportedTagException | InvalidDataException | NotSupportedException | IOException e) {
					System.err.println("There was an error during the tag process with song " + queryTerm + ": " + e.getCause() + " : " + e.getMessage());
				}
			}
		}else
		{
			System.out.println(" The song " + songFile.getName() + " already exist. Search and download skipped.");
		}
	}

	public void downloadSong(Song song, File outputDirectory, boolean classify) {
		downloadSong(song, outputDirectory, classify, DEFAULT_MAX_RESULTS);
	}

	public void downloadPlaylist(Playlist playlist, File outputDirectory, boolean classify) {
		List<Song> songList = playlist.getSongs();

		int count = 0;
		for(Song song : songList)
		{
			count++;
			System.out.println("\n PROCESSING SONG " + count + "/" + songList.size() + ": " + song.getArtist() + " - " + song.getName());
			System.out.println("-------------------------------------------------------------");
			downloadSong(song, outputDirectory, classify);
			System.out.println("-------------------------------------------------------------");
		}
	}

}
