package me.jesfot.jesbot.listeners;

import me.jesfot.jesbot.JesBot;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageDeleteEvent;
import sx.blah.discord.handle.impl.events.MessageEmbedEvent;
import sx.blah.discord.handle.impl.events.MessagePinEvent;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.MessageSendEvent;
import sx.blah.discord.handle.impl.events.MessageUnpinEvent;
import sx.blah.discord.handle.impl.events.MessageUpdateEvent;

public class MessagesListener
{
	private JesBot myBot;
	
	public MessagesListener(JesBot thebot)
	{
		this.myBot = thebot;
	}
	
	@EventSubscriber
	public void onMessageSend(MessageSendEvent event)
	{
		// Nothing here.
	}
	
	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(this.myBot.getCommandHandler().isCommand(event.getMessage().getContent()))
		{
			this.myBot.getCommandHandler().execute(event.getMessage());
		}
	}
	
	@EventSubscriber
	public void onMessageUpdate(MessageUpdateEvent event)
	{
		if(this.myBot.getCommandHandler().isCommand(event.getNewMessage().getContent()) && !event.getNewMessage().getAuthor().isBot())
		{
			this.myBot.getCommandHandler().execute(event.getNewMessage());
		}
	}
	
	@EventSubscriber
	public void onMessageEmbed(MessageEmbedEvent event)
	{
		//
	}
	
	@EventSubscriber
	public void onMessagePin(MessagePinEvent event)
	{
		//
	}
	
	@EventSubscriber
	public void onMessageUnpin(MessageUnpinEvent event)
	{
		//
	}
	
	@EventSubscriber
	public void onMessageDelete(MessageDeleteEvent event)
	{
		//
	}
}