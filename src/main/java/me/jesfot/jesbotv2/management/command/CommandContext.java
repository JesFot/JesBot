package me.jesfot.jesbotv2.management.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public final class CommandContext
{
	private static final Pattern ARGUMENTS_PATTERN = Pattern.compile("([^\"]\\S*|\"(.+?)((?<=[^\\\\])\"))\\s*");
	
	private final String originalContent;
	private final List<String> args;
	private final String usedPrefix;
	private final String cmdName;
	private final CommandSender sender;
	
	private final IMessage origin;
	
	public CommandContext(final String command, final String prefix, final String cmdName, final String args)
	{
		this.sender = ConsoleCommandSender.getConsoleCommandSender();
		this.origin = null;
		this.originalContent = command;
		this.usedPrefix = prefix;
		this.cmdName = cmdName;
		this.args = Collections.unmodifiableList(CommandContext.parseArguments(args));
	}
	
	public CommandContext(final IMessage message, final String prefix, final String cmdName, final String args)
	{
		this.sender = new UserCommandSender(message.getAuthor(), message.getChannel());
		this.origin = message;
		final String content = message.getContent();
		this.originalContent = content;
		this.usedPrefix = prefix;
		this.cmdName = cmdName;
		this.args = Collections.unmodifiableList(CommandContext.parseArguments(args));
	}
	
	public boolean hasOriginDiscordMessage()
	{
		return (this.origin != null);
	}
	
	public IMessage getOriginDiscordMessage()
	{
		return this.origin;
	}
	
	public IChannel getOriginDiscordChannel()
	{
		if (!this.hasOriginDiscordMessage())
		{
			return null;
		}
		return this.origin.getChannel();
	}
	
	public CommandSender getSender()
	{
		return this.sender;
	}
	
	public String getName()
	{
		return this.cmdName;
	}
	
	public List<String> getArgs()
	{
		return this.args;
	}
	
	public String getUsedPrefix()
	{
		return this.usedPrefix;
	}
	
	public String getArgument(int index)
	{
		return this.args.get(index);
	}
	
	public String getFullCommand()
	{
		return this.originalContent;
	}
	
	public String compileArgumentsFrom(int index)
	{
		StringBuilder sb = new StringBuilder();
		
		for (int i = index; i < this.args.size(); i++)
		{
			if (i != index)
			{
				sb.append(' ');
			}
			sb.append(this.args.get(i));
		}
		return sb.toString();
	}
	
	private static List<String> parseArguments(String content)
	{
		List<String> result = new ArrayList<String>();
		Matcher ma = CommandContext.ARGUMENTS_PATTERN.matcher(content);
		while (ma.find())
		{
			if (ma.groupCount() > 3)
			{
				result.add(ma.group(2));
			}
			else
			{
				result.add(ma.group(1));
			}
		}
		return result;
	}
}
