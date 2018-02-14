package net.mpolonioli.musicdownloaderyt.gui;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import net.mpolonioli.musicdownloaderyt.playlist.Playlist;
import net.mpolonioli.musicdownloaderyt.playlist.PlaylistManager;

public class PlaylistFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private static final File DEFAULT_PLAYLIST_FOLDER = new File("./playlists");
	private JList<Object> selectPlaylist;
	private final JButton selectFolder;
	private final JButton refreshPlaylists;
	private final JFileChooser folderPlaylist;

	public PlaylistFrame() {
		
		folderPlaylist = new JFileChooser(DEFAULT_PLAYLIST_FOLDER);
		folderPlaylist.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		selectFolder = new JButton("Playlist Folder");
		selectFolder.addActionListener(e -> folderPlaylist.showOpenDialog(this));
		
		refreshPlaylists = new JButton("Refresh");
		refreshPlaylists.addActionListener(e -> refreshPlaylists());
		
		selectPlaylist = new JList<>(PlaylistManager.getPlaylistsInFolder(DEFAULT_PLAYLIST_FOLDER).toArray());
		selectPlaylist.setVisible(true);
				
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		JPanel button = new JPanel();

		button.add(selectPlaylist);
		button.add(selectFolder);
		button.add(refreshPlaylists);

		center.add(button);

		setLayout(new BorderLayout());

		add(center, BorderLayout.CENTER);

		setResizable(false);
		pack();
	}
	
	public File getPlaylistFolder() {
		return folderPlaylist.getSelectedFile();
	}

	public Playlist getSelectedPlaylist() {
		return (Playlist) selectPlaylist.getSelectedValue();
	}
	
	public void refreshPlaylists() {
		File playlistFolder = getPlaylistFolder();
		if(playlistFolder != null)
		{
			selectPlaylist = new JList<>(PlaylistManager.getPlaylistsInFolder(playlistFolder).toArray());
		}else
		{
			selectPlaylist = new JList<>(PlaylistManager.getPlaylistsInFolder(DEFAULT_PLAYLIST_FOLDER).toArray());
		}
	}
}
