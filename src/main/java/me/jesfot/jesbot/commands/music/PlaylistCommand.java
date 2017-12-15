package me.jesfot.jesbot.commands.music;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import me.jesfot.jesbot.audio.MusicManager;
import me.jesfot.jesbot.commands.BaseMusicCommand;
import me.jesfot.jesbot.utils.Utils;
import net.dv8tion.jda.player.source.AudioInfo;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;

public class PlaylistCommand extends BaseMusicCommand
{
	public PlaylistCommand()
	{
		super("playlist", "Playlist commands", "See and control the current playlist", "<cmd> [-md] [add <song> | remove <index> | repeat | shuffle]");
		this.setAllowedForOwner(false);
		this.setMinimalPermission(null);
		this.setChannelPermission(Permissions.VOICE_CONNECT);
	}
	
	@Override
	public boolean executemusic(IUser sender, String fullContents, IVoiceChannel channel, IMessage datas)
	{
		int args = this.getArguments().size();
		if(args > 2)
		{
			return false;
		}
		IMessage response = Utils.sendSafeMessages(datas.getChannel(), sender.mention(true) + " Parsing command...");
		Utils.deleteSafeMessages(datas);
		try
		{
			MusicManager manager = new MusicManager(response.getGuild(), 1.0f);
			if(manager.emptyList() && (args == 0 || (args >= 1 && this.getArguments().get(0).equalsIgnoreCase("remove"))))
			{
				Utils.editSafeMessages(response, sender.mention(true) + " The playlist is empty !");
				return true;
			}
			if(args == 0 || (args == 1 && this.getArguments().get(0).equalsIgnoreCase("-md")))
			{
				this.showList(manager, response, sender, null, (args == 1));
			}
			else if(args == 1)
			{
				String arg = this.getArguments().get(0);
				if(arg.equalsIgnoreCase("repeat"))
				{
					manager.repeat();
					Utils.editSafeMessages(response, sender.mention(true) + " Set repeat mode to " + manager.getRepeat());
				}
				else if(arg.equalsIgnoreCase("shuffle"))
				{
					manager.shuffle();
					Utils.editSafeMessages(response, sender.mention(true) + " Set shuffle mode to " + manager.getShuffle());
				}
				else
				{
					return false;
				}
			}
			else if(args == 2)
			{
				String arg = this.getArguments().get(0);
				if(arg.equalsIgnoreCase("remove"))
				{
					int index = Utils.toInt(this.getArguments().get(1), -1);
					AudioInfo infos = manager.removeMusic(index - 1);
					if(infos != null)
					{
						Utils.editSafeMessages(response, sender.mention(true) + " Removed music #" + index + " (" + infos.getTitle() + ").");
					}
					else
					{
						Utils.editSafeMessages(response, sender.mention(true) + " Could not remove this music #" + index  + ".");
					}
				}
				else if(arg.equalsIgnoreCase("add"))
				{
					String name = this.getArguments().get(1);
					try
					{
						manager.addMusic(new URL(name));
					}
					catch(MalformedURLException e)
					{
						File file = new File(name);
						if(file.exists())
						{
							manager.addMusic(file);
						}
						else
						{
							Utils.editSafeMessages(response, sender.mention(true) + " Song not found !");
							return true;
						}
					}
					Utils.editSafeMessages(response, sender.mention(true) + " Succesfuly added " + name + " to the playlist.");
				}
				else
				{
					return false;
				}
			}
		}
		catch(Exception e)
		{
			Utils.editSafeMessages(response, sender.mention(true) + " Some errors occurred while pausing !?");
			e.printStackTrace();
		}
		return true;
	}
	
	private void showList(MusicManager manager, IMessage response, IUser sender, String before, boolean md)
	{
		String tmp;
		String msg = sender.mention(true) + "";
		if(before != null && !before.isEmpty())
		{
			msg += before;
		}
		msg += "There is " + manager.getSize() + " tracks in the playlist ";
		if(manager.isPlaying())
		{
			msg += "and one track currently reading :";
		}
		msg += "\n";
		msg += "```";
		if(manager.isPlaying())
		{
			msg += "Current: \n";
			msg += (tmp = "Title: " + manager.getCurrent().getInfo().getTitle() + "\n");
			if(!md)
			{
				String desc = manager.getCurrent().getInfo().getDescription();
				if(desc.startsWith(tmp))
				{
					desc = desc.substring(tmp.length());
				}
				msg += "Decription:\n" + desc + "\n";
			}
			msg += "Encoding: " + manager.getCurrent().getInfo().getEncoding() + "\n";
			msg += "Timestamp: " + manager.getTime() + "\n";
			msg += "Duration: " + manager.getCurrent().getInfo().getDuration().getFullTimestamp() + "\n";
		}
		msg += "#----------------------------------------------------------------#\n";
		for(int i = 0; i < manager.getSize(); i++)
		{
			msg += "Track #" + (i + 1) + "\n";
			msg += (tmp = "Title: " + manager.getTrackAt(i).getInfo().getTitle() + "\n");
			if(!md)
			{
				String desc = manager.getTrackAt(i).getInfo().getDescription();
				if(desc.startsWith(tmp))
				{
					desc = desc.substring(tmp.length());
				}
				msg += "Decription:\n" + desc + "\n";
			}
			msg += "Encoding: " + manager.getTrackAt(i).getInfo().getEncoding() + "\n";
			msg += "Duration: " + manager.getTrackAt(i).getInfo().getDuration().getFullTimestamp() + "\n";
			msg += "#----------------------------------------------------------------#\n";
		}
		msg += "```";
		Utils.editSafeMessages(response, msg);
	}
}
