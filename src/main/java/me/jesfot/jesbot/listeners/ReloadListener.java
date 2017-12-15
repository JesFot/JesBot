package me.jesfot.jesbot.listeners;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.shard.DisconnectedEvent;

public class ReloadListener implements IListener<DisconnectedEvent>
{
	public void handle(DisconnectedEvent event)
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
