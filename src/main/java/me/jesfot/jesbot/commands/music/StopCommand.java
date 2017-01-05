package me.jesfot.jesbot.commands.music;

import me.jesfot.jesbot.audio.MusicManager;
import me.jesfot.jesbot.commands.BaseMusicCommand;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;

public class StopCommand extends BaseMusicCommand
{
	public StopCommand()
	{
		super("stop", "stop a music", "Stop the current song and clear the playlist", "<cmd>");
		this.setChannelPermission(Permissions.VOICE_CONNECT);
	}
	
	@Override
	public boolean executemusic(IUser sender, String fullContents, IVoiceChannel channel, IMessage datas)
	{
		if(!this.getArguments().isEmpty() || channel == null)
		{
			return false;
		}
		IMessage response = Utils.sendSafeMessages(datas.getChannel(), sender.mention(true) + " Stopping current song...");
		Utils.deleteSafeMessages(datas);
		try
		{
			MusicManager manager = new MusicManager(datas.getGuild(), 0.0f);
			if(manager.isNotPlaying() && manager.emptyList())
			{
				Utils.editSafeMessages(response, sender.mention(true) + " Playlist already empty");
				return true;
			}
			manager.stopAndClear();
		}
		catch(Exception e)
		{
			Utils.editSafeMessages(response, sender.mention(true) + " Some errors occurred while stopping !?");
			return false;
		}
		Utils.editSafeMessages(response, sender.mention(true) + " Succesfully stopped the song and cleared the playlist");
		return true;
	}
}