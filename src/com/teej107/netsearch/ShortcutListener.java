package com.teej107.netsearch;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.SwingKeyAdapter;

import java.awt.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by teej107 on 8/3/2017.
 */
public class ShortcutListener
{
	public static final String SHORTCUT_PERFORMED = "shortcut performed";

	private Collection<Integer> keyShortcut, currentlyPressed;
	private List<ActionListener> actionListeners;

	public ShortcutListener() throws UnsupportedOperationException
	{
		try
		{
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF);
			logger.setUseParentHandlers(false);
			GlobalScreen.registerNativeHook();
			GlobalScreen.setEventDispatcher(new SwingDispatchService());
			GlobalScreen.addNativeKeyListener(new AWTNativeKeyListener());
			this.keyShortcut = new HashSet<>();
			this.currentlyPressed = new HashSet<>();
			this.actionListeners = new ArrayList<>();
			Runtime.getRuntime().addShutdownHook(new Thread(() ->
			{
				try
				{
					GlobalScreen.unregisterNativeHook();
				}
				catch (NativeHookException e)
				{
					e.printStackTrace();
				}
			}));
		}
		catch (NativeHookException e)
		{
			throw new UnsupportedOperationException(e);
		}
	}

	public void setKeyShortcut(int[] shortcut)
	{
		keyShortcut.clear();
		for (int i : shortcut)
		{
			keyShortcut.add(i);
		}
	}

	public void addActionListener(ActionListener listener)
	{
		actionListeners.add(listener);
	}

	public class AWTNativeKeyListener extends SwingKeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent keyEvent)
		{
			if (keyShortcut.contains(keyEvent.getKeyCode()))
			{
				currentlyPressed.add(keyEvent.getKeyCode());
			}
			if (keyShortcut.size() == currentlyPressed.size())
			{
				ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, SHORTCUT_PERFORMED);
				for (ActionListener listener : actionListeners)
				{
					listener.actionPerformed(event);
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent keyEvent)
		{
			currentlyPressed.remove(keyEvent.getKeyCode());
		}
	}
}
