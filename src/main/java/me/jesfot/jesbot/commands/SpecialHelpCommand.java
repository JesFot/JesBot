package me.jesfot.jesbot.commands;

import java.util.Collection;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.Statics;
import me.jesfot.jesbot.utils.Replacor;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class SpecialHelpCommand extends BaseCommand
{
	JesBot bot;
	
	public SpecialHelpCommand(JesBot jb)
	{
		super("help", "Show help about commands", "Show some help about given command or list all commands",
				"<cmd> [command]");
		this.registerCommand(jb.getCommandHandler());
		this.bot = jb;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		if(this.getArguments().size() == 0)
		{
			int max = this.max(this.bot.getCommandHandler().getNames()) + 1;
			String msg = "";
			msg += sender.mention(true);
			msg += " The list of alls commands for you : (All commands starts with '" + Statics.COMMAND_DESIGNATOR
					+ "')" + "\n";
			msg += "```\n";
			for(BaseCommand cmd : this.bot.getCommandHandler().getList())
			{
				if(Utils.hasPermissionSomewhere(sender, channel, cmd.getMinimalPermission())
						&& !(channel.isPrivate() && cmd instanceof BaseMusicCommand) && !cmd.isDisabled())
				{
					msg += this.add(max, cmd.getName(), ' ') + " - " + cmd.getShortDescription() + "\n";
				}
			}
			msg += "You can also do '//help format' to get help about formats\n";
			msg += "```\n";
			Utils.sendSafeMessages(channel, msg);
			Utils.deleteSafeMessages(datas);
		}
		else if(this.getArguments().size() == 1)
		{
			String cmd = this.getArguments().get(0);
			String fullCmd = Statics.COMMAND_DESIGNATOR + cmd;
			BaseCommand bcmd;
			if(!this.bot.getCommandHandler().isCommand(fullCmd))
			{
				if(cmd.equalsIgnoreCase("format"))
				{
					String msg = "";
					msg += sender.mention(true) + " The list of possible formats :\n";
					msg += "```\n";
					msg += Replacor.getAll();
					msg += "```\n";
					Utils.sendSafeMessages(channel, msg);
					Utils.deleteSafeMessages(datas);
					return true;
				}
				Utils.sendSafeMessages(channel, sender.mention(true) + " '" + cmd + "' is not a valid command");
				Utils.deleteSafeMessages(datas);
				return false;
			}
			bcmd = this.bot.getCommandHandler().getCommand(cmd);
			String msg = "";
			msg += sender.mention(true);
			msg += " Description of the '" + cmd + "' command :\n";
			msg += "```\n";
			msg += "Description: " + bcmd.getFullDescription() + "\n";
			msg += "Usage: " + bcmd.getUsage().replaceAll("<cmd>", fullCmd) + "\n";
			msg += "Minimal permission: " + this.checkPerm(bcmd.getMinimalPermission()) + "\n";
			msg += "```\n";
			Utils.sendSafeMessages(channel, msg);
			Utils.deleteSafeMessages(datas);
		}
		return true;
	}
	
	private int max(Collection<String> strs)
	{
		int max = 0;
		for(String str : strs)
		{
			if(str.length() > max)
			{
				max = str.length();
			}
		}
		return max;
	}
	
	private String add(int size, String used, char c)
	{
		String result = "";
		result += used;
		size = size - result.length();
		for(int i = 0; i < size; i++)
		{
			result += c;
		}
		return result;
	}
	
	private String checkPerm(Permissions perm)
	{
		if(perm == null)
		{
			return "All";
		}
		return perm.toString();
	}
}