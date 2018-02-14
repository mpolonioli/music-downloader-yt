package net.mpolonioli.musicdownloaderyt;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

public class TestApp {
	/**
	 * Define a global variable that identifies the name of a file that
	 * contains the developer's API key.
	 */
	private static final String PROPERTIES_FILENAME = "youtube.properties";

	private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

	public static void main(String[] args) {
		// This object is used to make YouTube Data API requests. The last
		// argument is required, but since we don't need anything
		// initialized when the HttpRequest is initialized, we override
		// the interface and provide a no-op function.
		YouTube youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {
			}
		}).setApplicationName("music-downloader-yt").build();

		
		// instance the YTSearch.
		YTSearch ytDownloader = new YTSearch(new File("./resources/" + PROPERTIES_FILENAME), youtube);

		try 
		{
			// Prompt the user to enter a query term.
			String queryTerm = getInputQuery();

			// Call the API and print results.
			SearchListResponse searchResponse = ytDownloader.searchVideos(queryTerm, NUMBER_OF_VIDEOS_RETURNED);
			List<SearchResult> searchResultList = searchResponse.getItems();
			if (searchResultList != null) {
				prettyPrint(searchResultList.iterator(), queryTerm);
			}
		} catch (IOException e) {
			System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
		}
	}

	/*
	 * Prompt the user to enter a query term and return the user-specified term.
	 */
	private static String getInputQuery() throws IOException {

		String inputQuery = "";

		System.out.print("Please enter a search term: ");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		inputQuery = bReader.readLine();

		if (inputQuery.length() < 1) {
			// Use the string "YouTube Developers Live" as a default.
			inputQuery = "YouTube Developers Live";
		}
		return inputQuery;
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
				"   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
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
    
}
