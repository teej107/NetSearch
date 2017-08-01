package com.teej107.netsearch.swing.search;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by teej107 on 7/31/2017.
 */
public class SearchFrame extends JDialog implements WindowFocusListener, MouseMotionListener, MouseListener
{
	private SearchPanel searchPanel;
	private boolean fullscreen;
	private Point lastDrag;

	public SearchFrame(SearchPanel searchPanel, String title, boolean fullscreen)
	{
		setTitle(title);
		setUndecorated(true);
		this.searchPanel = searchPanel;

		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setFullscreen(fullscreen);
		setModalityType(ModalityType.MODELESS);
		setContentPane(searchPanel);
		addWindowFocusListener(this);
	}

	public void setFullscreen(boolean b)
	{
		Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		if(b)
		{
			setSize(bounds.getSize());
			setLocation(bounds.getLocation());
			setBackground(new Color(0, 0, 0, 0.5f));
			removeMouseMotionListener(this);
			removeMouseListener(this);
		}
		else
		{
			Dimension screenSize = bounds.getSize();
			setSize(searchPanel.getPreferredSize());
			setLocation((screenSize.width / 2) - (getWidth() / 2), (screenSize.height / 2) - (getHeight() / 2));
			setBackground(new Color(0, 0, 0, 0));
			addMouseMotionListener(this);
			addMouseListener(this);
		}
		searchPanel.setFullscreen(b);
		this.fullscreen = b;
	}

	public boolean isFullscreen()
	{
		return fullscreen;
	}


	@Override
	public void windowGainedFocus(WindowEvent e)
	{

	}

	@Override
	public void windowLostFocus(WindowEvent e)
	{
		System.exit(0);
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(!fullscreen)
		{
			if(lastDrag != null)
			{
				Point point = e.getLocationOnScreen();
				int x = point.x - lastDrag.x;
				int y = point.y - lastDrag.y;
				Point currentLocation = getLocation();
				currentLocation.translate(x, y);
				setLocation(currentLocation);
			}
			lastDrag = e.getLocationOnScreen();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{

	}

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		lastDrag = null;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{

	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}
}
