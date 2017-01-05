package me.jesfot.jesbot.utils;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Replacor
{
	public static String replaceAll(final String msg, final IUser user, final IGuild server)
	{
		String result = Replacor.replaceUsers(msg, user, server);
		return Replacor.replaceServer(result, server);
	}
	
	public static String replaceUsers(final String msg, final IUser user, final IGuild server)
	{
		String result;
		result = msg.replaceAll("<user>", user.getName());
		result = result.replaceAll("<mention>", user.mention(true));
		result = result.replaceAll("<tag>", user.getDiscriminator());
		if(server == null)
		{
			return result;
		}
		result = result.replaceAll("<displayName>", user.getDisplayName(server));
		return result;
	}
	
	public static String replaceServer(final String msg, final IGuild server)
	{
		String result;
		result = msg.replaceAll("<servName>", server.getName());
		result = result.replaceAll("<servMOwner>", server.getOwner().mention(true));
		result = result.replaceAll("<region>", server.getRegion().getName());
		return result;
	}
	
	public static String getAll()
	{
		String resp = "";
		resp += "<user>        - Replace it with the user name" + "\n";
		resp += "<mention>     - Mention the user" + "\n";
		resp += "<tag>         - Replace it with the discriminator of the user" + "\n";
		resp += "<displayName> - Replace it by the server's displayed name of the user" + "\n";
		resp += "<servName>    - Replace it with the name of the server" + "\n";
		resp += "<servMOwner>  - Mention the server's owner" + "\n";
		resp += "<region>      - Gets the region the server is in" + "\n";
		return resp;
	}
}