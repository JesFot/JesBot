package me.jesfot.jesbot.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.jesfot.jesbot.Statics;
import sx.blah.discord.handle.obj.IMessage;

public class CommandHandler
{
	private Map<String, BaseCommand> commands;
	
	public CommandHandler()
	{
		this.commands = new HashMap<String, BaseCommand>();
	}
	
	public void addCommand(BaseCommand cmd)
	{
		this.commands.put(cmd.getName().toLowerCase(), cmd);
	}
	
	public void removeCommand(String cmdName)
	{
		this.commands.remove(cmdName);
	}
	
	public boolean exists(String cmdName)
	{
		return this.commands.containsKey(cmdName);
	}
	
	public BaseCommand getCommand(String cmdName)
	{
		if(cmdName.equalsIgnoreCase("h") || cmdName.equalsIgnoreCase("?"))
		{
			return this.getCommand("help");
		}
		return this.commands.get(cmdName);
	}
	
	public boolean execute(IMessage command)
	{
		BaseCommand cmd = this.getCommand(this.getCommandName(command.getContent()));
		return cmd.onCommand(command.getAuthor(), command.getContent(), command.getChannel(), command);
	}
	
	public boolean isCommand(String msg)
	{
		if(msg == null || msg.isEmpty())
		{
			return false;
		}
		if(msg.startsWith(Statics.COMMAND_DESIGNATOR) && msg.length() >= Statics.COMMAND_DESIGNATOR.length() + 1)
		{
			if(this.exists(this.getCommandName(msg)))
			{
				return true;
			}
		}
		return false;
	}
	
	public String getCommandName(String msg)
	{
		String tmp;
		tmp = msg.substring(Statics.COMMAND_DESIGNATOR.length());
		return tmp.split(" ")[0].toLowerCase();
	}

	public Collection<BaseCommand> getList()
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