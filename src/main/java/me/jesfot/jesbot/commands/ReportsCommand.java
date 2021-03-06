package me.jesfot.jesbot.commands;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.reports.Report;
import me.jesfot.jesbot.reports.Report.Status;
import me.jesfot.jesbot.reports.ReportManager;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

public class ReportsCommand extends BaseCommand
{
	private JesBot bot;
	
	public ReportsCommand(JesBot jb)
	{
		super("reports", "Admin reports", "Reports gestionnary", "<cmd> list|status | <cmd> change|remove|rename <id> [infos...]");
		this.setAllowedForOwner(false);
		this.setMinimalPermission(Permissions.MANAGE_PERMISSIONS);
		this.bot = jb;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas) throws CommandError
	{
		List<String> args = this.getArguments();
		if (args.size() < 1)
		{
			throw new CommandError("Not Enought Arguments", this);
		}
		if (args.get(0).equalsIgnoreCase("status") && args.size() == 1)
		{
			String msg = sender.mention(true) + " ";
			msg += "Possible status are :\n";
			for (Status status : Status.values())
			{
				msg += "  - " + status.id() + "\n";
			}
			Utils.sendSafeMessages(channel, msg);
			Utils.deleteSafeMessages(datas);
		}
		if (args.get(0).equalsIgnoreCase("list") && (args.size() == 1 || args.size() == 2))
		{
			Map<String, Report> allReports = this.bot.getReportManager().getReportsForGuild(channel.getGuild());
			if (allReports == null || allReports.isEmpty())
			{
				Utils.sendSafeMessages(channel, sender.mention(true) + " No entries for this guild.");
			}
			else if (args.size() == 1)
			{
				String msg = sender.mention(true) + " List of all reports :\n";
				for (Entry<String, Report> entry : allReports.entrySet())
				{
					Report value = entry.getValue();
					IUser v_sender = channel.getGuild().getUserByID(Long.parseUnsignedLong(value.getSenderID()));
					IUser v_target = channel.getGuild().getUserByID(Long.parseUnsignedLong(value.getTargetID()));
					msg += "id: \"" + entry.getKey() + "\" [" + value.getName() + "] ";
					msg += Utils.formatUser(v_sender) + " reported " + Utils.formatUser(v_target) + "\n";
				}
				Utils.sendSafeMessages(channel, msg);
			}
			else
			{
				EmbedBuilder embuilder = new EmbedBuilder();
				embuilder.withTitle("Report details :");
				Report rep = ReportManager.get(allReports, args.get(1));
				if (rep == null)
				{
					Utils.sendSafeMessages(channel, sender.mention(true) + " Report not found.");
				}
				else
				{
					IUser v_sender = channel.getGuild().getUserByID(Long.parseUnsignedLong(rep.getSenderID()));
					IUser v_target = channel.getGuild().getUserByID(Long.parseUnsignedLong(rep.getTargetID()));
					IMessage message = channel.getGuild().getMessageByID(Long.parseUnsignedLong(rep.getMessageID()));
					
					embuilder.appendField("Reporter", Utils.formatUser(v_sender), true).appendField("Reported", Utils.formatUser(v_target), true);
					embuilder.appendField("Name", rep.getName() + " [" + rep.getStatus().toString() + "]", false);
					embuilder.appendField("Channel / Message IDs", channel.getGuild().getChannelByID(Long.parseUnsignedLong(rep.getChannelID())).mention()
							+ " / " + rep.getMessageID(), true);
					
					if (message == null)
					{
						embuilder.appendField("Message", "[Message deleted or not found]", true);
					}
					else
					{
						if (!message.getContent().contentEquals(rep.getContent()))
						{
							embuilder.appendField("Message", "[Edited message] \"``" + message.getContent() + "``\"", true);
						}
					}
					embuilder.appendField("Original Message", "\"``" + rep.getContent() + "``\"", true);
					embuilder.appendField("Reasons", rep.getReasons(), true);
					embuilder.withFooterText(rep.getDate());
					rep.setStatus(Status.READ);
				}
				Utils.sendSafeEmbed(channel, embuilder.build());
			}
			Utils.deleteSafeMessages(datas);
			return true;
		}
		if (args.size() < 2)
		{
			throw new CommandError("Not Enought Arguments with those options", this);
		}
		if (args.get(0).equalsIgnoreCase("remove") && args.size() == 2)
		{
			this.bot.getReportManager().remove(channel.getGuild(), args.get(1));
			Utils.sendSafeMessages(channel, sender.mention(true) + " Removed !");
			Utils.deleteSafeMessages(datas);
			return true;
		}
		if (args.get(0).equalsIgnoreCase("rename") && args.size() == 3)
		{
			String name = args.get(2);
			try
			{
				this.bot.getReportManager().getReport(channel.getGuild(), args.get(1)).setName(name);
			}
			catch (NullPointerException e)
			{
				Utils.sendSafeMessages(channel, sender.mention(true) + " This report does not exists");
				return false;
			}
			Utils.sendSafeMessages(channel, sender.mention(true) + " Report renamed.");
			Utils.deleteSafeMessages(datas);
			return true;
		}
		if (args.get(0).equalsIgnoreCase("change") && args.size() == 3)
		{
			String name = args.get(2);
			try
			{
				this.bot.getReportManager().getReport(channel.getGuild(), args.get(1)).setStatus(Status.getByID(name));
			}
			catch (NullPointerException e)
			{
				Utils.sendSafeMessages(channel, sender.mention(true) + " This report does not exists or you gave a bad status name.");
				return false;
			}
			Utils.sendSafeMessages(channel, sender.mention(true) + " Status changed.");
			Utils.deleteSafeMessages(datas);
			return true;
		}
		throw new CommandError("Unknown option", this);
	}
}
