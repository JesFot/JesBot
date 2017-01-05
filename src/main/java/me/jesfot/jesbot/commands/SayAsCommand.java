package me.jesfot.jesbot.commands;

import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class SayAsCommand extends BaseCommand
{
	public SayAsCommand()
	{
		super("/sayas", "Say something as bot", "Say something throught the bot in channel you want",
				"<cmd> <channel> <msg...>");
		this.setMinimalPermission(Permissions.MANAGE_MESSAGES);
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		if(this.getArguments().size() <= 1)
		{
			return false;
		}
		String chan = this.getArguments().get(0);
		String msg = this.compileFrom(2);
		IChannel dest = Utils.getChannelAnyWay(datas.getGuild(), chan);
		if(chan.equalsIgnoreCase("none"))
		{
			dest = channel;
		}
		if(dest == null)
		{
			return false;
		}
		Utils.sendSafeMessages(dest, msg);
		Utils.deleteSafeMessages(datas);
		return true;
	}
}