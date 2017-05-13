package me.jesfot.jesbot.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class AICommand extends BaseCommand
{
	private final JesBot jesbot;
	
	private Map<String, Starter> actions = new HashMap<String, Starter>();
	
	public AICommand(JesBot bot)
	{
		super("/aictl", "Manage the bot AI", "Configure the bot's AI", "<cmd> start|stop");
		this.setMinimalPermission(Permissions.MANAGE_SERVER);
		this.setAllowedForOwner(true);
		this.jesbot = bot;
		
		// Actions :
		this.actions.put("start", new Starter()
		{
			@Override
			public void run(IMessage source)
			{
				AICommand.this.jesbot.getConfig().setProperty("useAIfor." + source.getGuild().getID(), Boolean.toString(true));
				Utils.sendSafeMessages(source.getChannel(), source.getAuthor().mention(true) + " Activated bot AI");
			}
		});
		this.actions.put("stop", new Starter()
		{
			@Override
			public void run(IMessage source)
			{
				AICommand.this.jesbot.getConfig().setProperty("useAIfor." + source.getGuild().getID(), Boolean.toString(false));
				Utils.sendSafeMessages(source.getChannel(), source.getAuthor().mention(true) + " Activated bot AI");
			}
		});
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas) throws CommandError
	{
		if(this.getArguments().size() < 1)
		{
			throw new CommandError("Not Enought Arguments", this);
		}
		String ctl = this.getArguments().get(0);
		for(Entry<String, Starter> entry : this.actions.entrySet())
		{
			if(entry.getKey().equalsIgnoreCase(ctl))
			{
				entry.getValue().run(datas);
				break;
			}
		}
		Utils.deleteSafeMessages(datas);
		return true;
	}
	
	public static abstract class Starter
	{
		public Starter()
		{};
		
		public abstract void run(IMessage source);
	}
}
