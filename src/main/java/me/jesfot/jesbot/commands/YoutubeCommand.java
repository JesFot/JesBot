package me.jesfot.jesbot.commands;

import java.util.Iterator;
import java.util.List;

import com.google.api.services.youtube.model.Video;

import me.jesfot.jesbot.utils.Utils;
import me.jesfot.jesbot.youtube.Search;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class YoutubeCommand extends BaseCommand
{
	public YoutubeCommand()
	{
		super("youtube", "Do somethings with youtube", "Search for youtube videos and print them",
				"<cmd> [wm <maxVideos>] show <search|link...> | <cmd> [wm <maxVideos>] <search|link...>");
		this.setMinimalPermission(Permissions.VOICE_SPEAK);
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas) throws CommandError
	{
		int argLength = this.getArguments().size();
		if(argLength < 1)
		{
			throw new CommandError("Not enought arguments", this);
		}
		IMessage resp = Utils.sendSafeMessages(channel, sender.mention(true) + " Searching a video for you....");
		channel.setTypingStatus(true);
		Utils.deleteSafeMessages(datas);
		boolean show = false;
		String search_link = "";
		long max = Search.DEFAULT_RETURNED_VIDEOS;
		if(this.getArguments().get(0).equalsIgnoreCase("wm") && argLength >= 3)
		{
			max = YoutubeCommand.parseSafeLong(this.getArguments().get(1), max);
			if(this.getArguments().get(2).equalsIgnoreCase("show") && argLength >= 4)
			{
				show = true;
				search_link = this.compileFrom(3);
			}
			else
			{
				search_link = this.compileFrom(2);
			}
		}
		else if(this.getArguments().get(0).equalsIgnoreCase("show") && argLength >= 2)
		{
			show = true;
			search_link = this.compileFrom(1);
		}
		else
		{
			search_link = this.compileFrom(0);
		}
		List<Video> results = Search.search(search_link, max);
		if(results == null)
		{
			Utils.editSafeMessages(resp, sender.mention(true) + " Sorry but an error occurred while searching");
			channel.setTypingStatus(false);
			return false;
		}
		if(results.isEmpty())
		{
			Utils.editSafeMessages(resp, sender.mention(true) + " No videos was found for the search " + search_link);
			channel.setTypingStatus(false);
			return false;
		}
		if(show)
		{
			String msg = sender.mention(true) + " The list of videos founded : \n";
			msg += "```\n";
			Iterator<Video> iterator = results.iterator();
			while(iterator.hasNext())
			{
				Video res = iterator.next();
				msg += res.getSnippet().getTitle() + " (https://youtu.be/"
							+ res.getId() + ")\n";
				msg += "\t " + YoutubeCommand.troncDesc(res.getSnippet().getDescription(), 100) + "\n";
				msg += "\t by " + res.getSnippet().getChannelTitle() + "\n";
				msg += "\t duration: " + YoutubeCommand.time(res.getContentDetails().getDuration()) + "\n";
				msg += "\t views: " + res.getStatistics().getViewCount()+ "\n";
				msg += "\t rate: " + res.getStatistics().getLikeCount() + "/" + res.getStatistics().getDislikeCount() + "\n\n";
			}
			msg += "```\n";
			Utils.editSafeMessages(resp, msg);
			channel.setTypingStatus(false);
			return true;
		}
		return true;
	}
	
	private static String troncDesc(String desc, int max)
	{
		if(desc == null)
		{
			return "(null)";
		}
		if(desc.length() >= max)
		{
			desc = desc.substring(0, max - 3);
			desc += "...";
		}
		return desc.replaceAll("\n", " ");
	}
	
	private static String time(String time)
	{
		time = time.substring(2);
		time = time.replace("H", ":");
		time = time.replace("M", ":");
		time = time.replace("S", "");
		return time;
	}
	
	private static long parseSafeLong(String in, long defaul)
	{
		try
		{
			return Long.parseLong(in);
		}
		catch(NumberFormatException ex)
		{
			return defaul;
		}
	}
}