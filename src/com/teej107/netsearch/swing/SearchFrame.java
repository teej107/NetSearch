package com.teej107.netsearch.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Created by teej107 on 7/31/2017.
 */
public class SearchFrame extends JFrame
{
	private SearchPanel searchPanel;
	private boolean fullscreen;

	public SearchFrame(boolean fullscreen)
	{
		setUndecorated(true);
		this.searchPanel = new SearchPanel(fullscreen);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setFullscreen(fullscreen);

		setContentPane(searchPanel);
	}

	public Rectangle getScreenSize()
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	}

	public void setFullscreen(boolean b)
	{
		Rectangle bounds = getScreenSize();
		if(b)
		{
			setSize(bounds.getSize());
			setLocation(bounds.getLocation());
			setBackground(new Color(0, 0, 0, 0.5f));
		}
		else
		{
			Dimension screenSize = bounds.getSize();
			setSize(new Dimension(screenSize.width / 3, 60));
			setLocation((screenSize.width / 2) - (getWidth() / 2), (screenSize.height / 2) - (getHeight() / 2));
			setBackground(new Color(0, 0, 0, 0));
		}
		searchPanel.setFullscreen(b);
		this.fullscreen = b;
	}

	public boolean isFullscreen()
	{
		return fullscreen;
	}
}
