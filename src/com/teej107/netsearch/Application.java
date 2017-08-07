package com.teej107.netsearch;

import com.teej107.netsearch.io.SearchLoader;
import com.teej107.netsearch.io.SearchPreferences;
import com.teej107.netsearch.swing.PreferencesPanel;
import com.teej107.netsearch.swing.search.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
public class Application implements PreferenceChangeListener, ActionListener
{
	private Image icon;
	private SearchLoader searchLoader;
	private SearchPreferences searchPreferences;
	private SearchFrame searchFrame;
	private PreferencesPanel preferencesPanel;
	private ShortcutListener shortcutListener;
	private Path directory;
	private Desktop desktop;

	public Application() throws IOException
	{
		this.directory = Paths.get(System.getProperty("user.home"), getName());
		Files.createDirectories(directory);
		this.searchPreferences = new SearchPreferences(getName());
		this.searchPreferences.registerPreferenceChangeListener(this);
		this.searchLoader = new SearchLoader(directory);
		this.desktop = Desktop.getDesktop();
		this.shortcutListener = new ShortcutListener();
		this.shortcutListener.setKeyShortcut(searchPreferences.getKeyShortcut());
		this.shortcutListener.addActionListener(this);
		this.icon = ImageIO.read(getClass().getResourceAsStream("/assets/icon.png"));
		if (SystemTray.isSupported())
		{
			SystemTray systemTray = SystemTray.getSystemTray();
			PopupMenu popupMenu = new PopupMenu();
			MenuItem item = new MenuItem("Open");
			item.addActionListener(action -> createAndShowGui());
			popupMenu.add(item);
			item = new MenuItem("Preferences");
			item.addActionListener(action -> showPreferences());
			popupMenu.add(item);
			item = new MenuItem("Exit");
			item.addActionListener(action -> System.exit(0));
			popupMenu.add(item);
			TrayIcon trayIcon = new TrayIcon(icon, getName(), popupMenu);
			trayIcon.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					if (SwingUtilities.isLeftMouseButton(e))
					{
						createAndShowGui();
					}
				}
			});
			trayIcon.setImageAutoSize(true);
			try
			{
				systemTray.add(trayIcon);
			}
			catch (AWTException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void createGUI()
	{
		SearchTextField searchTextField = new SearchTextField();
		searchTextField.requestFocusInWindow();
		searchTextField.addActionListener(actionEvent -> search(searchTextField.getText()));
		SearchPanel searchPanel = new SearchPanel(searchTextField, getName(), searchPreferences.isFullscreen(), 60);
		searchPanel.getSuggestionList().addSelectionListener(e -> search(searchPanel.getSuggestionList().getSelectedString()));
		searchFrame = new SearchFrame(searchPanel, getName(), searchPreferences);
		searchFrame.setAutoRequestFocus(true);
		searchFrame.addWindowFocusListener(new WindowAdapter()
		{
			@Override
			public void windowGainedFocus(WindowEvent e)
			{

			}

			@Override
			public void windowLostFocus(WindowEvent e)
			{
				searchTextField.setText("");
			}
		});
		searchPanel.getCloseButton().addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (SwingUtilities.isRightMouseButton(e))
				{
					showPreferences();
				}
				else if (SwingUtilities.isLeftMouseButton(e))
				{
					searchFrame.setVisible(false);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (SwingUtilities.isLeftMouseButton(e))
				{
					e.consume();
				}
			}
		});
	}

	public void createAndShowGui()
	{
		SwingUtilities.invokeLater(() ->
		{
			if (searchFrame == null)
				createGUI();
			searchFrame.setVisible(true);

			if (searchFrame.isFullscreen() || !searchPreferences.isAlwaysOnTop())
			{
				//Bring focus but don't want it to always be on top
				searchFrame.setAlwaysOnTop(true);
				searchFrame.setAlwaysOnTop(false);
			}
			searchFrame.toFront();
		});
	}

	public void showPreferences()
	{
		SwingUtilities.invokeLater(() ->
		{
			JFrame frame = new JFrame(getName() + " Preferences");
			frame.setAlwaysOnTop(true);
			frame.setIconImage(icon);
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			frame.setResizable(false);
			frame.setLocationByPlatform(true);
			if (preferencesPanel == null)
			{
				this.preferencesPanel = new PreferencesPanel(searchPreferences);
			}
			frame.setContentPane(preferencesPanel);
			frame.setSize(preferencesPanel.getPreferredSize());
			frame.setVisible(true);
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
					args = Arrays.copyOfRange(args, 1, args.length);
				}
				else if (names.contains(args[args.length - 1].toLowerCase()))
				{
					searchExpression = searchLoader.getSearchExpression(args[args.length - 1]);
					args = Arrays.copyOfRange(args, 0, args.length - 1);
				}
			}
			if (searchExpression != null)
			{
				try
				{
					StringBuilder sb = new StringBuilder();
					for (String s1 : args)
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
		SwingUtilities.invokeLater(() ->
		{
			switch (event.getKey())
			{
				case SearchPreferences.FULLSCREEN:
					searchFrame.setFullscreen(searchPreferences.isFullscreen());
					break;
				case SearchPreferences.ALWAYS_ON_TOP:
					searchFrame.setAlwaysOnTop(searchPreferences.isAlwaysOnTop());
					break;
				case SearchPreferences.BLUR:
					boolean blur = searchPreferences.isBlurred();
					if (blur)
					{
						Window window = preferencesPanel == null ? null : SwingUtilities.getWindowAncestor(preferencesPanel);
						if (window == null)
						{
							searchFrame.setBlurred(true);
						}
						else
						{
							window.setVisible(false);
							searchFrame.setVisible(false);
							Timer timer = new Timer(500, e ->
							{
								searchFrame.setBlurred(true);
								searchFrame.setVisible(true);
								window.setVisible(true);
							});
							timer.setRepeats(false);
							timer.start();
						}
					}
					else
					{
						searchFrame.setBlurred(false);
						searchFrame.repaint();
					}
					break;
				case SearchPreferences.KEY_SHORTCUT:
					shortcutListener.setKeyShortcut(searchPreferences.getKeyShortcut());
					break;
			}
		});
	}

	public static int calculateFontSize(FontMetrics metrics, int height)
	{
		return (int) ((height / Math.max(metrics.getHeight(), 0.1)) * metrics.getFont().getSize());
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		createAndShowGui();
	}
}
