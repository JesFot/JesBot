package me.jesfot.jesbot.commands.mychan;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.commands.BaseCommand;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;

public class SetChanCommand extends BaseCommand
{
	private JesBot jb;
	
	public SetChanCommand(JesBot jesbot)
	{
		super("/setmainchan", "Define THE channel", "Set the channel the bot will print in", "<cmd> [channel_name]");
		this.setAllowedForOwner(true);
		this.setMinimalPermission(Permissions.MANAGE_CHANNELS);
		this.jb = jesbot;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		IChannel setting = channel;
		String arg1 = null;
		
		if (this.getArguments().size() > 0)
		{
			arg1 = this.getArguments().get(0);
		}
		if (arg1 != null)
		{
			setting = Utils.getChannelAnyWay(datas.getGuild(), arg1);
		}
		if (setting == null || setting.isPrivate() || setting instanceof IVoiceChannel)
		{
			Utils.sendSafeMessages(channel, sender.mention(true) + " Cannot set this channel as main channel.");
			Utils.deleteSafeMessages(datas);
			return false;
		}
		this.jb.getConfig().setProperty("channel.main." + datas.getGuild().getStringID(), setting.getStringID());
		Utils.sendSafeMessages(channel, sender.mention(true) + " Setted the channel #" + setting.getName() + " as main channel.");
		Utils.deleteSafeMessages(datas);
		return true;
	}
}
