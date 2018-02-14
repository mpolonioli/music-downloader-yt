package net.mpolonioli.musicdownloaderyt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

public class YTSearch 
{
	/**
	 * Define a variable that identifies the file the API key.
	 */
	private final String API_KEY;

	/**
	 * Define a global instance of a Youtube object, which will be used
	 * to make YouTube Data API requests.
	 */
	private YouTube youtube; 

	public YTSearch(File youtubePropFile, YouTube youtube) {
		this.youtube = youtube;
		// Read the developer key from the properties file.
		Properties properties = new Properties();
		try {
			InputStream in = new FileInputStream(youtubePropFile);
			properties.load(in);

		} catch (IOException e) {
			System.err.println("There was an error reading " + youtubePropFile.getName() + ": " + e.getCause()
			+ " : " + e.getMessage());
			System.exit(1);
		}
		API_KEY = properties.getProperty("youtube.apikey");
	}
	
	public YTSearch(String apiKey, YouTube youtube) {
		API_KEY = apiKey;
		this.youtube = youtube;
	}

	private static final long DEFAULT_NUMBER_OF_VIDEOS_RETURNED = 25;

	public SearchListResponse searchVideos(String queryTerm, long maxResults) {
		try {

			// Define the API request for retrieving search results.
			YouTube.Search.List search = youtube.search().list("id,snippet");

			// Set your developer key from the {{ Google Cloud Console }} for
			// non-authenticated requests. See:
			// {{ https://cloud.google.com/console }}
			search.setKey(API_KEY);
			search.setQ(queryTerm);

			// Restrict the search results to only include videos. See:
			// https://developers.google.com/youtube/v3/docs/search/list#type
			search.setType("video");

			// To increase efficiency, only retrieve the fields that the
			// application uses.
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(maxResults);

			// Call the API and return results.
			return search.execute();
		} catch (GoogleJsonResponseException e) {
			System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());
		} catch (IOException e) {
			System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public SearchListResponse searchVideos(String queryTerm) {
		return searchVideos(queryTerm, DEFAULT_NUMBER_OF_VIDEOS_RETURNED);
	}
}
