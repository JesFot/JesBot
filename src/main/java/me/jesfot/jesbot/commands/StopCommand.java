package me.jesfot.jesbot.commands;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.Statics;
import me.jesfot.jesbot.utils.Utils;

public class StopCommand extends BaseCommand
{
	private JesBot bot;
		
	public StopCommand(JesBot jb)
	{
		super("/stopbot", "Stop the bot", "Terminate the bot procecss", "<cmd>");
		this.registerCommand(jb.getCommandHandler());
		this.setMinimalPermission(Permissions.MANAGE_SERVER);
		this.bot = jb;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		if(Utils.hasPermissionSomewhere(sender, channel, Permissions.MANAGE_SERVER))
		{
			Utils.sendSafeMessages(channel, Statics.BOT_NAME + " is going offline ...");
			Utils.deleteSafeMessages(datas);
			Utils.safeLogout(this.bot.getClient());
			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
}