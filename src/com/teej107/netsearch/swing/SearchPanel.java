package com.teej107.netsearch.swing;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.SOUTH;
import static javax.swing.SpringLayout.WEST;

/**
 * Created by teej107 on 7/31/2017.
 */
public class SearchPanel extends JPanel
{
	private SpringLayout layout;
	private SearchTextField searchTextField;

	public SearchPanel(boolean fullscreen)
	{
		this.layout = new SpringLayout();
		setLayout(layout);
		this.searchTextField = new SearchTextField();

		setFullscreen(fullscreen);

		add(searchTextField);
	}

	public void setFullscreen(boolean b)
	{
		setOpaque(!b);
		Dimension dim = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
		int textFieldHeight = 60;
		int textFieldWidth = dim.width / 3;
		int northPad = b ? (dim.height / 2) - (textFieldHeight / 2) : 0;
		int southPad = b ? (dim.height / 2) + (textFieldHeight / 2) : 0;
		int eastPad = b ? (dim.width / 2) + (textFieldWidth / 2) : 0;
		int westPad = b ? (dim.width / 2) - (textFieldWidth / 2) : 0;
		layout.putConstraint(NORTH, searchTextField, northPad, NORTH, this);
		layout.putConstraint(SOUTH, searchTextField, southPad, SOUTH, this);
		layout.putConstraint(EAST, searchTextField, eastPad, EAST, this);
		layout.putConstraint(WEST, searchTextField, westPad, WEST, this);
	}
}
