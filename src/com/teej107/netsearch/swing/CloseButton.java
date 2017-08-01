package com.teej107.netsearch.swing;

import com.teej107.netsearch.Application;
import sun.font.FontDesignMetrics;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by teej107 on 7/31/17.
 */
public class CloseButton extends JComponent
{
	private static final Color BODY = new Color(1f, 1f, 1f, 0.5f);

	public CloseButton(int size)
	{
		setPreferredSize(new Dimension(size, size));
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		float fontSize = Application.calculateFontSize(SwingUtilities2.getFontMetrics(this, font), Math.max(size - 10, size));
		setFont(font.deriveFont(fontSize));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(BODY);
		Dimension preferredSize = getPreferredSize();
		g.fillOval(0, 0, preferredSize.width, preferredSize.height);

		final String drawString = "X";
		FontMetrics fontMetrics = getFontMetrics(getFont());
		Dimension rect = fontMetrics.getStringBounds(drawString, g).getBounds().getSize();
		g.setColor(Color.BLACK);
		g.setFont(getFont());
		g.drawString(drawString, (preferredSize.width / 2) - (rect.width / 2), (preferredSize.height / 2) + (int)(rect.height / 2.5));

	}
}
