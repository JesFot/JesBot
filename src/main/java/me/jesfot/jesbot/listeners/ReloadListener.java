package me.jesfot.jesbot.listeners;

import me.jesfot.jesbot.commands.ReloadCommand;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.DiscordDisconnectedEvent;
import sx.blah.discord.handle.impl.events.DiscordDisconnectedEvent.Reason;

public class ReloadListener implements IListener<DiscordDisconnectedEvent>
{
	public void handle(DiscordDisconnectedEvent event)
	{
		if(event.getReason().equals(Reason.LOGGED_OUT))
		{
			if(ReloadCommand.__channel != null)
			{
				try
				{
					event.getClient().login();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
