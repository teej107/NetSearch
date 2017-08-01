package com.teej107.netsearch;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;

/**
 * Created by teej107 on 7/31/2017.
 */
public class Main
{
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getLookAndFeel());
		}
		catch (Exception e)
		{

		}
		if (!Desktop.isDesktopSupported())
		{
			JOptionPane.showInputDialog(null, "This application is not supported on this system");
			System.exit(0);
			return;
		}
		try
		{
			Application application = new Application();
			application.createAndShowGui();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			JOptionPane.showInputDialog(null, e.getMessage());
		}
	}

	public static void test()
	{
		Desktop desktop = Desktop.getDesktop();
		String s = "https://www.google.com/search?q=definitely not bees";
		if (s == null)
		{
			JOptionPane.showInputDialog(null, "Invalid search expression");
		}
		else
		{
			try
			{
				URL url = new URL(s);
				URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(),
						url.getRef());
				desktop.browse(uri);
			}
			catch (IOException | URISyntaxException e)
			{
				e.printStackTrace();
			}
		}
	}
}
