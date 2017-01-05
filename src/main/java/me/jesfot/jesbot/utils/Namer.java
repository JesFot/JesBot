package me.jesfot.jesbot.utils;

import sx.blah.discord.handle.obj.Presences;

public class Namer
{
	public static String presence(Presences pre)
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
		}
		return pre.name();
	}
}
