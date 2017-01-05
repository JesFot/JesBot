package me.jesfot.jesbot.listeners;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.Statics;
import me.jesfot.jesbot.commands.ReloadCommand;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Status;

public class BotReadyListener implements IListener<ReadyEvent>
{
	private JesBot jb;
	
	public BotReadyListener(JesBot bot)
	{
		this.jb = bot;
	}
	
	public void handle(ReadyEvent event)
	{
		for(IGuild guild : event.getClient().getGuilds())
		{
			System.out.println("Bot is ready on " + guild.getName());
		}
		event.getClient().changeStatus(Status.game(this.jb.getConfig().getProps().getProperty("bot.game", "'//help' pour des infos")));
		if(ReloadCommand.__channel != null)
		{
			Utils.sendSafeMessages(event.getClient().getChannelByID(ReloadCommand.__channel), "``" + Statics.BOT_NAME + " succesfuly reloaded !!``");
			ReloadCommand.__channel = null;
		}
	}
}