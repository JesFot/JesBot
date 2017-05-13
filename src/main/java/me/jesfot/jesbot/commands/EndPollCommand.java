package me.jesfot.jesbot.commands;

import java.util.List;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.polls.Poll;
import me.jesfot.jesbot.polls.ProgressBar;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class EndPollCommand extends PollCommands
{
	public EndPollCommand(JesBot bot)
	{
		super(bot, "endpoll", "End a poll", "End a poll", "/<cmd> [pollID]");
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		List<String> argv = this.getArguments();
		int argc = argv.size();
		if(argc != 0 && argc != 1 || !this.bot.polls.containsKey(channel.getGuild().getLongID()))
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
		this.bot.polls.get(channel.getGuild().getLongID()).remove(pollID, poll);
		ProgressBar pb = new ProgressBar('[', '█', ' ', ']', 25);
		String msg = sender.mention(true);
		msg += " Poll id " + pollID + " concluded.\n";
		msg += "```\n";
		msg += poll.getQuestion() + "\n";
		for (int i = 0; i < poll.getAlls().size(); i++)
		{
			int votes = poll.getAt(i).getVotes();
			int total = poll.getTotalVotes();
			int pc = (int)(((double)votes / (double)total) * 100);
			String spc = Integer.toString(pc);
			while(spc.length() < 3)
			{
				spc = " " + spc;
			}
			msg += Integer.toString(i + 1) + ") " + poll.getAt(i).getAnswer() + " - " + pc + "% " + pb.getFor(pc) + " - " + votes + " votes\n";
		}
		msg += "```\n";
		Utils.sendSafeMessages(channel, msg);
		Utils.deleteSafeMessages(datas);
		return true;
	}
}
