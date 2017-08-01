package com.teej107.netsearch.swing.search;

import com.teej107.netsearch.Application;

import javax.swing.*;
import java.awt.*;

/**
 * Created by teej107 on 7/31/2017.
 */
public class SearchTextField extends JTextField
{
	private int fontSize;

	@Override
	public void invalidate()
	{
		fontSize = 0;
		super.invalidate();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(fontSize == 0)
		{
			FontMetrics metrics = getFontMetrics(getFont());
			fontSize = Application.calculateFontSize(metrics, getHeight());
			setFont(metrics.getFont().deriveFont((float) fontSize));
		}
	}
}
