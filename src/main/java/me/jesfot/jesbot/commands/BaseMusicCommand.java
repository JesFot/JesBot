package me.jesfot.jesbot.commands;

import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;

public abstract class BaseMusicCommand extends BaseCommand
{
	private Permissions channelPermission;
	
	protected BaseMusicCommand(final String command, final String shortdesc, final String longdesc, final String p_usage)
	{
		super(command, shortdesc, longdesc, p_usage);
	}
	
	protected final void setChannelPermission(Permissions perm)
	{
		this.channelPermission = perm;
	}
	
	public final Permissions getChannelPermission()
	{
		return this.channelPermission;
	}
	
	@Override
	public final boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		if(channel.isPrivate())
		{
			Utils.sendSafeMessages(channel, "You cannot control music by private messages !");
			return false;
		}
		IGuild guild = datas.getGuild();
		IVoiceChannel chan = null;
		for(IVoiceChannel chnnel : sender.getConnectedVoiceChannels())
		{
			if(chnnel.getGuild().getID().equals(guild.getID()))
			{
				if(chnnel.getModifiedPermissions(guild.getClient().getOurUser()).contains(Permissions.VOICE_SPEAK))
				{
					if(chnnel.getModifiedPermissions(sender).contains(this.getChannelPermission()))
					{
						chan = chnnel;
					}
				}
			}
		}
		if(chan == null)
		{
			Utils.sendSafeMessages(channel, sender.mention(true) + " You must be connected to a voice channel to use that command !");
			return false;
		}
		return this.executemusic(sender, fullContents, chan, datas);
	}
	
	@Override
	@Deprecated
	protected final void setFullCommand(String caca)
	{
		// Nothing here.
	}
	
	public final boolean fake_command(IUser sender, String fullContents, IMessage message, IVoiceChannel channel)
	{
		if(channel == null)
		{
			Utils.sendSafeMessages(message.getChannel(), sender.mention(true) + " You must give a voice channel to use that command !");
			return false;
		}
		if(message.getChannel().isPrivate())
		{
			Utils.sendSafeMessages(message.getChannel(), "You cannot control music by private messages !");
			return false;
		}
		super.setFullCommand(fullContents);
		return this.executemusic(sender, fullContents, channel, message);
	}
	
	public abstract boolean executemusic(IUser sender, String fullContents, IVoiceChannel channel, IMessage datas);
}
