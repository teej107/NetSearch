package com.teej107.netsearch.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

import static javax.swing.SpringLayout.*;

/**
 * Created by teej107 on 8/3/2017.
 */
public class ShortcutDialog extends JDialog implements KeyListener, MouseListener, FocusListener
{
	private static ShortcutDialog DIALOG;

	private SpringLayout layout;
	private JPanel panel;
	private JTextField textField;
	private JButton ok, cancel;
	private Set<Integer> keyPresses;
	private Component exitComponent;

	private ShortcutDialog()
	{
		setTitle("Set shortcut");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.keyPresses = new HashSet<>();
		this.layout = new SpringLayout();
		this.panel = new JPanel()
		{
			@Override
			public Insets getInsets()
			{
				return new Insets(5, 5, 5, 5);
			}

			@Override
			public Component add(Component comp)
			{
				comp.setFocusTraversalKeysEnabled(false);
				return super.add(comp);
			}
		};
		this.panel.setLayout(layout);

		this.textField = new JTextField();
		this.textField.addFocusListener(this);
		this.textField.addKeyListener(this);
		this.ok = new JButton("Done");
		this.ok.addMouseListener(this);
		this.cancel = new JButton("Cancel");
		this.cancel.addMouseListener(this);

		layout.putConstraint(NORTH, textField, 0, NORTH, panel);
		layout.putConstraint(WEST, textField, 0, WEST, panel);
		layout.putConstraint(EAST, textField, 0, EAST, panel);
		layout.putConstraint(NORTH, ok, 5, SOUTH, textField);
		layout.putConstraint(EAST, ok, 0, EAST, panel);
		layout.putConstraint(NORTH, cancel, 0, NORTH, ok);
		layout.putConstraint(EAST, cancel, -5, WEST, ok);

		panel.add(textField);
		panel.add(ok);
		panel.add(cancel);

		setMinimumSize(new Dimension(300, 100));
		setContentPane(panel);
	}

	public static boolean isListening()
	{
		return DIALOG == null ? false : DIALOG.isVisible();
	}

	public static int[] getKeyCodes(Component c)
	{
		if(DIALOG == null)
		{
			DIALOG = new ShortcutDialog();
		}
		DIALOG.setLocationRelativeTo(c);
		DIALOG.exitComponent = null;

		DIALOG.setModalityType(ModalityType.APPLICATION_MODAL);
		DIALOG.setVisible(true);

		if(DIALOG.exitComponent == DIALOG.ok)
		{
			int[] codes = new int[DIALOG.keyPresses.size()];
			int index = 0;
			for (Integer i : DIALOG.keyPresses)
			{
				codes[index++] = i;
			}
			return codes;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		e.consume();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(keyPresses.add(e.getKeyCode()))
		{
			String text = textField.getText();
			textField.setText(text + (text.length() == 0 ? "" : " + ") + KeyEvent.getKeyText(e.getKeyCode()));
		}
		e.consume();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		ok.grabFocus();
		e.consume();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if(e.getComponent() == ok)
		{
			exitComponent = ok;
		}
		setVisible(false);
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
	public void focusGained(FocusEvent e)
	{
		keyPresses.clear();
		this.textField.setText("");
	}

	@Override
	public void focusLost(FocusEvent e)
	{

	}
}
