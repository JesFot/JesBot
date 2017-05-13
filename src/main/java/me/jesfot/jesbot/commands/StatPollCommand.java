package me.jesfot.jesbot.commands;

import java.util.List;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.polls.Poll;
import me.jesfot.jesbot.polls.ProgressBar;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class StatPollCommand extends PollCommands
{
	public StatPollCommand(JesBot bot)
	{
		super(bot, "statpoll", "Stat a poll", "Stat a new poll", "/<cmd> [pollID]");
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		List<String> argv = this.getArguments();
		int argc = argv.size();
		if(argc != 0 && argc != 1 
				|| !this.bot.polls.containsKey(channel.getGuild().getLongID())
				|| this.bot.polls.get(channel.getGuild().getLongID()).isEmpty())
		{
			return false;
		}
		String pollID = this.bot.polls.get(channel.getGuild().getLongID()).entrySet().iterator().next().getKey();
		if(argc == 1)
		{
			pollID = argv.get(0);
		}
		Poll poll = this.bot.polls.get(channel.getGuild().getLongID()).get(pollID);
		if(poll == null)
		{
			return false;
		}
		ProgressBar pb = new ProgressBar('[', '█', ' ', ']', 25);
		IUser creator = this.bot.getClient().getUserByID(poll.getSummonerID());
		String msg = sender.mention(true);
		msg += " Poll started by " + creator.getDisplayName(datas.getGuild()) + " (" + creator.getStringID() + ").\n";
		msg += "```\n";
		msg += poll.getQuestion() + "\n";
		for (int i = 0; i < poll.getAlls().size(); i++)
		{
			int votes = poll.getAt(i).getVotes();
			int total = poll.getTotalVotes();
			int pc = (int)(((double)votes / (double)total) * 100);
			msg += Integer.toString(i + 1) + ") " + poll.getAt(i).getAnswer() + " - " + pc + "% " + pb.getFor(pc) + " - " + votes + " vote(s)\n";
		}
		msg += "```\n";
		Utils.sendSafeMessages(channel, msg);
		Utils.deleteSafeMessages(datas);
		return true;
	}
}
