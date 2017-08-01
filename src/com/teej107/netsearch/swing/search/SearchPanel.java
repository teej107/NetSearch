package com.teej107.netsearch.swing.search;

import com.teej107.netsearch.Application;
import com.teej107.netsearch.swing.CloseButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

import static javax.swing.SpringLayout.*;

/**
 * Created by teej107 on 7/31/2017.
 */
public class SearchPanel extends JPanel implements MouseListener
{
	private String title;
	private boolean fullscreen;
	private SpringLayout layout;
	private SearchTextField searchTextField;
	private CloseButton closeButton;
	private int textFieldHeight, textFieldWidth, padding;
	private Font font;

	public SearchPanel(SearchTextField searchTextField, String title, boolean fullscreen, int textFieldHeight)
	{
		this.title = title;
		this.textFieldHeight = textFieldHeight;
		this.textFieldWidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize().width / 3;
		this.padding = 5;
		this.layout = new SpringLayout();
		setLayout(layout);
		this.searchTextField = searchTextField;
		this.searchTextField.setPreferredSize(new Dimension(textFieldWidth, textFieldHeight));
		this.closeButton = new CloseButton();
		this.closeButton.addMouseListener(this);

		setOpaque(false);

		add(searchTextField);
		add(closeButton);

		setFullscreen(fullscreen);
	}

	private void calculateTitleFont()
	{
		font = getFont().deriveFont((float) Application.calculateFontSize(getFontMetrics(getFont()), closeButton.getSetSize()));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(!fullscreen)
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Dimension size = getSize();
			g2d.setColor(new Color(10, 10, 10, 230));
			Insets insets = searchTextField.getInsets();
			g2d.fill(new RoundRectangle2D.Double(insets.left / 2, 0, size.getWidth() - insets.left, size.getHeight(), 10, 10));
			g2d.setColor(new Color(250, 250, 250));
			g2d.setFont(font);
			FontMetrics metrics = getFontMetrics(font);
			g2d.drawString(title, padding, metrics.getHeight() - padding);
		}
	}

	public void setFullscreen(boolean b)
	{
		Dimension dim = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
		if (b)
		{
			layout.putConstraint(NORTH, searchTextField, (dim.height / 2) - (textFieldHeight / 2), NORTH, this);
			layout.putConstraint(SOUTH, searchTextField, textFieldHeight, NORTH, searchTextField);
			layout.putConstraint(WEST, searchTextField, (dim.width / 2) - (textFieldWidth / 2), WEST, this);
			layout.putConstraint(EAST, searchTextField, textFieldWidth, WEST, searchTextField);
			closeButton.setSize(Math.min(60, Math.max(textFieldHeight, 30)));
		}
		else
		{
			layout.putConstraint(SOUTH, searchTextField, 0, SOUTH, this);
			layout.putConstraint(EAST, searchTextField, 0, EAST, this);
			layout.putConstraint(WEST, searchTextField, 0, WEST, this);
			layout.putConstraint(NORTH, searchTextField, -textFieldHeight, SOUTH, searchTextField);
			closeButton.setSize(30);
			if(font == null)
			{
				calculateTitleFont();
			}
		}

		layout.putConstraint(EAST, closeButton, -padding, EAST, searchTextField);
		layout.putConstraint(SOUTH, closeButton, b ? -padding : -padding / 2, NORTH, searchTextField);
		setPreferredSize(new Dimension(textFieldWidth, searchTextField.getPreferredSize().height + closeButton.getSetSize() + padding));

		this.fullscreen = b;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		System.exit(0);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		SwingUtilities.getWindowAncestor(this).setOpacity(0);
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
