package net.mpolonioli.musicdownloaderyt.gui;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.mpolonioli.musicdownloaderyt.AppController;
import net.mpolonioli.musicdownloaderyt.playlist.Playlist;

public class DownloadFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final AppController controller;
	private final JButton download;
	private final JButton selectFolder;
	private final JButton selectPlaylist;
	private final JFileChooser folderDestination;
	private final PlaylistFrame playlistFrame;
	
	public DownloadFrame(AppController controller) {
		this.controller = controller;
		
		folderDestination = new JFileChooser();
		folderDestination.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		setTitle("JGUI");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		download = new JButton("Download");
		download.addActionListener(e -> controller.downloadAction());
		
		selectFolder = new JButton("Destination");
		selectFolder.addActionListener(e -> folderDestination.showOpenDialog(this));
		
		playlistFrame = new PlaylistFrame();
		selectPlaylist = new JButton("Playlist");
		selectPlaylist.addActionListener(e -> playlistFrame.setVisible(true));
		
		
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		JPanel button = new JPanel();
		
		button.add(selectPlaylist);
		button.add(selectFolder);
		button.add(download);
		
		center.add(button);
		
		setLayout(new BorderLayout());
		
		add(center, BorderLayout.CENTER);
		
		setResizable(false);
		pack();
	}
	
	public File getDestinationFolder() {
		return folderDestination.getSelectedFile();
	}
	
	public Playlist getPlaylist() {
		return playlistFrame.getSelectedPlaylist();
	}
	
	public void enableData(boolean status) {
		selectPlaylist.setEnabled(status);
		download.setEnabled(status);
		selectFolder.setEnabled(status);
	}

	public AppController getController() {
		return controller;
	}

}
