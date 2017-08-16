package com.teej107.netsearch.swing.search;

import com.teej107.netsearch.Application;
import com.teej107.netsearch.GaussianBlur;
import com.teej107.netsearch.io.GoogleSearchSuggestion;
import com.teej107.netsearch.swing.CloseButton;
import com.teej107.netsearch.swing.StringJList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import static javax.swing.SpringLayout.*;

/**
 * Created by teej107 on 7/31/2017.
 */
public class SearchPanel extends JPanel implements DocumentListener, KeyListener
{
	private BufferedImage blurredImage;
	private GaussianBlur blur;
	private String title;
	private boolean fullscreen;
	private SpringLayout layout;
	private SearchTextField searchTextField;
	private CloseButton closeButton;
	private StringJList suggestionList;
	private int textFieldHeight, textFieldWidth, padding;
	private Font font;
	private Future<?> future;

	public SearchPanel(SearchTextField searchTextField, String title, boolean fullscreen, int textFieldHeight)
	{
		this.title = title;
		this.textFieldHeight = textFieldHeight;
		this.textFieldWidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize().width / 3;
		this.padding = 5;
		this.layout = new SpringLayout();
		this.searchTextField = searchTextField;
		this.searchTextField.addKeyListener(this);
		this.searchTextField.setPreferredSize(new Dimension(textFieldWidth, textFieldHeight));
		this.searchTextField.getDocument().addDocumentListener(this);
		this.closeButton = new CloseButton();
		this.suggestionList = new StringJList(searchTextField.getBorder(), true, Math.max(20, textFieldHeight / 2));
		this.suggestionList.setOpaque(false);
		this.suggestionList.setBorder(null);
		this.blur = new GaussianBlur(15, 7, Color.WHITE);

		setLayout(layout);
		setOpaque(false);

		add(searchTextField);
		add(closeButton);
		add(suggestionList);

		setFullscreen(fullscreen);
		setPreferredSize(new Dimension(textFieldWidth, searchTextField.getPreferredSize().height + closeButton.getSetSize() + padding));
	}

	public void setBlurred(BufferedImage image)
	{
		this.blurredImage = image == null ? null : blur.blur(image);
	}

	public CloseButton getCloseButton()
	{
		return closeButton;
	}

	public StringJList getSuggestionList()
	{
		return suggestionList;
	}

	private void calculateTitleFont()
	{
		font = getFont().deriveFont((float) Application.calculateFontSize(getFontMetrics(getFont()), closeButton.getSetSize()));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Dimension size = getSize();
		if (fullscreen)
		{
			if (blurredImage != null)
			{
				g.drawImage(blurredImage, 0, 0, null);
				g.setColor(SwingUtilities.getWindowAncestor(this).getBackground());
				g.fillRect(0, 0, size.width, size.height);
			}
		}
		else
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Color titleBarColor = new Color(10, 10, 10, 230);
			Color gradientColor = new Color(35, 35, 35, 240);
			int gradientY = closeButton.getSetSize();
			Paint gradient = new GradientPaint(0, 0, titleBarColor, 0, gradientY, gradientColor);
			g2d.setPaint(gradient);
			g2d.fill(new RoundRectangle2D.Double(0, 0, size.getWidth(), size.getHeight(), 10, 10));
			g2d.setColor(new Color(250, 250, 250));
			g2d.setFont(font);
			FontMetrics metrics = getFontMetrics(font);
			g2d.drawString(title, padding, metrics.getHeight() - (int) Math.ceil(padding / 2.0));
		}
	}

	public void setFullscreen(boolean b)
	{
		this.fullscreen = b;
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
			int closeButtonSize = 30;
			layout.putConstraint(NORTH, searchTextField, closeButtonSize + padding, NORTH, this);
			layout.putConstraint(EAST, searchTextField, 0, EAST, this);
			layout.putConstraint(WEST, searchTextField, 0, WEST, this);
			layout.putConstraint(SOUTH, searchTextField, textFieldHeight, NORTH, searchTextField);
			closeButton.setSize(closeButtonSize);
			if (font == null)
			{
				calculateTitleFont();
			}
		}

		layout.putConstraint(EAST, closeButton, -padding, EAST, searchTextField);
		layout.putConstraint(SOUTH, closeButton, b ? -padding : -padding / 2, NORTH, searchTextField);

		layout.putConstraint(NORTH, suggestionList, b ? padding : 1, SOUTH, searchTextField);
		layout.putConstraint(WEST, suggestionList, 0, WEST, searchTextField);
		layout.putConstraint(EAST, suggestionList, textFieldWidth, WEST, suggestionList);
		layout.putConstraint(SOUTH, suggestionList, -padding, SOUTH, this);
	}

	private void handleDocumentEvent(DocumentEvent e)
	{
		final String text = searchTextField.getText();
		if (future != null)
			future.cancel(true);

		if (text.trim().length() == 0)
		{
			suggestionList.setData(null);
		}
		else
		{
			this.future = GoogleSearchSuggestion.getThreadService().submit(() ->
			{
				try
				{
					List<String> list = GoogleSearchSuggestion.getResults(text);
					SwingUtilities.invokeLater(() -> suggestionList.setData(future.isCancelled() ? null : list));
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			});
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		handleDocumentEvent(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
		handleDocumentEvent(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{

	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_KP_DOWN || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
			moveSelection(true);
		else if (e.getKeyCode() == KeyEvent.VK_KP_UP || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_PAGE_UP)
			moveSelection(false);
	}

	@Override
	public void keyReleased(KeyEvent e)
	{

	}

	private void moveSelection(boolean down)
	{
		if (suggestionList.getMaxIndex() == -1)
			return;

		searchTextField.getDocument().removeDocumentListener(this);
		int selectedIndex = suggestionList.getSelectedIndex() + (down ? 1 : -1);
		if (selectedIndex > suggestionList.getMaxIndex())
		{
			suggestionList.setSelectedIndex(0);
		}
		else if (selectedIndex < 0)
		{
			suggestionList.setSelectedIndex(suggestionList.getMaxIndex());
		}
		else
		{
			suggestionList.setSelectedIndex(selectedIndex);
		}
		searchTextField.setText(suggestionList.getSelectedString());
		searchTextField.getDocument().addDocumentListener(this);
	}
}
