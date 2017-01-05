package me.jesfot.jesbot.listeners;

import me.jesfot.jesbot.Statics;
import me.jesfot.jesbot.commands.ReloadCommand;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Status;

public class BotReadyListener implements IListener<ReadyEvent>
{
	public void handle(ReadyEvent event)
	{
		for(IGuild guild : event.getClient().getGuilds())
		{
			System.out.println("Bot is ready on " + guild.getName());
		}
		event.getClient().changeStatus(Status.game("faire le bot | Testing"));
		if(ReloadCommand.__channel != null)
		{
			Utils.sendSafeMessages(event.getClient().getChannelByID(ReloadCommand.__channel), "``" + Statics.BOT_NAME + " succesfuly reloaded !!``");
			ReloadCommand.__channel = null;
		}
	}
}