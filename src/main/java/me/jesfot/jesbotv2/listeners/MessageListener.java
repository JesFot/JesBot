package me.jesfot.jesbotv2.listeners;

import me.jesfot.jesbotv2.JesBot;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageSendEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageUpdateEvent;

public class MessageListener
{
	private JesBot bot;
	
	public MessageListener(JesBot bot)
	{
		this.bot = bot;
	}
	
	@EventSubscriber
	public void onMessageSend(MessageSendEvent event)
	{
		if (this.bot.getCommandHandler().isCommand(event.getMessage().getContent()))
		{
			this.bot.getCommandHandler().executeCommand(event.getMessage(), event.getMessage().getContent());
		}
	}
	
	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if (this.bot.getCommandHandler().isCommand(event.getMessage().getContent()))
		{
			this.bot.getCommandHandler().executeCommand(event.getMessage(), event.getMessage().getContent());
		}
	}
	
	@EventSubscriber
	public void onMessageUpdate(MessageUpdateEvent event)
	{
		if (!event.getAuthor().isBot() && this.bot.getCommandHandler().isCommand(event.getMessage().getContent()))
		{
			this.bot.getCommandHandler().executeCommand(event.getMessage(), event.getMessage().getContent());
		}
	}
}
