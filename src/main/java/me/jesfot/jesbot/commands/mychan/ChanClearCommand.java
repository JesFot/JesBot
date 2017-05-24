package me.jesfot.jesbot.commands.mychan;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.commands.BaseCommand;
import me.jesfot.jesbot.commands.CommandError;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class ChanClearCommand extends BaseCommand
{
	private JesBot jb;
	
	public ChanClearCommand(JesBot bot)
	{
		super("clearall", "Clear all", "Clear ALL the main channel BEWARE !", "<cmd>");
		this.setAllowedForOwner(true);
		this.setMinimalPermission(Permissions.MANAGE_MESSAGES);
		this.jb = bot;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas) throws CommandError
	{
		IMessage msg;
		IChannel target = null;
		String tar = this.jb.getConfig().getProps().getProperty("channel.main." + datas.getGuild().getStringID());
		if (tar != null)
		{
			target = datas.getGuild().getChannelByID(Long.parseUnsignedLong(tar));
		}
		if (target == null)
		{
			Utils.deleteSafeMessages(datas);
			Utils.sendSafeMessages(channel, sender.mention(true) + " Cannot found the main channel for this guild.");
			return false;
		}
		Utils.deleteSafeMessages(datas);
		msg = Utils.sendSafeMessages(channel, sender.mention(true) + " Clearing all these message, can take a while (bot freeze too...).");
		channel.bulkDelete();
		if (!msg.isDeleted())
		{
			Utils.deleteSafeMessages(msg);
		}
		Utils.sendSafeMessages(channel, sender.mention(true) + " Succesfuly cleared the messages");
		return true;
	}
}
