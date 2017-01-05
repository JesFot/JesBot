package me.jesfot.jesbot.commands;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import me.jesfot.jesbot.Statics;
import me.jesfot.jesbot.utils.Utils;

public class VersionCommand extends BaseCommand
{
	public VersionCommand()
	{
		super("version", "Show the bot version", "Show the bot version", "<cmd>");
	}

	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		String msg = "";
		msg += sender.mention(true);
		msg += "\n" + Statics.BOT_NAME + " ";
		msg += "version " + Statics.VERSION + " ";
		msg += "by " + Statics.DISPLAYED_AUTHOR;
		Utils.sendSafeMessages(channel, msg);
		Utils.deleteSafeMessages(datas);
		return true;
	}
}
