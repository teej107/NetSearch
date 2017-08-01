package com.teej107.netsearch;

import com.teej107.netsearch.io.SearchLoader;
import com.teej107.netsearch.io.SearchPreferences;
import com.teej107.netsearch.swing.SearchFrame;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.*;

/**
 * Created by teej107 on 7/31/2017.
 */
public class Application
{
	private SearchLoader searchLoader;
	private SearchPreferences searchPreferences;
	private SearchFrame searchFrame;
	private Path directory;

	public Application() throws IOException
	{
		this.directory = Paths.get(System.getProperty("user.home"), getName());
		Files.createDirectories(directory);
		this.searchPreferences = new SearchPreferences(getName());
		this.searchLoader = new SearchLoader(directory);
	}

	public void createAndShowGui()
	{
		SwingUtilities.invokeLater(() ->
		{
			searchFrame = new SearchFrame(searchPreferences.isFullscreen());
			searchFrame.setVisible(true);
		});
	}

	public String getName()
	{
		return "Net Search";
	}
}
