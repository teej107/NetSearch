package com.teej107.netsearch.swing.search;

import com.teej107.netsearch.swing.CloseButton;

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
	private CloseButton closeButton;

	public SearchPanel(boolean fullscreen)
	{
		this.layout = new SpringLayout();
		setLayout(layout);
		this.searchTextField = new SearchTextField();
		this.closeButton = new CloseButton(60);

		setFullscreen(fullscreen);

		add(searchTextField);
		add(closeButton);
	}

	public void setFullscreen(boolean b)
	{
		setOpaque(!b);
		Dimension dim = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
		int textFieldHeight = 60;
		int textFieldWidth = dim.width / 3;
		int northPad = b ? (dim.height / 2) - (textFieldHeight / 2) : 0;
		int westPad = b ? (dim.width / 2) - (textFieldWidth / 2) : 0;
		layout.putConstraint(NORTH, searchTextField, northPad, NORTH, this);
		layout.putConstraint(SOUTH, searchTextField, textFieldHeight, NORTH, searchTextField);
		layout.putConstraint(WEST, searchTextField, westPad, WEST, this);
		layout.putConstraint(EAST, searchTextField, textFieldWidth, WEST, searchTextField);

		layout.putConstraint(EAST, closeButton, 0, EAST, searchTextField);
		layout.putConstraint(SOUTH, closeButton, -5, NORTH, searchTextField);
	}
}
