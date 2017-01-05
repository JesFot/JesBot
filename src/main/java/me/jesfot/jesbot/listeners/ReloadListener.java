package me.jesfot.jesbot.listeners;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.DiscordDisconnectedEvent;

public class ReloadListener implements IListener<DiscordDisconnectedEvent>
{
	public void handle(DiscordDisconnectedEvent event)
	{
		/*if(event.getReason().equals(Reason.LOGGED_OUT))
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
		}*/
	}
}
