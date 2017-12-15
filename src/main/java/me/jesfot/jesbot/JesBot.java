package me.jesfot.jesbot;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.jesfot.jesbot.ai.MainAI;
import me.jesfot.jesbot.audio.MusicManager;
import me.jesfot.jesbot.commands.AICommand;
import me.jesfot.jesbot.commands.CommandHandler;
import me.jesfot.jesbot.commands.DelLastCommand;
import me.jesfot.jesbot.commands.EndPollCommand;
import me.jesfot.jesbot.commands.FakeMusicCommand;
import me.jesfot.jesbot.commands.PollCommands;
import me.jesfot.jesbot.commands.ReloadCommand;
import me.jesfot.jesbot.commands.ReportCommand;
import me.jesfot.jesbot.commands.ReportsCommand;
import me.jesfot.jesbot.commands.RuCommand;
import me.jesfot.jesbot.commands.SayAsCommand;
import me.jesfot.jesbot.commands.SetDefaultChannelCommand;
import me.jesfot.jesbot.commands.SetJoinLeaveMsgCommand;
import me.jesfot.jesbot.commands.SpecialHelpCommand;
import me.jesfot.jesbot.commands.StartPollCommand;
import me.jesfot.jesbot.commands.StatPollCommand;
import me.jesfot.jesbot.commands.StatusCommand;
import me.jesfot.jesbot.commands.StopCommand;
import me.jesfot.jesbot.commands.VersionCommand;
import me.jesfot.jesbot.commands.WhoIsCommand;
import me.jesfot.jesbot.commands.music.NextCommand;
import me.jesfot.jesbot.commands.music.PauseCommand;
import me.jesfot.jesbot.commands.music.PlayCommand;
import me.jesfot.jesbot.commands.music.PlaylistCommand;
import me.jesfot.jesbot.commands.music.VolumeCommand;
import me.jesfot.jesbot.commands.mychan.ChanClearCommand;
import me.jesfot.jesbot.commands.mychan.ChanPermsCommand;
import me.jesfot.jesbot.commands.mychan.SetChanCommand;
import me.jesfot.jesbot.config.Configuration;
import me.jesfot.jesbot.listeners.BotConnectionsListener;
import me.jesfot.jesbot.listeners.BotReadyListener;
import me.jesfot.jesbot.listeners.MessagesListener;
import me.jesfot.jesbot.listeners.ReloadListener;
import me.jesfot.jesbot.listeners.UserListener;
import me.jesfot.jesbot.polls.Poll;
import me.jesfot.jesbot.reports.ReportManager;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;

public class JesBot
{
	private IDiscordClient client;
	
	private CommandHandler commands;
	private Configuration configMain;
	
	private MainAI artIntel;
	
	private ReportManager reports;
	
	private static JesBot instance;
	
	public Map<Long, Map<String, Poll>> polls;
	
	public boolean init(boolean deamon)
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
			Configuration secrets = new Configuration(Statics.SECRETS_FILE_NAME);
			secrets.init();
			String token = secrets.getProps().getProperty("bot.token");
			if (token == null)
			{
				return false;
			}
			this.client = Utils.getClient(token, Statics.LOGIN_ON_START, deamon);
		}
		catch(DiscordException de)
		{
			de.printStackTrace();
		}
		catch (IOException e)
		{
			return false;
		}
		
		this.polls = new HashMap<Long, Map<String, Poll>>();
		
		JesBot.instance = this;
		
		this.artIntel = new MainAI(this);
		
		this.reports = new ReportManager(new File("datas"), "reports");
		
		this.reports.readConfig();
		
		this.commands = new CommandHandler();
		
		this.reloadCmd();
		
		EventDispatcher dispacher = this.client.getDispatcher();
		dispacher.registerListener(new BotConnectionsListener(this));
		dispacher.registerListener(new BotReadyListener(this));
		dispacher.registerListener(new ReloadListener());
		dispacher.registerListener(new MessagesListener(this));
		dispacher.registerListener(new UserListener(this));
		
		//Thread d = new WaitForEnd(this);
		//d.start();
		
		return true;
	}
	
	public static final JesBot getInstance()
	{
		return JesBot.instance;
	}
	
	public void connectCh(IGuild guild, long cid) throws MissingPermissionsException
	{
		IVoiceChannel voiceChannel = this.getClient().getVoiceChannelByID(cid);
		new MusicManager(guild, 0.50F);
		voiceChannel.join();
	}
	
	public void leaveCh(IGuild guild)
	{
		MusicManager manager = new MusicManager(guild, 0.0f);
		manager.stopAndClear();
		for(IVoiceChannel vch : guild.getVoiceChannels())
		{
			if(vch.isConnected())
			{
				vch.leave();
			}
		}
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
	
	public MainAI getBotAI()
	{
		return this.artIntel;
	}
	
	public ReportManager getReportManager()
	{
		return this.reports;
	}
	
	public void reloadCmd()
	{
		this.getCommandHandler().clear();
		
		this.commands.addCommand(new VersionCommand());
		this.commands.addCommand(new SayAsCommand());
		//this.commands.addCommand(new YoutubeCommand());
		this.commands.addCommand(new DelLastCommand());
		this.commands.addCommand(new WhoIsCommand());
		this.commands.addCommand(new ReportCommand());
		this.commands.addCommand(new StatusCommand(this));
		this.commands.addCommand(new AICommand(this));
		this.commands.addCommand(new ReportsCommand(this));
		this.commands.addCommand(new StartPollCommand(this));
		this.commands.addCommand(new PollCommands.VoteCommand(this));
		this.commands.addCommand(new StatPollCommand(this));
		this.commands.addCommand(new EndPollCommand(this));
		this.commands.addCommand(new RuCommand(this));
		this.commands.addCommand(new SetChanCommand(this));
		this.commands.addCommand(new ChanClearCommand(this));
		this.commands.addCommand(new ChanPermsCommand(this));
		new ReloadCommand(this);
		new StopCommand(this);
		new SetDefaultChannelCommand(this);
		new SetJoinLeaveMsgCommand(this);
		new SpecialHelpCommand(this);
		// Music :
		this.commands.addCommand(new FakeMusicCommand(this));
		this.commands.addCommand(new PlaylistCommand());
		this.commands.addCommand(new PlayCommand());
		this.commands.addCommand(new VolumeCommand());
		this.commands.addCommand(new PauseCommand());
		this.commands.addCommand(new NextCommand());
		this.commands.addCommand(new me.jesfot.jesbot.commands.music.StopCommand());
	}
}