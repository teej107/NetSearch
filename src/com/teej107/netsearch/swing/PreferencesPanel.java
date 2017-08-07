package com.teej107.netsearch.swing;

import com.teej107.netsearch.io.SearchPreferences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by teej107 on 7/31/17.
 */
public class PreferencesPanel extends JPanel implements MouseListener
{
	private SearchPreferences preferences;
	private JCheckBox fullscreenCheckbox, alwaysOnTopCheckbox, blurCheckbox;
	private JLabel shortcutLabel;
	private JButton exit;

	public PreferencesPanel(SearchPreferences preferences)
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.preferences = preferences;
		this.alwaysOnTopCheckbox = new JCheckBox("Always on top", preferences.isAlwaysOnTop());
		this.alwaysOnTopCheckbox.setEnabled(!preferences.isFullscreen());
		this.blurCheckbox = new JCheckBox("Blur Background", preferences.isBlurred());
		this.blurCheckbox.setEnabled(preferences.isFullscreen());
		this.fullscreenCheckbox = new JCheckBox("Fullscreen", preferences.isFullscreen());

		this.alwaysOnTopCheckbox.addMouseListener(this);
		this.blurCheckbox.addMouseListener(this);
		this.fullscreenCheckbox.addMouseListener(this);

		this.shortcutLabel = new JLabel();
		this.shortcutLabel.setBorder(BorderFactory.createEtchedBorder());
		this.shortcutLabel.setForeground(Color.BLACK);
		this.shortcutLabel.addMouseListener(this);
		updateShortcutLabel(preferences.getKeyShortcut());

		this.exit = new JButton("Exit");
		this.exit.addMouseListener(this);

		add(fullscreenCheckbox);
		add(alwaysOnTopCheckbox);
		add(blurCheckbox);
		add(shortcutLabel);
		add(Box.createVerticalStrut(5));
		add(exit);

		setPreferredSize(new Dimension(300, 250));
	}

	@Override
	public Component add(Component comp)
	{
		super.add(Box.createVerticalStrut(15));
		comp.setFocusable(false);
		return super.add(comp);
	}

	private void updateShortcutLabel(int[] shortcuts)
	{
		if (shortcuts.length == 0)
		{
			shortcutLabel.setText("<Shortcut not set>");
		}
		else
		{
			StringBuilder sb = new StringBuilder();
			for (int i : shortcuts)
			{
				if (sb.length() != 0)
				{
					sb.append(" ");
				}
				sb.append(KeyEvent.getKeyText(i));
			}
			shortcutLabel.setText(sb.toString());
		}
	}

	@Override
	public Insets getInsets()
	{
		return new Insets(5, 5, 5, 5);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getComponent() == alwaysOnTopCheckbox)
		{
			preferences.setAlwaysOnTop(alwaysOnTopCheckbox.isSelected());
		}
		else if (e.getComponent() == blurCheckbox)
		{
			preferences.setBlur(blurCheckbox.isSelected());
		}
		else if (e.getComponent() == fullscreenCheckbox)
		{
			preferences.setFullscreen(fullscreenCheckbox.isSelected());
			alwaysOnTopCheckbox.setEnabled(!preferences.isFullscreen());
			blurCheckbox.setEnabled(preferences.isFullscreen());
		}
		else if(e.getComponent() == shortcutLabel)
		{
			int[] shortcut = ShortcutDialog.getKeyCodes(this);
			if(shortcut != null)
			{
				preferences.setKeyShortcut(shortcut);
				updateShortcutLabel(shortcut);
			}
		}
		else if(e.getComponent() == exit)
		{
			System.exit(0);
		}
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
		if (e.getComponent() == shortcutLabel)
		{
			shortcutLabel.setForeground(Color.BLUE);
		}
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		if(e.getComponent() == shortcutLabel)
		{
			shortcutLabel.setForeground(Color.BLACK);
		}
	}
}
