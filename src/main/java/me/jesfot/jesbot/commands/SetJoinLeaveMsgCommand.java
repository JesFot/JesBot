package me.jesfot.jesbot.commands;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class SetJoinLeaveMsgCommand extends BaseCommand
{
	private JesBot bot;
	
	public SetJoinLeaveMsgCommand(JesBot jb)
	{
		super("setjlmsg", "Change join/leave message", "Reset the join or the leave server message on main channel",
				"<cmd> join|leave <msg...>");
		this.registerCommand(jb.getCommandHandler());
		this.setMinimalPermission(Permissions.MANAGE_CHANNELS);
		this.bot = jb;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		if(Utils.hasPermissionSomewhere(sender, channel, Permissions.MANAGE_CHANNELS))
		{
			if(this.getArguments().size() >= 2)
			{
				String servID = datas.getGuild().getStringID();
				String act = this.getArguments().get(0);
				if(act.equalsIgnoreCase("join"))
				{
					String value = this.compileFrom(1);
					Utils.sendSafeMessages(channel, sender.mention() + " Setted join message to '" + value + "'.");
					this.bot.getConfig().setProperty("channel.message.join." + servID, value);
					Utils.deleteSafeMessages(datas);
				}
				else if(act.equalsIgnoreCase("leave"))
				{
					String value = this.compileFrom(1);
					Utils.sendSafeMessages(channel, sender.mention() + " Setted leave message to '" + value + "'.");
					this.bot.getConfig().setProperty("channel.message.leave." + servID, value);
					Utils.deleteSafeMessages(datas);
				}
			}
		}
		return true;
	}
}