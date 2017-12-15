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
	
	/*
	 * public void setFailure(CommandFailure failure) {
	 * this.setStatus(CommandStatus.FAILURE); this.failure = failure; }
	 */
	public CommandStatus getStatus()
	{
		return this.status;
	}
	
	/*
	 * public boolean failed() { if (this.failure == null) { return false; }
	 * return this.status.equals(CommandStatus.FAILURE); }
	 *//*
		 * public CommandFailure getFailure() { return this.failure; }
		 */
	public static enum CommandStatus
	{
		Preprocessing,
		Processing,
		Untracked,
		Success,
		Failure;
	}
}
