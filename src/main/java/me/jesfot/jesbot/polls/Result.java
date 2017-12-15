package me.jesfot.jesbot.polls;

public class Result
{
	private int value;
	private Result.Type type;
	private String message;
	
	public Result()
	{};
	
	public Result(int val, Result.Type tp, String msg)
	{
		this.value = val;
		this.type = tp;
		this.message = msg;
	}
	
	public void setValue(int val)
	{
		this.value = val;
	}
	
	public void setType(Result.Type tp)
	{
		this.type = tp;
	}
	
	public void setMessage(String msg)
	{
		this.message = msg;
	}
	
	public int getValue()
	{
		return this.value;
	}
	
	public Result.Type getType()
	{
		return this.type;
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	public static enum Type
	{
		ERROR,
		ABORTED,
		VOTED,
		CHANGED,
		REMOVED;
	}
}
