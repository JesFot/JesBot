package me.jesfot.jesbot.utils;

import java.util.Scanner;

import me.jesfot.jesbot.JesBot;

public class WaitForEnd extends Thread
{
	private JesBot bot;
	
	public WaitForEnd(JesBot jesbot)
	{
		this.bot = jesbot;
	}
	
	@Override
	public void run()
	{
		Scanner in = new Scanner(System.in);
		boolean cont = true;
		while(cont)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			if(in.nextLine().equalsIgnoreCase("stop"))
			{
				this.bot.getClient().idle();
				Utils.safeLogout(this.bot.getClient());
				cont = false;
			}
		}
		in.close();
	}
}