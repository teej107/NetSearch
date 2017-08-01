package com.teej107.netsearch;

import com.teej107.netsearch.io.SearchLoader;
import com.teej107.netsearch.io.SearchPreferences;
import com.teej107.netsearch.swing.search.SearchFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

/**
 * Created by teej107 on 7/31/2017.
 */
public class Application implements PreferenceChangeListener
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
			searchFrame = new SearchFrame(getName(), searchPreferences.isFullscreen());
			searchFrame.setVisible(true);
		});
	}

	public String getName()
	{
		return "Net Search";
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent event)
	{
		switch (event.getKey())
		{
			case SearchPreferences.FULLSCREEN:
				if(searchFrame != null)
				{
					searchFrame.setFullscreen(searchPreferences.isFullscreen());
				}
				break;
		}
	}

	public static int calculateFontSize(FontMetrics metrics, int height)
	{
		return (int) ((height / Math.max(metrics.getHeight(), 0.1)) * metrics.getFont().getSize());
	}
}
