package me.jesfot.jesbot.utils;

import java.util.List;

import me.jesfot.jesbot.Statics;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class Utils
{
	public static IDiscordClient getClient(String token, boolean login) throws DiscordException
	{
		ClientBuilder buidler = new ClientBuilder();
		buidler.withToken(token);
		if(login)
		{
			return buidler.login();
		}
		return buidler.build();
	}
	
	public static IRole getHigherRole(List<IRole> roles)
	{
		IRole result = roles.get(0);
		for(IRole role : roles)
		{
			if(role.getPosition() > result.getPosition())
			{
				result = role;
			}
		}
		return result;
	}
	
	public static IChannel getChannelAnyWay(IGuild guild, String call)
	{
		if(call.startsWith("<#") && call.endsWith(">"))
		{
			call = call.substring(2, call.length() - 1);
			return guild.getChannelByID(call);
		}
		List<IChannel> results = guild.getChannelsByName(call);
		if(results.isEmpty())
		{
			return null;
		}
		return results.get(0);
	}
	
	public static boolean isMyOwner(IUser user)
	{
		if(user.isBot())
		{
			return false;
		}
		for(String str : Statics.AUTHORS)
		{
			String id = str.split("|")[1];
			if(user.getID() == id)
			{
				return true;
			}
		}
		return false;
	}
	
	public static IUser getUserById(IGuild guild, String call)
	{
		if(call.startsWith("<@") && call.endsWith(">"))
		{
			call = call.substring(2, call.length() - 1);
		}
		if(guild.getUserByID(call) != null)
		{
			return guild.getUserByID(call);
		}
		List<IUser> results = guild.getUsersByName(call);
		if(results.isEmpty())
		{
			return null;
		}
		return results.get(0);
	}
	
	public static boolean hasPermissionSomewhere(IUser user, IChannel channel, Permissions permission)
	{
		if(permission == null || channel.isPrivate())
		{
			return true;
		}
		List<IRole> roles = user.getRolesForGuild(channel.getGuild());
		for(IRole role : roles)
		{
			if(role.getPermissions().contains(permission))
			{
				return true;
			}
		}
		return false;
	}
	
	public static int safeLogout(IDiscordClient client)
	{
		try
		{
			client.logout();
		}
		catch(RateLimitException e)
		{
			e.printStackTrace();
			return 1;
		}
		catch(DiscordException e)
		{
			e.printStackTrace();
			return 2;
		}
		return 0;
	}
	
	public static IMessage sendSafeMessages(IChannel channel, String message)
	{
		if(channel == null)
		{
			return null;
		}
		try
		{
			return channel.sendMessage(message);
		}
		catch(MissingPermissionsException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(RateLimitException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(DiscordException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static IMessage editSafeMessages(IMessage message, String newContent)
	{
		try
		{
			return message.edit(newContent);
		}
		catch(MissingPermissionsException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(RateLimitException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(DiscordException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static int deleteSafeMessages(IMessage message)
	{
		if(message.getChannel().isPrivate())
		{
			return 0;
		}
		try
		{
			message.delete();
		}
		catch(MissingPermissionsException e)
		{
			e.printStackTrace();
			return 1;
		}
		catch(RateLimitException e)
		{
			e.printStackTrace();
			return 2;
		}
		catch(DiscordException e)
		{
			e.printStackTrace();
			return 3;
		}
		return 0;
	}
}