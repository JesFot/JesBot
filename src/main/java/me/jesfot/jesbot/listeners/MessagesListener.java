package me.jesfot.jesbot.listeners;

import me.jesfot.jesbot.JesBot;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEmbedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessagePinEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageSendEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageUnpinEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageUpdateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;

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
		if(this.myBot.getCommandHandler().isCommand(event.getMessage().getContent()))
		{
			this.myBot.getCommandHandler().execute(event.getMessage());
		}
	}
	
	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(this.myBot.getCommandHandler().isCommand(event.getMessage().getContent()))
		{
			this.myBot.getCommandHandler().execute(event.getMessage());
		}
		else if (Boolean.parseBoolean(this.myBot.getConfig().getProps().getProperty("useAIfor." + event.getMessage().getGuild().getStringID(), "false")))
		{
			if(this.myBot.getBotAI().onMessage(event.getMessage()))
			{
				//
			}
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
	public void onMessageReaction(ReactionAddEvent event)
	{
		//
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