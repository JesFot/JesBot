package me.jesfot.jesbot.commands.music;

import me.jesfot.jesbot.audio.MusicManager;
import me.jesfot.jesbot.commands.BaseMusicCommand;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;

public class VolumeCommand extends BaseMusicCommand
{
	public VolumeCommand()
	{
		super("volume", "Manage current bot vulome", "Gets or set the volume", "<cmd> [newVolume]");
		this.setChannelPermission(Permissions.VOICE_CONNECT);
	}
	
	@Override
	public boolean executemusic(IUser sender, String fullContents, IVoiceChannel channel, IMessage datas)
	{
		if(this.getArguments().size() > 1 || channel == null)
		{
			return false;
		}
		Utils.deleteSafeMessages(datas);
		float volume;
		try
		{
			MusicManager manager = new MusicManager(channel.getGuild(), 0.0f);
			volume = manager.getVolume() * 100;
			if(this.getArguments().size() == 1)
			{
				volume = VolumeCommand.getFloat(this.getArguments().get(0), volume);
				if (volume > 100.0F)
					volume = 100.0F;
				if (volume < 0.0F)
					volume = 0.0F;
				if(volume == manager.getVolume())
				{
					Utils.sendSafeMessages(datas.getChannel(), sender.mention(true) + "The volume was already to " + Float.toString(volume) + "%.");
					return true;
				}
				manager.setVolume(volume / 100F);
				Utils.sendSafeMessages(datas.getChannel(), sender.mention(true) + " Sets the volume to " + Float.toString(volume) + "%.");
				return true;
			}
			Utils.sendSafeMessages(datas.getChannel(), sender.mention(true) + " The actual volume is " + Float.toString(volume) + "%.");
			return true;
		}
		catch(Exception ex)
		{
			Utils.sendSafeMessages(datas.getChannel(), sender.mention(true) + " Some errors occurred while getting or changing volume !?");
		}
		return false;
	}
	
	private static final float getFloat(String flt, float def)
	{
		try
		{
			return Float.parseFloat(flt);
		}
		catch(NumberFormatException ex)
		{
			return def;
		}
	}
}