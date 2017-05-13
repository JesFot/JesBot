package me.jesfot.jesbot.listeners;

import me.jesfot.jesbot.JesBot;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.DiscordDisconnectedEvent;

public class BotConnectionsListener
{
	private JesBot jb;
	
	public BotConnectionsListener(JesBot bot)
	{
		this.jb = bot;
	}
	
	@EventSubscriber
	public void onDisconnect(DiscordDisconnectedEvent event)
	{
		this.jb.getReportManager().saveConfig();
	}
}
