package me.jesfot.jesbotv2.utils;

import java.util.logging.Level;

public class MyLogger
{
	private String name;
	private String subType;
	
	private MyLogger(final String n, final String s)
	{
		this.name = n;
		this.subType = s;
	}
	
	public void log(Level level, String message)
	{
		System.out.print("[" + Thread.currentThread().getName() + "] " + level.getName() + " [" + this.name + "] ");
		System.out.println("[" + this.subType + "] " + message);
	}
	
	public static MyLogger getLogger(String name, String subType)
	{
		return new MyLogger(name, subType);
	}
}
