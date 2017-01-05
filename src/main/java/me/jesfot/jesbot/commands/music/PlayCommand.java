package me.jesfot.jesbot.commands.music;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import me.jesfot.jesbot.audio.MusicManager;
import me.jesfot.jesbot.commands.BaseMusicCommand;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MissingPermissionsException;

public class PlayCommand extends BaseMusicCommand
{
	public PlayCommand()
	{
		super("play", "start a music", "Add a music to queue and play the first one", "<cmd> <trackName>");
		this.setAllowedForOwner(false);
		this.setChannelPermission(Permissions.VOICE_CONNECT);
		this.setMinimalPermission(null);
	}
	
	@Override
	public boolean executemusic(IUser sender, String fullContents, IVoiceChannel channel, IMessage datas)
	{
		if(this.getArguments().size() != 1 || channel == null)
		{
			return false;
		}
		if(!channel.getModifiedPermissions(datas.getClient().getOurUser()).contains(Permissions.VOICE_SPEAK))
		{
			return false;
		}
		try
		{
			channel.join();
		}
		catch (MissingPermissionsException e1)
		{
			e1.printStackTrace();
		}
		IMessage response = Utils.sendSafeMessages(datas.getChannel(), sender.mention(true) + " Adding a new song...");
		Utils.deleteSafeMessages(datas);
		try
		{
			MusicManager manager = new MusicManager(channel.getGuild(), 1.0f);
			if(PlayCommand.isUrl(this.getArguments().get(0)))
			{
				manager.addMusic(new URL(this.getArguments().get(0)));
			}
			else
			{
				manager.addMusic(new File(this.getArguments().get(0)));
			}
			manager.play();
			Utils.editSafeMessages(response, sender.mention(true) + " Succesfuly added new song !");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Utils.editSafeMessages(response, sender.mention(true) + " Some errors occurred while adding a song !?");
			return false;
		}
		return true;
	}
	
	private static final boolean isUrl(String some)
	{
		try
		{
			new URL(some);
		}
		catch(MalformedURLException e)
		{
			return false;
		}
		return true;
	}
}
