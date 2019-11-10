package me.jesfot.jesbotv2.management.command;

public final class CommandResult
{
	private CommandStatus status;
	
	public CommandResult(CommandStatus status)
	{
		this.status = status;
	}
	
	public void setStatus(CommandStatus status)
	{
		this.status = status;
	}
	
	public CommandStatus getStatus()
	{
		return this.status;
	}
	
	public enum CommandStatus
	{
		Preprocessing,
		Processing,
		Untracked,
		Success,
		Failure;
	}
}
