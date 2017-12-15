package me.jesfot.jesbotv2.management.command;

import me.jesfot.jesbotv2.JesBot;

import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public final class ConsoleCommandSender implements CommandSender
{
	private static final ConsoleCommandSender INSTANCE = new ConsoleCommandSender();
	
	public static CommandSender getConsoleCommandSender()
	{
		return ConsoleCommandSender.INSTANCE;
	}
	
	private ConsoleCommandSender()
	{
		//
	}
	
	public void sendMessage(String message)
	{
		JesBot.getInstance().getLogger().info(message);
	}
	
	public String getName()
	{
		return "CONSOLE";
	}
	
	public IUser getDiscordUser()
	{
		return JesBot.getInstance().getClient().getOurUser();
	}
	
	public boolean hasPermission(Permissions permission)
	{
		return true;
	}
	
	public boolean isConsole()
	{
		return true;
	}
}
