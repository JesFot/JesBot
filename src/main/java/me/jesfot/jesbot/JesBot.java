package me.jesfot.jesbot;

import me.jesfot.jesbot.commands.CommandHandler;
import me.jesfot.jesbot.commands.DelLastCommand;
import me.jesfot.jesbot.commands.ReloadCommand;
import me.jesfot.jesbot.commands.SayAsCommand;
import me.jesfot.jesbot.commands.SetDefaultChannelCommand;
import me.jesfot.jesbot.commands.SetJoinLeaveMsgCommand;
import me.jesfot.jesbot.commands.SpecialHelpCommand;
import me.jesfot.jesbot.commands.StopCommand;
import me.jesfot.jesbot.commands.VersionCommand;
import me.jesfot.jesbot.commands.WhoIsCommand;
import me.jesfot.jesbot.commands.YoutubeCommand;
import me.jesfot.jesbot.commands.music.VolumeCommand;
import me.jesfot.jesbot.config.Configuration;
import me.jesfot.jesbot.listeners.BotReadyListener;
import me.jesfot.jesbot.listeners.MessagesListener;
import me.jesfot.jesbot.listeners.ReloadListener;
import me.jesfot.jesbot.listeners.UserListener;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

public class JesBot
{
	private IDiscordClient client;
	
	private CommandHandler commands;
	private Configuration configMain;
	
	public void init()
	{
		this.configMain = new Configuration("jesbot.cfg");
		
		try
		{
			this.configMain.init();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		this.configMain.setProperty("FirstOn", Boolean.toString(false));
		
		try
		{
			this.client = Utils.getClient(Statics.TOKEN, Statics.LOGIN_ON_START);
		}
		catch(DiscordException de)
		{
			de.printStackTrace();
		}
		
		this.commands = new CommandHandler();
		
		this.commands.addCommand(new VersionCommand());
		this.commands.addCommand(new SayAsCommand());
		this.commands.addCommand(new YoutubeCommand());
		this.commands.addCommand(new me.jesfot.jesbot.commands.music.StopCommand());
		this.commands.addCommand(new VolumeCommand());
		this.commands.addCommand(new DelLastCommand());
		this.commands.addCommand(new WhoIsCommand());
		new ReloadCommand(this);
		new StopCommand(this);
		new SetDefaultChannelCommand(this);
		new SetJoinLeaveMsgCommand(this);
		new SpecialHelpCommand(this);
		
		EventDispatcher dispacher = this.client.getDispatcher();
		dispacher.registerListener(new BotReadyListener());
		dispacher.registerListener(new ReloadListener());
		dispacher.registerListener(new MessagesListener(this));
		dispacher.registerListener(new UserListener(this));
		
		//Thread d = new WaitForEnd(this);
		//d.start();
	}
	
	public IDiscordClient getClient()
	{
		return this.client;
	}
	
	public CommandHandler getCommandHandler()
	{
		return this.commands;
	}
	
	public Configuration getConfig()
	{
		this.configMain.reload();
		return this.configMain;
	}
	
	public void reload() throws DiscordException, RateLimitException, InterruptedException
	{
		this.getCommandHandler().clear();
		
		this.commands.addCommand(new VersionCommand());
		this.commands.addCommand(new SayAsCommand());
		this.commands.addCommand(new YoutubeCommand());
		this.commands.addCommand(new me.jesfot.jesbot.commands.music.StopCommand());
		this.commands.addCommand(new VolumeCommand());
		this.commands.addCommand(new DelLastCommand());
		this.commands.addCommand(new WhoIsCommand());
		new ReloadCommand(this);
		new StopCommand(this);
		new SetDefaultChannelCommand(this);
		new SetJoinLeaveMsgCommand(this);
		new SpecialHelpCommand(this);
	}
}