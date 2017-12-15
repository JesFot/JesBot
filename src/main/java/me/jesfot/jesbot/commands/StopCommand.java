package me.jesfot.jesbot.commands;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.Statics;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class StopCommand extends BaseCommand
{
	private JesBot bot;
		
	public StopCommand(JesBot jb)
	{
		super("/stopbot", "Stop the bot", "Terminate the bot procecss", "<cmd>");
		this.registerCommand(jb.getCommandHandler());
		this.setMinimalPermission(Permissions.MANAGE_SERVER);
		this.setAllowedForOwner(true);
		this.bot = jb;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		if(Utils.hasPermissionSomewhere(sender, channel, Permissions.MANAGE_SERVER) || Utils.isMyOwner(sender))
		{
			Utils.sendSafeMessages(channel, Statics.BOT_NAME + " is going offline ...");
			Utils.deleteSafeMessages(datas);
			this.bot.getReportManager().saveConfig();
			Utils.safeLogout(this.bot.getClient());
			try
			{
				Thread.sleep(10);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
}