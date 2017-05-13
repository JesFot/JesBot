package me.jesfot.jesbot.commands;

import java.util.List;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.polls.Poll;
import me.jesfot.jesbot.polls.Result;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public abstract class PollCommands extends BaseCommand
{
	protected JesBot bot;
	
	public PollCommands(JesBot jb, String name, String ltDesc, String desc, String usage)
	{
		super(name, ltDesc, desc, usage);
		this.setAllowedForOwner(false);
		this.setMinimalPermission(null);
		this.bot = jb;
	}
	
	public static class VoteCommand extends PollCommands
	{
		public VoteCommand(JesBot bot)
		{
			super(bot, "vote", "Vote !", "Vote !", "/<cmd> <response>");
		}
		
		@Override
		public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
		{
			List<String> argv = this.getArguments();
			int argc = argv.size();
			if(argc < 1 || argc > 2 || this.bot.polls.isEmpty())
			{
				return false;
			}
			String pollID = this.bot.polls.get(channel.getGuild().getLongID()).entrySet().iterator().next().getKey();
			int choice = 1;
			if(argc == 1)
			{
				choice = Utils.toInt(argv.get(0), 0);
			}
			else
			{
				pollID = argv.get(1);
				choice = Utils.toInt(argv.get(0), 0);
			}
			if(choice < 1 || !this.bot.polls.containsKey(channel.getGuild().getLongID()) ||
					!this.bot.polls.get(channel.getGuild().getLongID()).containsKey(pollID))
			{
				return false;
			}
			Poll poll = this.bot.polls.get(channel.getGuild().getLongID()).get(pollID);
			Result result = poll.vote(choice - 1, datas, true);
			switch(result.getType())
			{
				case ERROR:
				case ABORTED:
					System.out.println(result.getMessage());
					return false;
					
				case VOTED:
				case CHANGED:
				case REMOVED:
					Utils.sendSafeMessages(channel, sender.mention(true) + " Successfully voted for option " + Integer.toString(choice)
						+ ". Use ``//statpoll " + pollID + "`` to view the status of the poll.");
					Utils.deleteSafeMessages(datas);
					return true;
			}
			return true;
		}
	}
}
