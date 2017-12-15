package me.jesfot.jesbot.commands.music;

import me.jesfot.jesbot.audio.MusicManager;
import me.jesfot.jesbot.commands.BaseMusicCommand;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;

public class PauseCommand extends BaseMusicCommand
{
	public PauseCommand()
	{
		super("pause", "pause the current song", "pause the current song", "<cmd>");
		this.setAllowedForOwner(false);
		this.setMinimalPermission(null);
		this.setChannelPermission(Permissions.VOICE_CONNECT);
	}
	
	@Override
	public boolean executemusic(IUser sender, String fullContents, IVoiceChannel channel, IMessage datas)
	{
		if(!this.getArguments().isEmpty() || channel == null)
		{
			return false;
		}
		if(!channel.isConnected())
		{
			Utils.sendSafeMessages(datas.getChannel(), sender.mention(true) + " I'm not in this channel... (" + channel.getName() + ").");
			Utils.deleteSafeMessages(datas);
			return true;
		}
		IMessage response = Utils.sendSafeMessages(datas.getChannel(), sender.mention(true) + " Pausing or playing the song...");
		Utils.deleteSafeMessages(datas);
		try
		{
			MusicManager manager = new MusicManager(channel.getGuild(), 0.0f);
			if(manager.isNotPlaying())
			{
				Utils.editSafeMessages(response, sender.mention(true) + " Any song is currently playing or paused.");
				return true;
			}
			if(manager.isPaused())
			{
				manager.play();
				Utils.editSafeMessages(response, sender.mention(true) + " Now playing the song.");
			}
			else
			{
				manager.pause();
				Utils.editSafeMessages(response, sender.mention(true) + " Succesfully paused the song.");
			}
		}
		catch(Exception e)
		{
			Utils.editSafeMessages(response, sender.mention(true) + " Some errors occurred while pausing !?");
			return false;
		}
		return true;
	}
}
