package me.jesfot.jesbotv2.main;

import java.io.IOException;

import me.jesfot.jesbotv2.Statics;

public final class Main
{
	public static void main(String[] args)
	{
		try
		{
			Launcher launcher = new Launcher();
			launcher.launchJesBot();
			if (Statics.IS_DEAMON)
			{
				launcher.readConsole();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
