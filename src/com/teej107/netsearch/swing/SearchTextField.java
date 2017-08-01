package com.teej107.netsearch.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Created by teej107 on 7/31/2017.
 */
public class SearchTextField extends JTextField
{
	private int fontSize;

	private static int calculateFontSize(FontMetrics metrics, int height)
	{
		return (int) ((height / Math.max(metrics.getHeight(), 0.1)) * metrics.getFont().getSize());
	}

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
			fontSize = calculateFontSize(metrics, getHeight());
			setFont(metrics.getFont().deriveFont((float) fontSize));
		}
	}
}
