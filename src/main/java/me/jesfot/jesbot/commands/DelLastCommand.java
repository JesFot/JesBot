package me.jesfot.jesbot.commands;

import java.util.Iterator;

import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class DelLastCommand extends BaseCommand
{
	public DelLastCommand()
	{
		super("dellast", "Delete my last message", "Delete my last response to someone", "<cmd>");
		this.setMinimalPermission(Permissions.SEND_MESSAGES);
		this.setAllowedForOwner(true);
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		Iterator<IMessage> it = channel.getMessages().iterator();
		while(it.hasNext())
		{
			IMessage msg = it.next();
			try
			{
				if(msg.getAuthor().getID().equals(msg.getClient().getApplicationClientID()))
				{
					if(msg.getMentions().get(0).equals(sender))
					{
						msg.delete();
						break;
					}
				}
			}
			catch(DiscordException e)
			{
				e.printStackTrace();
			}
			catch(MissingPermissionsException e)
			{
				e.printStackTrace();
			}
			catch(RateLimitException e)
			{
				e.printStackTrace();
			}
		}
		Utils.deleteSafeMessages(datas);
		return false;
	}
}