package me.jesfot.jesbot.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.polls.Poll;
import me.jesfot.jesbot.polls.ProgressBar;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class StartPollCommand extends PollCommands
{
	public StartPollCommand(JesBot bot)
	{
		super(bot, "startpoll", "Start a poll", "Start a new poll", "/<cmd> <question> <choice1>; <choice2>[; ...]");
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		List<String> argv = this.getArguments();
		int argc = argv.size();
		if(argc < 2)
		{
			return false;
		}
		String argu = this.compileFrom(1);
		List<String> responses = Arrays.asList(argu.replaceAll("; ", ";").split(";"));
		if(!this.bot.polls.containsKey(channel.getGuild().getID()))
		{
			this.bot.polls.put(channel.getGuild().getID(), new HashMap<String, Poll>());
		}
		String pollID = Integer.toString(this.bot.polls.get(channel.getGuild().getID()).size() + 1);
		while(this.bot.polls.get(channel.getGuild().getID()).containsKey(pollID))
		{
			pollID = Integer.toString(Utils.toInt(pollID, this.bot.polls.size() + 1) + 1);
		}
		Poll poll = new Poll(pollID, datas);
		poll.question(argv.get(0));
		for(String str : responses)
		{
			poll.newAnswer(str);
		}
		this.bot.polls.get(channel.getGuild().getID()).put(pollID, poll);
		ProgressBar pb = new ProgressBar('[', '█', ' ', ']', 25);
		String msg = sender.mention(true);
		msg += " Successfully created poll with id " + pollID + ". Use ``//vote <choice> " + pollID + "`` to vote. Use ``//statpoll " + pollID + "`` to view the status of the poll.\n";
		msg += "```\n";
		msg += poll.getQuestion() + "\n";
		for (int i = 0; i < poll.getAlls().size(); i++)
		{
			int votes = poll.getAt(i).getVotes();
			int total = poll.getTotalVotes();
			int pc = (int)(((double)votes / (double)total) * 100);
			msg += Integer.toString(i + 1) + ") " + poll.getAt(i).getAnswer() + " - " + pc + "% " + pb.getFor(pc) + " - " + votes + " votes\n";
		}
		msg += "```\n";
		Utils.sendSafeMessages(channel, msg);
		Utils.deleteSafeMessages(datas);
		return true;
	}
}
