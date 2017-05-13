package me.jesfot.jesbot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.Statics;
import me.jesfot.jesbot.utils.MyLogger;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public abstract class BaseCommand
{
	private final String commandName;
	private final String shortDescription;
	private final String description;
	private final String usage;
	
	private Permissions minimalPerm;
	
	private String fullCommand;
	
	private boolean activated;
	private boolean allowForOwner;
	
	protected BaseCommand(final String command, final String shortdesc, final String longdesc, final String p_usage)
	{
		this.commandName = command;
		this.shortDescription = shortdesc;
		this.description = longdesc;
		this.usage = p_usage;
		this.minimalPerm = null;
		this.activated = true;
		this.allowForOwner = false;
	}
	
	protected final void disable()
	{
		this.activated = false;
	}
	
	final void setEnable(final boolean enable)
	{
		this.activated = enable;
	}
	
	protected final void setAllowedForOwner(boolean value)
	{
		this.allowForOwner = value;
	}
	
	public final boolean isDisabled()
	{
		return !this.activated;
	}
	
	protected final void setMinimalPermission(final Permissions perm)
	{
		this.minimalPerm = perm;
	}
	
	public final String getName()
	{
		return this.commandName;
	}
	
	public final String getShortDescription()
	{
		return this.shortDescription;
	}
	
	public final String getFullDescription()
	{
		return this.description;
	}
	
	public final String getUsage()
	{
		return this.usage;
	}
	
	public final Permissions getMinimalPermission()
	{
		return this.minimalPerm;
	}
	
	public final boolean isAllowForOwner()
	{
		return this.allowForOwner;
	}
	
	protected void setFullCommand(String something)
	{
		if(this instanceof BaseMusicCommand)
		{
			this.fullCommand = something;
		}
	}
	
	public final boolean onCommand(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		if(!this.activated)
		{
			BaseCommand.logCommand(sender, datas.getChannel(), fullContents);
			Utils.sendSafeMessages(channel, sender.mention(true) + " This command is disabled, sorry !");
			return false;
		}
		if(!(Utils.isMyOwner(sender) && this.allowForOwner) && !Utils.hasPermissionSomewhere(sender, channel, this.getMinimalPermission()))
		{
			return false;
		}
		//BaseCommand.logCommand(sender, datas.getChannel(), fullContents);
		this.fullCommand = fullContents;
		try
		{
			boolean res = this.execute(sender, fullContents, channel, datas);
			if(res)
			{
				BaseCommand.logCommand(sender, datas.getChannel(), "[Success]" + fullContents);
			}
			else
			{
				BaseCommand.logCommand(sender, datas.getChannel(), "[Failure]" + fullContents);
			}
			return res;
		}
		catch (CommandError error)
		{
			BaseCommand.logCommand(sender, datas.getChannel(), "[Failure]" + fullContents);
			BaseCommand.logCommand(sender, datas.getChannel(), "[Failure reason : " + error.getMessage() + "]");
			if (Boolean.parseBoolean(JesBot.getInstance().getConfig().getProps().getProperty("sendreason." + channel.getGuild().getStringID(), Boolean.TRUE.toString())))
			{
				Utils.sendSafeMessages(channel, sender.mention(true) + " Error : \"``" + error.getMessage() + "\"``");
			}
		}
		return false;
	}
	
	public abstract boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas) throws CommandError;
	
	protected final List<String> getArguments()
	{
		List<String> result = new ArrayList<String>();
		String[] tmp = this.fullCommand.split(" ");
		for(int i = 1; i < tmp.length; i++)
		{
			String t = tmp[i];
			if(t.startsWith("\""))
			{
				i++;
				while(!tmp[i].endsWith("\"") && i < tmp.length)
				{
					t += " ";
					t += tmp[i];
					i++;
				}
				if(tmp[i].endsWith("\""))
				{
					t += " " + tmp[i];
				}
				result.add(t.substring(1, t.length() - 1));
			}
			else
			{
				result.add(t);
			}
		}
		return result;
	}
	
	protected final String compileFrom(int index)
	{
		String result;
		String[] tmp = this.getArguments().toArray(new String[]{});
		result = tmp[index];
		for(int i = (index + 1); i < tmp.length; i++)
		{
			result += " " + tmp[i];
		}
		return result;
	}
	
	private static final void logCommand(IUser sender, IChannel channel, final String fullCmd)
	{
		MyLogger logger = MyLogger.getLogger(Statics.BOT_NAME, "CommandManager");
		logger.log(Level.INFO, "[" + channel.getGuild().getName() + "] /" + channel.getName() + "/ " + sender.getName() + "#" + sender.getDiscriminator() + " used command " + fullCmd);
	}
	
	protected final void registerCommand(CommandHandler handler)
	{
		handler.addCommand(this);
	}
}