package net.mpolonioli.musicdownloaderyt;

import java.io.File;

import javax.swing.JOptionPane;

import net.mpolonioli.musicdownloaderyt.gui.DownloadFrame;
import net.mpolonioli.musicdownloaderyt.gui.PlaylistFrame;
import net.mpolonioli.musicdownloaderyt.playlist.Playlist;
public class AppController {
	final DownloadFrame downloadFrame;
	final PlaylistFrame playlistFrame;
	
	public AppController() {
		downloadFrame = new DownloadFrame(this);
		playlistFrame = new PlaylistFrame();
	}
	
	public void openDownloadFrame() {
		downloadFrame.setVisible(true);
	}

	public void downloadAction() {
		File dest = downloadFrame.getDestinationFolder();
		Playlist playlist = downloadFrame.getPlaylist();
		if(playlist != null && dest != null)
		{
			MusicDownloader downloader = new MusicDownloader("youtube-dl");
			downloader.downloadPlaylist(playlist, dest);
		}else
		{
			String message;
			if(playlist == null) 
			{
				message = "Please select a playlist first.";
			}else
			{
				message = "Please select a destination folder first.";
			}
			JOptionPane.showMessageDialog(this.downloadFrame, message);
		}
	}
	
	public static void main(String[] args) {
		AppController app = new AppController();
		app.openDownloadFrame();
	}

}
