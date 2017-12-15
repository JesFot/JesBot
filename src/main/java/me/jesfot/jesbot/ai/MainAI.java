package me.jesfot.jesbot.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.Statics;
import me.jesfot.jesbot.utils.MyLogger;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public final class MainAI
{
	private final JesBot jesbot;
	
	public static final Map<String, String> POLITENESS = new HashMap<String, String>();
	public static final List<String> IDENTIFIERS = Collections.unmodifiableList(Arrays.asList("JësBot", "JesBot", "tout le monde", "minna"));
	public static final List<String> CANCELERS = Collections.unmodifiableList(Arrays.asList("JësFot", "JesFot", "ce bot", "il"));
	
	static
	{
		HashMap<String, String> polit = new HashMap<String, String>();
		
		polit.put("Salut", "Salut !");
		polit.put("Bonjour", "Bonjour.");
		polit.put("Bye", "Bye");
		polit.put("Au revoir", "Bye");
		polit.put("Bonne nuit", "Salut");
		polit.put("Bonsoir", "Bonsoir.");
		polit.put("comment va tu", "Je vais Bien");
		polit.put("comment vas tu", "Je vais Bien");
		polit.put("comment vas-tu", "Je vais Bien");
		polit.put("comment va-tu", "Bien");
		polit.put("comment ca vas", "Bien");
		polit.put("comment ca va", "Bien");
		polit.put("comment ça va", "Bien");
		polit.put("comment ça vas", "Bien");
		polit.put("tu vas bien", "Oui");
		polit.put("tu va bien", "Oui");
		
		MainAI.POLITENESS.clear();
		MainAI.POLITENESS.putAll(polit);
	}
	
	public MainAI(JesBot bot)
	{
		this.jesbot = bot;
	}
	
	public boolean onMessage(IMessage message)
	{
		if(this.isDirectSpeakingToMe(message.getContent(), (!message.getMentions().isEmpty() && message.getMentions().contains(this.jesbot.getClient().getOurUser()))))
		{
			String resp = null;
			if((resp = this.isToBePolit(message.getContent())) != null)
			{
				MainAI.logIA(message.getAuthor(), message.getChannel(), message.getContent());
				Utils.sendSafeMessages(message.getChannel(), resp);
				return true;
			}
		}
		return false;
	}
	
	public String isToBePolit(String content)
	{
		for(Entry<String, String> entry : MainAI.POLITENESS.entrySet())
		{
			if(content.toLowerCase().contains(entry.getKey().toLowerCase()))
			{
				return entry.getValue();
			}
		}
		return null;
	}
	
	public boolean isDirectSpeakingToMe(String content, boolean force)
	{
		for(String id : MainAI.IDENTIFIERS)
		{
			if(content.toLowerCase().contains(id.toLowerCase()) || force)
			{
				for(String cancel : MainAI.CANCELERS)
				{
					if(content.toLowerCase().contains(cancel.toLowerCase()))
					{
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public static final void logIA(IUser sender, IChannel channel, final String fullContent)
	{
		MyLogger logger = MyLogger.getLogger(Statics.BOT_NAME, "IA Manager");
		logger.log(Level.FINE, "[" + channel.getGuild().getName() + "] /" + channel.getName() + "/ Answered to '" + fullContent + "'");
	}
}
