package me.jesfot.jesbotv2.main;

import java.io.IOException;

import me.jesfot.jesbotv2.JesBot;
import me.jesfot.jesbotv2.Statics;

public class Launcher
{
	private JesBot bot;
	
	public Launcher() throws IOException
	{
		this.bot = new JesBot();
	}
	
	public void launchJesBot()
	{
		bot.getLogger().info("Enabled JesBot version " + Statics.VERSION);
		bot.init();
		bot.start();
	}
	
	public void readConsole() throws IOException
	{
		String line;
		
		while ((line = this.bot.getConsoleReader().readLine(">")) != null)
		{
			this.bot.getConsoleCommandHandler().executeCommand(null, line);
		}
	}
}
