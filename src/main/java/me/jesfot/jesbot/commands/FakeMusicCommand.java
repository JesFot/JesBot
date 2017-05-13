package me.jesfot.jesbot.commands;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;

public class FakeMusicCommand extends BaseCommand
{
	private JesBot jb;
	
	public FakeMusicCommand(JesBot bot)
	{
		super("fakemusic", "something special", "Execute a music command with given voice channel.", "<cmd> <voiceChannel> <cmds...>");
		this.setAllowedForOwner(true);
		this.setMinimalPermission(Permissions.MANAGE_CHANNELS);
		this.jb = bot;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas) throws CommandError
	{
		if(this.getArguments().size() < 2)
		{
			Utils.sendSafeMessages(channel, sender.mention(true) + " Missing arguments. ('//help fakemusic' for help).");
			return false;
		}
		IVoiceChannel result;
		IChannel ch = Utils.getVoiceChannelAnyWay(channel.getGuild(), this.getArguments().get(0));
		if(ch instanceof IVoiceChannel)
		{
			result = (IVoiceChannel)ch;
		}
		else
		{
			Utils.sendSafeMessages(channel, sender.mention(true) + " Channel not found.");
			return false;
		}
		BaseMusicCommand cmd;
		BaseCommand ccmd = this.jb.getCommandHandler().getCommand(this.getArguments().get(1));
		if(ccmd != null && ccmd instanceof BaseMusicCommand)
		{
			cmd = (BaseMusicCommand)ccmd;
		}
		else
		{
			Utils.sendSafeMessages(channel, sender.mention(true) + " Command not found.");
			return false;
		}
		return cmd.fake_command(sender, this.compileFrom(1), datas, result);
	}
}
