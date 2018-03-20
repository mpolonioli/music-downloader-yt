package net.mpolonioli.musicdownloaderyt;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import net.mpolonioli.musicdownloaderyt.playlist.Playlist;
import net.mpolonioli.musicdownloaderyt.playlist.PlaylistManager;
import net.mpolonioli.musicdownloaderyt.playlist.Song;

public class MusicDownloader {

	private static final long DEFAULT_MAX_RESULTS = 5;
	private final String API_KEY = "AIzaSyBZSvKQzYZ0GvGGPSfMXoCoxlYTPlxQ-pQ";
	private final YTSearch YT_SEARCH;
	private final YTDownloader YT_DOWNLOADER;
	private final String APP_NAME = "music-downloader-yt";

	public MusicDownloader(String youtubedlPath) {
		YouTube youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {
			}
		}).setApplicationName(APP_NAME).build();
		YT_SEARCH = new YTSearch(API_KEY, youtube);
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
				SearchListResponse searchResponse = YT_SEARCH.searchVideos(queryTerm, maxResults);
				List<SearchResult> searchResultList = searchResponse.getItems();
				if (searchResultList != null) {
					SearchResult searchResult = findBetter(searchResultList);
					prettyPrint(searchResult, queryTerm);

					ytUrl = "https://www.youtube.com/watch?v=" + searchResult.getId().getVideoId();	
				}
			}
			if(ytUrl != null) {
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
			}else
			{
				System.out.println("\n-------------------------------------------------------------\n");
				System.out.println(" The search on query: " + queryTerm + " returned a null result.");
				System.out.println("\n-------------------------------------------------------------\n");
			}
		}else
		{
			System.out.println("\n-------------------------------------------------------------\n");
			System.out.println(" The song " + songFile.getName() + " already exist. Search and download skipped.");
			System.out.println("\n-------------------------------------------------------------\n");
		}
	}

	public void downloadSong(Song song, File outputDirectory, boolean classify) {
		downloadSong(song, outputDirectory, classify, DEFAULT_MAX_RESULTS);
	}

	public void downloadPlaylist(Playlist playlist, File outputDirectory, boolean classify) {
		List<Song> songList = playlist.getSongs();

		for(Song song : songList)
		{
			downloadSong(song, outputDirectory, classify);
		}
	}

	/*
	 * Find and return the better result in the list.
	 * Returns null if list is empty.
	 * TODO add more criteria.
	 */
	public static SearchResult findBetter(List<SearchResult> searchResultList) {
		if(searchResultList.isEmpty())
		{
			return null;
		}
		SearchResult betterResult = searchResultList.get(0);
		if(searchResultList.size() == 1) {
			return betterResult;
		}else
		{
			for(int i = 1; i < searchResultList.size(); i++) {
				SearchResult result = searchResultList.get(i);
				if(isBetter(result, betterResult)) {
					betterResult = result;
				}
			}
			return betterResult;
		}
	}

	/*
	 * Returns true if the first SearchResult is better than the second.
	 * Returns false otherwise.
	 */
	private static boolean isBetter(SearchResult video_1, SearchResult video_2) {
		String title_1 = video_1.getSnippet().getTitle();
		String title_2 = video_2.getSnippet().getTitle();
		if(!title_1.toLowerCase().contains("live") && title_2.toLowerCase().contains("live")) {
			return true;
		}else
		{
			return false;
		}
	}

	/*
	 * Prints out all results in the Iterator. For each result, print the
	 * title, video ID, and thumbnail.
	 *
	 * @param iteratorSearchResults Iterator of SearchResults to print
	 *
	 * @param query Search query (String)
	 */
	private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

		System.out.println("\n=============================================================");
		System.out.println(
				" Video for search on \"" + query + "\".");
		System.out.println("=============================================================\n");

		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" There aren't any results for your query.");
		}

		while (iteratorSearchResults.hasNext()) {

			SearchResult singleVideo = iteratorSearchResults.next();
			ResourceId rId = singleVideo.getId();

			// Confirm that the result represents a video. Otherwise, the
			// item will not contain a video ID.
			if (rId.getKind().equals("youtube#video")) {
				Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

				System.out.println(" Video Id" + rId.getVideoId());
				System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
				System.out.println(" Thumbnail: " + thumbnail.getUrl());
				System.out.println(" URL: https://www.youtube.com/watch?v=" + rId.getVideoId());
				System.out.println("\n-------------------------------------------------------------\n");
			}
		}
	}

	/*
	 * Prints out all results in the Iterator. For each result, print the
	 * title, video ID, and thumbnail.
	 *
	 * @param searchResult SearchResults to print
	 *
	 * @param query Search query (String)
	 */
	private static void prettyPrint(SearchResult searchResult, String queryTerm) {
		Iterator<SearchResult> iterator = Arrays.asList(searchResult).iterator();
		prettyPrint(iterator, queryTerm);
	}
	
	public Playlist updatePlaylist(File inputFile, long maxResults) throws IOException {
		Playlist result = updatePlaylist(PlaylistManager.getPlaylistFromFile(inputFile), maxResults);
		inputFile.delete();
		PlaylistManager.saveToFile(result, inputFile);
		return result;
	}
	
	public Playlist updatePlaylist(File inputFile) throws IOException {
		return updatePlaylist(inputFile, DEFAULT_MAX_RESULTS);
	}

	public Playlist updatePlaylist(Playlist playlist, long maxResults) {
		Playlist result = new Playlist(playlist.getName());
		for(Song song : playlist.getSongs()) {
			if(song.getYtUrl() == null ) {
				String queryTerm = song.getArtist() + " - " + song.getName();
				SearchListResponse searchResponse = YT_SEARCH.searchVideos(queryTerm, maxResults);
				List<SearchResult> searchResultList = searchResponse.getItems();
				if (searchResultList != null) {
					SearchResult searchResult = findBetter(searchResultList);
					prettyPrint(searchResult, queryTerm);

					song.setYtUrl("https://www.youtube.com/watch?v=" + searchResult.getId().getVideoId());	
				}
			}
			result.addSong(song);
		}
		return result;
	}
	
	public Playlist updatePlaylist(Playlist playlist) throws IOException {
		return updatePlaylist(playlist, DEFAULT_MAX_RESULTS);
	}

	// TODO
	public void downloadAction() {
		
	}

}
