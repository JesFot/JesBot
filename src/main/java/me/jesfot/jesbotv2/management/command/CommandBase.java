package me.jesfot.jesbotv2.management.command;

import java.util.logging.Logger;

import me.jesfot.jesbotv2.JesBot;
import me.jesfot.jesbotv2.management.command.CommandResult.CommandStatus;
import me.jesfot.jesbotv2.utils.Utils;

import sx.blah.discord.handle.obj.Permissions;

public abstract class CommandBase
{
	private final String commandName;
	private final String shortDescription;
	private final String description;
	private final String usage;
	
	private Permissions minimalPermission;
	
	private boolean activated;
	private String disabledMessage = "This command has been disabled";
	private boolean ownerBypass;
	
	protected CommandBase(final String command, final String shortDesc, final String longDesc, final String p_usage)
	{
		this.commandName = command;
		this.shortDescription = shortDesc;
		this.description = longDesc;
		this.usage = p_usage;
		this.minimalPermission = null;
		this.activated = true;
		this.ownerBypass = false;
	}
	
	protected final void disable(String message)
	{
		this.activated = false;
		if (message != null)
		{
			this.disabledMessage = message;
		}
	}
	
	protected final void setMinimalPermission(Permissions permission)
	{
		this.minimalPermission = permission;
	}
	
	public final Permissions getMinimaPermission()
	{
		return this.minimalPermission;
	}
	
	protected final void setOwnerBypass(boolean bypass)
	{
		this.ownerBypass = bypass;
	}
	
	public final boolean isDisabled()
	{
		return !this.activated;
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
	
	public final String getRawUsage()
	{
		return this.usage;
	}
	
	public final boolean isBypassByOwner()
	{
		return this.ownerBypass;
	}
	
	public final String getDisabledMessage()
	{
		return this.disabledMessage;
	}
	
	public void onFailure(CommandContext context, CommandResult result)
	{
		CommandBase.logCommand(context, result, null);
	}
	
	private final void notActivated(CommandContext context, CommandResult state)
	{
		state.setStatus(CommandStatus.Failure);
		CommandBase.logCommand(context, state, this.getDisabledMessage());
		Utils.sendSafeMessages(context.getOriginDiscordChannel(),
				context.getSender().getDiscordUser().mention() + this.getDisabledMessage());
	}
	
	public final void executeCommand(CommandContext context, CommandResult result) throws CommandError
	{
		if (!this.activated)
		{
			this.notActivated(context, result);
			return;
		}
		if (!context.getSender().hasPermission(minimalPermission))
		{
			result.setStatus(CommandStatus.Failure);
			this.onFailure(context, result);
			return;
		}
		try
		{
			result.setStatus(CommandStatus.Processing);
			boolean res = this.execute(context, result);
			if (result.getStatus().equals(CommandStatus.Processing))
			{
				result.setStatus((res ? CommandStatus.Success : CommandStatus.Untracked));
			}
			else if (result.getStatus().equals(CommandStatus.Untracked))
			{
				result.setStatus((res ? CommandStatus.Success : CommandStatus.Failure));
			}
		}
		catch (CommandError ce)
		{
			result.setStatus(CommandStatus.Failure);
			CommandBase.logCommand(context, result, ce.getMessage());
			throw ce;
		}
		if (result.getStatus().equals(CommandStatus.Failure))
		{
			this.onFailure(context, result);
		}
	}
	
	protected abstract boolean execute(CommandContext context, CommandResult state) throws CommandError;
	
	public static final void logCommand(CommandContext context, CommandResult result, final String message)
	{
		Logger logger = JesBot.getInstance().getLogger();
		if (context.getSender().isConsole() && message != null)
		{
			logger.info(() -> "[CommandManager] " + message);
		}
		else if (!context.getSender().isConsole())
		{
			logger.info(() -> "[CommandManager] [" + context.getOriginDiscordMessage().getGuild().getName() + "] ("
					+ context.getOriginDiscordChannel().getName() + ") "
					+ context.getSender().getDiscordUser().getName() + "#"
					+ context.getSender().getDiscordUser().getDiscriminator() + " used command ["
					+ result.getStatus().name() + "]" + context.getFullCommand());
			if (result.getStatus().equals(CommandStatus.Failure))
			{
				logger.info(() -> "[CommandManager] [" + context.getOriginDiscordMessage().getGuild().getName() + "] ("
						+ context.getOriginDiscordChannel().getName() + ") "
						+ context.getSender().getDiscordUser().getName() + "#"
						+ context.getSender().getDiscordUser().getDiscriminator() + " [Failure reason : " + message
						+ "]");
			}
			else if (message != null)
			{
				logger.info(() -> "[CommandManager] [" + context.getOriginDiscordMessage().getGuild().getName() + "] ("
						+ context.getOriginDiscordChannel().getName() + ") "
						+ context.getSender().getDiscordUser().getName() + "#"
						+ context.getSender().getDiscordUser().getDiscriminator() + " more infos : " + message);
			}
		}
		else
		{
			logger.info(() -> "[CommandManager] Command state : " + result.getStatus().name());
			if (result.getStatus().equals(CommandStatus.Failure))
			{
				logger.info(() -> "[CommandManager] Failure reason : " + message);
			}
		}
	}
}
