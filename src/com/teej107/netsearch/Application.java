package com.teej107.netsearch;

import com.teej107.netsearch.io.SearchLoader;
import com.teej107.netsearch.io.SearchPreferences;
import com.teej107.netsearch.swing.search.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Set;
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
	private Desktop desktop;

	public Application() throws IOException
	{
		this.directory = Paths.get(System.getProperty("user.home"), getName());
		Files.createDirectories(directory);
		this.searchPreferences = new SearchPreferences(getName());
		this.searchLoader = new SearchLoader(directory);
		this.desktop = Desktop.getDesktop();
	}

	public void createAndShowGui()
	{
		SwingUtilities.invokeLater(() ->
		{
			SearchTextField searchTextField = new SearchTextField();
			searchTextField.addActionListener(actionEvent -> search(searchTextField.getText()));
			SearchPanel searchPanel = new SearchPanel(searchTextField, getName(), searchPreferences.isFullscreen(), 60);
			searchFrame = new SearchFrame(searchPanel, getName(), searchPreferences.isFullscreen());
			searchFrame.setVisible(true);
		});
	}

	public String getName()
	{
		return "Net Search";
	}

	public void search(String s)
	{
		s = s.trim();
		if (s.length() > 0)
		{
			String[] args = s.split(" ");
			SearchExpression searchExpression = searchLoader.getSearchExpression("google");
			if (args.length > 1)
			{
				Set<String> names = searchLoader.getSearchExpressionNames();
				if (names.contains(args[0].toLowerCase()))
				{
					searchExpression = searchLoader.getSearchExpression(args[0]);
					args = Arrays.copyOfRange(args, 1, args.length - 1);
				}
				else if (names.contains(args[args.length - 1].toLowerCase()))
				{
					searchExpression = searchLoader.getSearchExpression(args[args.length - 1]);
					args = Arrays.copyOfRange(args, 0, args.length - 2);
				}
			}
			if (searchExpression != null)
			{
				try
				{
					StringBuilder sb = new StringBuilder();
					for(String s1 : args)
					{
						sb.append(s1).append(' ');
					}
					sb.setLength(sb.length() - 1);
					parseArgs(searchExpression, sb.toString());
				}
				catch (IOException | URISyntaxException e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		}
	}

	private void parseArgs(SearchExpression expression, String args) throws IOException, URISyntaxException
	{
		String format = expression.format(args);
		if (format == null)
			return;
		URL url = new URL(format);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(),
				url.getRef());
		desktop.browse(uri);
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent event)
	{
		switch (event.getKey())
		{
			case SearchPreferences.FULLSCREEN:
				if (searchFrame != null)
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
