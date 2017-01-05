package me.jesfot.jesbot.commands;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.jesfot.jesbot.Statics;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
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
	
	public final boolean onCommand(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		if(!this.activated)
		{
			BaseCommand.logCommand(sender, datas.getGuild(), fullContents);
			Utils.sendSafeMessages(channel, sender.mention(true) + " This command is disabled, sorry !");
			return false;
		}
		if(!(Utils.isMyOwner(sender) && this.allowForOwner) && !Utils.hasPermissionSomewhere(sender, channel, this.getMinimalPermission()))
		{
			return false;
		}
		BaseCommand.logCommand(sender, datas.getGuild(), fullContents);
		this.fullCommand = fullContents;
		return this.execute(sender, fullContents, channel, datas);
	}
	
	public abstract boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas);
	
	protected final List<String> getArguments()
	{
		List<String> result = new ArrayList<String>();
		String[] tmp = this.fullCommand.split(" ");
		for(int i = 1; i < tmp.length; i++)
		{
			result.add(tmp[i]);
		}
		return result;
	}
	
	protected final String compileFrom(int index)
	{
		String result;
		String[] tmp = this.fullCommand.split(" ");
		result = tmp[index];
		for(int i = (index + 1); i < tmp.length; i++)
		{
			result += " " + tmp[i];
		}
		return result;
	}
	
	private static final void logCommand(IUser sender, IGuild guild, final String fullCmd)
	{
		Logger logger = LoggerFactory.getLogger("[" + Statics.BOT_NAME + "][CommandManager]");
		logger.info("[" + guild.getName() + "]" + sender.getName() + "#" + sender.getDiscriminator() + " used command " + fullCmd);
	}
	
	protected final void registerCommand(CommandHandler handler)
	{
		handler.addCommand(this);
	}
}