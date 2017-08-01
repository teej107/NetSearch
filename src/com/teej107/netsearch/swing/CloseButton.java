package com.teej107.netsearch.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by teej107 on 7/31/17.
 */
public class CloseButton extends JComponent implements MouseListener
{
	private static final Color BODY = new Color(1f, 1f, 1f, 0.5f);
	private static final Color HOVER = new Color(1f, 1f, 1f, 0.7f);

	private boolean hover;

	public CloseButton()
	{
		addMouseListener(this);
	}

	public void setSize(int size)
	{
		setPreferredSize(new Dimension(size, size));
	}

	public int getSetSize()
	{
		return getPreferredSize().height;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(hover ? HOVER : BODY);
		Dimension preferredSize = getPreferredSize();
		g.fillOval(0, 0, preferredSize.width, preferredSize.height);

		int xSize = (int) (preferredSize.width / 2.2);
		int offset = (preferredSize.width - xSize) / 2;
		g.setColor(Color.BLACK);
		g.drawLine(offset, offset, preferredSize.width - offset, preferredSize.height - offset);
		g.drawLine(offset, preferredSize.height - offset, preferredSize.width - offset, offset);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e)
	{

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		hover = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		hover = false;
		repaint();
	}
}
