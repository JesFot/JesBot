package me.jesfot.jesbot.commands;

public class CommandError extends Throwable
{
	private static final long serialVersionUID = -2861665830768483795L;
	
	private BaseCommand thrower;
	
	public CommandError(String description, BaseCommand thrower)
	{
		super(description);
		this.thrower = thrower;
	}
	
	public BaseCommand getThrower()
	{
		return this.thrower;
	}
}
