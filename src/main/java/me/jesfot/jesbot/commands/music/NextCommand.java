package me.jesfot.jesbot.commands.music;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.audio.MusicManager;
import me.jesfot.jesbot.commands.BaseMusicCommand;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;

public class NextCommand extends BaseMusicCommand
{
	public NextCommand()
	{
		super("next", "pass to the next song", "Move to the next song in the playlist", "<cmd>");
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
		IMessage response = Utils.sendSafeMessages(datas.getChannel(), sender.mention(true) + " Skipping current song...");
		Utils.deleteSafeMessages(datas);
		try
		{
			MusicManager manager = new MusicManager(datas.getGuild(), 0.50f);
			if(manager.isNotPlaying() && manager.emptyList())
			{
				Utils.editSafeMessages(response, sender.mention(true) + " Playlist is empty");
				JesBot.getInstance().leaveCh(channel.getGuild());
				return true;
			}
			if(manager.next())
			{
				Utils.editSafeMessages(response, sender.mention(true) + " Succesfully skipped the song");
			}
			else
			{
				Utils.editSafeMessages(response, sender.mention(true) + " Could not skip the song");
			}
		}
		catch(Exception e)
		{
			Utils.editSafeMessages(response, sender.mention(true) + " Some errors occurred while stopping !?");
			return false;
		}
		return true;
	}
}
