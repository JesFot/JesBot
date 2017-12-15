package me.jesfot.jesbotv2.management.command;

import org.apache.commons.lang3.Validate;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public final class UserCommandSender implements CommandSender
{
	private final IUser sender;
	private final IChannel channel;
	
	public UserCommandSender(IUser user, IChannel place)
	{
		Validate.notNull(user, "user must not be null");
		Validate.notNull(place, "channel must not be null");
		this.sender = user;
		this.channel = place;
	}
	
	public String getName()
	{
		return this.sender.getName();
	}
	
	public IUser getDiscordUser()
	{
		return this.sender;
	}
	
	public void sendMessage(String message)
	{
		this.channel.sendMessage(this.sender.mention(true) + " " + message);
	}
	
	public boolean hasPermission(Permissions permission)
	{
		if (permission == null)
		{
			return true;
		}
		if (this.channel.isPrivate())
		{
			return true;
		}
		return this.channel.getModifiedPermissions(this.sender).contains(permission);
	}
	
	public boolean isConsole()
	{
		return false;
	}
}
