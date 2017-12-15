package me.jesfot.jesbot.commands;

import java.util.List;
import java.util.UUID;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.reports.Report;
import me.jesfot.jesbot.reports.Report.Status;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageComparator;
import sx.blah.discord.util.MessageHistory;

public class ReportCommand extends BaseCommand
{
	public ReportCommand()
	{
		super("report", "Report !", "report a player", "<cmd> <player> <msgNumber> <reason...>");
		this.setAllowedForOwner(false);
		this.setMinimalPermission(null);
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas) throws CommandError
	{
		List<String> args = this.getArguments();
		if (args.size() < 3)
		{
			throw new CommandError("Not Enought Arguments", this);
		}
		Report newReport;
		String target = args.get(0);
		int msg = Utils.toInt(args.get(1), 1500);
		long msgId = 0;
		String reason = this.compileFrom(2);
		if (msg <= 0)
		{
			throw new CommandError("Message index is negative", this);
		}
		if (msg >= 1500)
		{
			try
			{
				msgId = Long.parseUnsignedLong(args.get(1));
			}
			catch (NumberFormatException e)
			{
				throw new CommandError("Cannot parse message id", this);
			}
		}
		IUser player = Utils.getUserById(channel.getGuild(), target);
		if (player == null)
		{
			throw new CommandError("Target is null / unfindable", this);
		}
		IMessage message = null;
		if(msgId > 0)
		{
			message = channel.getMessageByID(msgId);
		}
		else
		{
			MessageHistory history = channel.getMessageHistory(msg + 3);
			history.sort(MessageComparator.DEFAULT);
			message = history.asArray()[msg];
		}
		if (message == null || message.getAuthor().getLongID() != player.getLongID())
		{
			throw new CommandError("Cannot get message or is not good author" + (message == null ? " MSG_NULL" : ""), this);
		}
		newReport = new Report();
		newReport.setChannelID(channel.getStringID());
		newReport.setMessageID(message.getStringID());
		newReport.setContent(message.getContent());
		newReport.setDate(datas.getTimestamp().toString());
		newReport.setName(UUID.randomUUID().toString());
		newReport.setReason(reason);
		newReport.setSenderID(sender.getStringID());
		newReport.setTargetID(player.getStringID());
		newReport.setStatus(Status.THROWN);
		JesBot.getInstance().getReportManager().addReport(channel.getGuild(), datas.getStringID(), newReport);
		JesBot.getInstance().getReportManager().saveConfig();
		Utils.sendSafeMessages(channel, sender.mention(true) + " Report sended !");
		Utils.deleteSafeMessages(datas);
		return true;
	}
}
