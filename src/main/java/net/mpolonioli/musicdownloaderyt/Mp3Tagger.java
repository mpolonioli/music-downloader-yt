package net.mpolonioli.musicdownloaderyt;

import java.io.File;
import java.io.IOException;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class Mp3Tagger {

	public static void setID3V1Tag(String source, String dest, String title, String artist, boolean removeSource) 
			throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
		File sourceFile = new File(source);
		Mp3File mp3file = new Mp3File(sourceFile);
		ID3v1 id3v1Tag;
		
		System.out.println("[Mp3Tagger] Tagging song " + sourceFile.getName());
		if (mp3file.hasId3v1Tag()) {
			id3v1Tag =  mp3file.getId3v1Tag();
		} else {
			// mp3 does not have an ID3v1 tag, let's create one.
			id3v1Tag = new ID3v1Tag();
			mp3file.setId3v1Tag(id3v1Tag);
		}
		id3v1Tag.setTitle(title);
		id3v1Tag.setArtist(artist);
		
		System.out.println("[Mp3Tagger] Destination: " + dest);
		mp3file.save(dest);
		
		if(removeSource) {
			System.out.println("[Mp3Tagger] Deleting original file " + source + " (pass removeSource=false to keep)");
			sourceFile.delete();
		}
	}
}
