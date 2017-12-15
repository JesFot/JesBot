package me.jesfot.jesbot.utils;

import sx.blah.discord.handle.obj.StatusType;

public class Namer
{
	public static String presence(StatusType pre)
	{
		switch(pre)
		{
			case DND:
				return "Do not disturb";
			case IDLE:
				return "Idle";
			case OFFLINE:
				return "Offline";
			case ONLINE:
				return "Online";
			case STREAMING:
				return "Streaming";
			case UNKNOWN:
				return "Unknown";
		}
		return pre.name();
	}
}
