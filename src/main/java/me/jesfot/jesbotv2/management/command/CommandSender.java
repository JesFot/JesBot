package me.jesfot.jesbotv2.management.command;

import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public interface CommandSender
{
	public String getName();
	
	public void sendMessage(String message);
	
	public boolean hasPermission(Permissions permission);
	
	public IUser getDiscordUser();
	
	public boolean isConsole();
}
