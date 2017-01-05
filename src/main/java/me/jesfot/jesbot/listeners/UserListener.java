package me.jesfot.jesbot.listeners;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.utils.Replacor;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.UserBanEvent;
import sx.blah.discord.handle.impl.events.UserJoinEvent;
import sx.blah.discord.handle.impl.events.UserLeaveEvent;
import sx.blah.discord.handle.impl.events.UserPardonEvent;
import sx.blah.discord.handle.impl.events.UserRoleUpdateEvent;
import sx.blah.discord.handle.impl.events.UserUpdateEvent;
import sx.blah.discord.handle.obj.IChannel;

public class UserListener
{
	private JesBot bot;
	
	public UserListener(JesBot jb)
	{
		this.bot = jb;
	}
	
	@EventSubscriber
	public void onUserJoin(UserJoinEvent event)
	{
		IChannel main;
		String ch = this.bot.getConfig().getProps().getProperty("channel.default." + event.getGuild().getID());
		main = event.getGuild().getChannelByID(ch);
		String msg = this.bot.getConfig().getProps().getProperty("channel.message.join." + event.getGuild().getID());
		if(msg == null)
		{
			msg = "Welcome <mention> on this server !";
		}
		Utils.sendSafeMessages(main, Replacor.replaceAll(msg, event.getUser(), event.getGuild()));
	}
	
	@EventSubscriber
	public void onUserUpdate(UserUpdateEvent event)
	{
		//
	}
	
	@EventSubscriber
	public void onUserRoleUpdate(UserRoleUpdateEvent event)
	{
		//
	}
	
	@EventSubscriber
	public void onUserLeave(UserLeaveEvent event)
	{
		IChannel main;
		String ch = this.bot.getConfig().getProps().getProperty("channel.default." + event.getGuild().getID());
		main = event.getGuild().getChannelByID(ch);
		String msg = this.bot.getConfig().getProps().getProperty("channel.message.leave." + event.getGuild().getID());
		if(msg == null)
		{
			msg = "Bye bye <user> !";
		}
		Utils.sendSafeMessages(main, Replacor.replaceAll(msg, event.getUser(), event.getGuild()));
	}
	
	@EventSubscriber
	public void onUserBan(UserBanEvent event)
	{
		//
	}
	
	@EventSubscriber
	public void onUserPardon(UserPardonEvent event)
	{
		//
	}
}