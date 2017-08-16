package com.teej107.netsearch;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by teej107 on 7/31/2017.
 */
public class Main
{
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{

		}
		if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
		{
			JOptionPane.showInputDialog(null, "This application is not supported on this system");
			System.exit(0);
			return;
		}

		try
		{
			Application application = new Application();
			//Don't wont to show GUI at startup in case program is started at login unless a keyboard shortcut is not defined
			if(!application.hasKeyboardShortcut())
			{
				application.createAndShowGui();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			JOptionPane.showInputDialog(null, e.getMessage());
		}
	}
}
