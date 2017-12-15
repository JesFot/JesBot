package me.jesfot.jesbotv2.management.command;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jesfot.jesbotv2.Statics;
import me.jesfot.jesbotv2.management.command.CommandResult.CommandStatus;

import sx.blah.discord.handle.obj.IMessage;

public final class CommandHandler
{
	public static final String COMMAND_REGEXP = "[\\/!?;:.=+@_#&a-zA-Z0-9]+";
	
	private final Map<String, CommandBase> commands;
	
	private String commandPrefix;
	private Pattern commandPattern;
	
	public CommandHandler()
	{
		this.commands = new HashMap<String, CommandBase>();
		this.changePrefix(Statics.COMMAND_DESIGNATOR);
	}
	
	public String changePrefix(String newPrefix)
	{
		String old = this.commandPrefix;
		this.commandPrefix = newPrefix;
		this.commandPattern = Pattern.compile("^(" + Pattern.quote(newPrefix) + ")(" + COMMAND_REGEXP + ")\\s*(.*)",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		return old;
	}
	
	public String morePrefixs(String... newPrefixs)
	{
		if (newPrefixs == null || newPrefixs.length < 1)
		{
			return this.commandPrefix;
		}
		String old = this.commandPrefix;
		this.commandPrefix = newPrefixs[0];
		StringBuilder patternBuilder = new StringBuilder("^(");
		for (int i = 0; i < newPrefixs.length; i++)
		{
			if (i > 0)
			{
				patternBuilder.append('|');
			}
			patternBuilder.append(Pattern.quote(newPrefixs[i]));
		}
		patternBuilder.append(')').append('(').append(COMMAND_REGEXP).append(")\\s*(.*)");
		this.commandPattern = Pattern.compile(patternBuilder.toString(), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		return old;
	}
	
	public Pattern getCommandPettern()
	{
		return this.commandPattern;
	}
	
	public void addCommand(CommandBase cmd)
	{
		this.commands.put(cmd.getName(), cmd);
	}
	
	public CommandBase getCommand(String cmdName)
	{
		return this.commands.get(cmdName);
	}
	
	public CommandBase removeCommand(String cmdName)
	{
		return this.commands.remove(cmdName);
	}
	
	public boolean exists(String cmdName)
	{
		return this.commands.containsKey(cmdName);
	}
	
	public CommandResult executeCommand(IMessage message, String content)
	{
		if (content == null)
		{
			throw new IllegalArgumentException("message or command content cannot be null");
		}
		Matcher matcher = this.commandPattern.matcher(content);
		CommandResult result = new CommandResult(CommandStatus.Preprocessing);
		if (matcher.matches())
		{
			String prefix = matcher.group(1);
			String cmdName = matcher.group(2);
			
			if (this.exists(cmdName))
			{
				String args = matcher.group(3);
				
				CommandContext context;
				
				if (message != null)
				{
					context = new CommandContext(message, prefix, cmdName, args);
				}
				else
				{
					context = new CommandContext(content, prefix, cmdName, args);
				}
				CommandBase command = this.getCommand(cmdName);
				try
				{
					command.executeCommand(context, result);
				}
				catch (CommandError ce)
				{
					result.setStatus(CommandStatus.Failure);
				}
				return result;
			}
		}
		result.setStatus(CommandStatus.Failure);
		return result;
	}
	
	public boolean isCommand(String content)
	{
		if (content == null || content.isEmpty())
		{
			return false;
		}
		Matcher matcher = this.commandPattern.matcher(content);
		if (matcher.matches())
		{
			String cmdName = matcher.group(2);
			return this.exists(cmdName);
		}
		return false;
	}
	
	public String getCommandName(String content)
	{
		Matcher matcher = this.commandPattern.matcher(content);
		if (matcher.matches())
		{
			return matcher.group(2);
		}
		return content;
	}
	
	public Collection<CommandBase> getCommands()
	{
		return this.commands.values();
	}
	
	public Set<String> getNames()
	{
		return this.commands.keySet();
	}
	
	public void clear()
	{
		this.commands.clear();
	}
}
