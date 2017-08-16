package com.teej107.netsearch.swing.search;

import com.teej107.netsearch.io.SearchPreferences;
import com.teej107.netsearch.swing.PreferencesPanel;
import com.teej107.netsearch.swing.StringJList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by teej107 on 7/31/2017.
 */
public class SearchFrame extends JDialog implements WindowFocusListener, MouseMotionListener, MouseListener, ChangeListener
{
	private SearchPreferences searchPreferences;
	private SearchPanel searchPanel;
	private boolean fullscreen;
	private Point lastDrag;
	private Robot robot;

	public SearchFrame(SearchPanel searchPanel, String title, SearchPreferences searchPreferences)
	{
		this.searchPanel = searchPanel;
		this.searchPanel.getSuggestionList().setChangeListener(this);
		setTitle(title);
		this.searchPreferences = searchPreferences;
		setUndecorated(true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setFullscreen(searchPreferences.isFullscreen());
		setModalityType(ModalityType.MODELESS);
		setContentPane(searchPanel);
		addWindowFocusListener(this);

		try
		{
			this.robot = new Robot();
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}

	public void click(Component c)
	{
		if (robot != null)
		{
			Point point = c.getLocation();
			SwingUtilities.convertPointToScreen(point, this);
			robot.mouseMove(point.x, point.y);
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		}
	}

	public void setBlurred(boolean b)
	{
		searchPanel.setBlurred(robot != null && fullscreen && b ? robot.createScreenCapture(getBounds()) : null);
	}

	@Override
	public void setVisible(boolean b)
	{
		setBlurred(searchPreferences.isBlurred());
		super.setVisible(b);
	}

	public void setFullscreen(boolean b)
	{
		this.fullscreen = b;
		searchPanel.setFullscreen(b);
		if (b)
		{
			Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			setAlwaysOnTop(false);
			setSize(bounds.getSize());
			setLocation(bounds.getLocation());
			setBackground(new Color(0, 0, 0, 0.5f));
			removeMouseMotionListener(this);
			removeMouseListener(this);
		}
		else
		{
			setAlwaysOnTop(searchPreferences.isAlwaysOnTop());
			setSize(searchPanel.getPreferredSize());
			setLocation(searchPreferences.getWindowLocation(this));
			setBackground(new Color(0, 0, 0, 0));
			addMouseMotionListener(this);
			addMouseListener(this);
		}
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
		Window focusGained = e.getOppositeWindow();
		if (focusGained instanceof RootPaneContainer)
		{
			RootPaneContainer container = (RootPaneContainer) focusGained;
			if (container.getContentPane() instanceof PreferencesPanel)
				return;
		}
		setVisible(false);
		if (!isFullscreen())
		{
			searchPreferences.setWindowLocation(getLocation());
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (!fullscreen)
		{
			if (lastDrag != null)
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

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (!fullscreen)
		{
			StringJList stringJList = (StringJList) e.getSource();
			Dimension size = searchPanel.getPreferredSize();
			setSize(stringJList.isEmpty() ?
					size :
					new Dimension(size.width, size.height + stringJList.getTotalHeight()));
		}
	}
}
