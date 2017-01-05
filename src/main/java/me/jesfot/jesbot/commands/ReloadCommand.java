package me.jesfot.jesbot.commands;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class ReloadCommand extends BaseCommand
{
	private JesBot bot;
	
	public static String __channel = null;
	
	public ReloadCommand(JesBot jb)
	{
		super("/reload", "Reload the bot", "Switch the bot off and then re-restart it", "<cmd>");
		this.registerCommand(jb.getCommandHandler());
		this.setMinimalPermission(Permissions.MANAGE_SERVER);
		this.bot = jb;
		//this.disable();
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		if(Utils.hasPermissionSomewhere(sender, channel, Permissions.MANAGE_SERVER))
		{
			Utils.deleteSafeMessages(datas);
			try
			{
				this.bot.reload();
				//ReloadCommand.__channel = channel.getID();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
}