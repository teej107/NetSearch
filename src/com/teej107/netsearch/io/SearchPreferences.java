package com.teej107.netsearch.io;

import java.awt.*;
import java.nio.*;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

/**
 * Created by teej107 on 7/31/2017.
 */
public class SearchPreferences
{
	public static final String FULLSCREEN = "fullscreen";
	public static final String ALWAYS_ON_TOP = "always on top";
	public static final String BLUR = "blur";
	public static final String WINDOW_X = "window x";
	public static final String WINDOW_Y = "window y";
	public static final String KEY_SHORTCUT = "key shortcut";

	private Preferences preferences;

	public SearchPreferences(String node)
	{
		this.preferences = Preferences.userRoot().node(node);
	}

	public void registerPreferenceChangeListener(PreferenceChangeListener listener)
	{
		preferences.addPreferenceChangeListener(listener);
	}

	public boolean isFullscreen()
	{
		return preferences.getBoolean(FULLSCREEN, true);
	}

	public void setFullscreen(boolean b)
	{
		preferences.putBoolean(FULLSCREEN, b);
	}

	public boolean isAlwaysOnTop()
	{
		return preferences.getBoolean(ALWAYS_ON_TOP, false);
	}

	public void setAlwaysOnTop(boolean b)
	{
		preferences.putBoolean(ALWAYS_ON_TOP, b);
	}

	public Point getWindowLocation(Window window)
	{
		int x = preferences.getInt(WINDOW_X, Integer.MAX_VALUE);
		int y = preferences.getInt(WINDOW_Y, Integer.MAX_VALUE);
		if(x == Integer.MAX_VALUE || y == Integer.MAX_VALUE)
		{
			Dimension dim = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
			x = (dim.width / 2) - (window.getWidth() / 2);
			y = (dim.height / 2) - (window.getHeight() / 2);
		}
		return new Point(x, y);
	}

	public void setWindowLocation(Point point)
	{
		preferences.putInt(WINDOW_X, point.x);
		preferences.putInt(WINDOW_Y, point.y);
	}

	public boolean isBlurred()
	{
		return preferences.getBoolean(BLUR, false);
	}

	public void setBlur(boolean b)
	{
		preferences.putBoolean(BLUR, b);
	}

	public void setKeyShortcut(int[] keyCodes)
	{
		ByteBuffer byteBuffer = ByteBuffer.allocate(keyCodes.length * 4).order(ByteOrder.BIG_ENDIAN);
		byteBuffer.asIntBuffer().put(keyCodes);
		preferences.putByteArray(KEY_SHORTCUT, byteBuffer.array());
	}

	public int[] getKeyShortcut()
	{
		byte[] data = preferences.getByteArray(KEY_SHORTCUT, null);
		if(data == null)
			return new int[0];
		IntBuffer intBuf = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
		int[] keyCodes = new int[intBuf.remaining()];
		intBuf.get(keyCodes);
		return keyCodes;
	}
}
