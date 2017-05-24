package me.jesfot.jesbot.utils;

import java.util.List;

import me.jesfot.jesbot.Statics;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
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
			return guild.getChannelByID(Long.parseUnsignedLong(call));
		}
		List<IChannel> results = guild.getChannelsByName(call);
		if(results.isEmpty())
		{
			return null;
		}
		return results.get(0);
	}
	
	public static IVoiceChannel getVoiceChannelAnyWay(IGuild guild, String call)
	{
		if(call.startsWith("<#") && call.endsWith(">"))
		{
			call = call.substring(2, call.length() - 1);
			return guild.getVoiceChannelByID(Long.parseUnsignedLong(call));
		}
		List<IVoiceChannel> results = guild.getVoiceChannelsByName(call);
		if(results.isEmpty())
		{
			return null;
		}
		return results.get(0);
	}
	
	public static boolean isMe(IUser user, IDiscordClient client)
	{
		if(!user.isBot())
		{
			return false;
		}
		return (user.getLongID() == client.getOurUser().getLongID());
	}
	
	public static boolean isMyOwner(IUser user)
	{
		if(user.isBot())
		{
			return false;
		}
		for(Long id : Statics.AUTHORS_IDS)
		{
			if(user.getLongID() == id.longValue())
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
		if (call.startsWith("!"))
		{
			call = call.substring(1);
		}
		try
		{
			if(guild.getUserByID(Long.parseUnsignedLong(call)) != null)
			{
				return guild.getUserByID(Long.parseUnsignedLong(call));
			}
		}
		catch (Exception e)
		{}
		List<IUser> results = guild.getUsersByName(call);
		if(results.isEmpty())
		{
			return null;
		}
		return results.get(0);
	}
	
	public static String formatUser(IUser user)
	{
		String result = "";
		if (user == null)
		{
			result = "(null)";
			return result;
		}
		result += user.getName() + "#"  + user.getDiscriminator();
		result += "{" + user.getPresence().toString() + "}";
		return result;
	}
	
	public static boolean hasPermissionSomewhere(IUser user, IChannel channel, Permissions permission)
	{
		if(permission == null || channel.isPrivate())
		{
			return true;
		}
		return (channel.getModifiedPermissions(user).contains(permission));
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
	
	public static IMessage sendSafeEmbed(IChannel channel, EmbedObject message)
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
			System.err.println(e.getMessage());
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
			System.err.println(e.getMessage());
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
			System.err.println(e.getMessage());
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
			System.err.println(e.getMessage());
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
	
	public static int toInt(final String str, final int p_default)
	{
		try
		{
			return Integer.parseInt(str);
		}
		catch(NumberFormatException e)
		{
			return p_default;
		}
	}
}