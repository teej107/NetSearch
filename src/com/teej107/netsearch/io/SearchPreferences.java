package com.teej107.netsearch.io;

import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

/**
 * Created by teej107 on 7/31/2017.
 */
public class SearchPreferences
{
	public static final String FULLSCREEN = "fullscreen";

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
}
