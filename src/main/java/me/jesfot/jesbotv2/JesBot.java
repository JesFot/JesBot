package me.jesfot.jesbotv2;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fusesource.jansi.AnsiConsole;

import me.jesfot.jesbotv2.command.CommandSayas;
import me.jesfot.jesbotv2.command.CommandStop;
import me.jesfot.jesbotv2.listeners.MessageListener;
import me.jesfot.jesbotv2.log.ConsoleLogger;
import me.jesfot.jesbotv2.log.LoggingOutputStream;
import me.jesfot.jesbotv2.management.command.CommandBase;
import me.jesfot.jesbotv2.management.command.CommandHandler;
import me.jesfot.jesbotv2.utils.Utils;
import me.unei.configuration.SavedFile;
import me.unei.configuration.api.IFlatConfiguration;
import me.unei.configuration.api.IFlatPropertiesConfiguration;
import me.unei.configuration.api.exceptions.FileFormatException;
import me.unei.configuration.api.impl.PropertiesConfig;

import jline.console.ConsoleReader;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;

public class JesBot
{
	private IDiscordClient client;
	
	private long mainServer = -1;
	
	private PropertiesConfig configMain;
	public final IFlatConfiguration secrets;
	
	private final ConsoleReader consoleReader;
	
	private final ConsoleLogger logger;
	
	private CommandHandler consoleCommandHandler;
	private CommandHandler commandHandler;
	
	private static JesBot INSTANCE;
	
	public JesBot() throws IOException
	{
		System.setProperty("library.jansi.version", "JesBot");
		
		AnsiConsole.systemInstall();
		this.consoleReader = new ConsoleReader();
		this.consoleReader.setExpandEvents(false);
		
		this.logger = new ConsoleLogger("JesBot", "jesbot.log", this.consoleReader);
		System.setErr(new PrintStream(new LoggingOutputStream(this.logger, Level.SEVERE), true));
		System.setOut(new PrintStream(new LoggingOutputStream(this.logger, Level.INFO), true));
		
		this.secrets = new PropertiesConfig(new SavedFile(null, Statics.SECRETS_FILE_NAME, Statics.CONFIG_FILE_EXT));
	}
	
	public void start()
	{
		this.logger.fine("Starting !");
		String token = this.secrets.getString("bot.token");
		if (token == null)
		{
			return;
		}
		this.client = Utils.getClient(token, Statics.LOGIN_ON_START, Statics.IS_DEAMON);
		EventDispatcher evtDispactcher = this.client.getDispatcher();
		evtDispactcher.registerListener(new MessageListener(this));
	}
	
	public boolean init()
	{
		this.logger.fine("Loading configurations...");
		this.configMain = new PropertiesConfig(new SavedFile(null, "jesbot", Statics.CONFIG_FILE_EXT));
		
		try
		{
			this.configMain.reload();
		}
		catch (FileFormatException ffe)
		{
			ffe.printStackTrace();
		}
		
		this.configMain.setBoolean("FirstOn", false);
		
		if (this.configMain.contains("server.main"))
		{
			this.mainServer = this.configMain.getLong("server.main");
		}
		
		try
		{
			this.secrets.reload();
			this.secrets.lock();
		}
		catch (DiscordException de)
		{
			de.printStackTrace();
		}
		catch (Exception e)
		{
			return false;
		}
		
		this.logger.fine("Configuration loaded !");
		this.logger.fine("Preparing command handlers...");
		
		this.consoleCommandHandler = new CommandHandler();
		this.consoleCommandHandler.morePrefixs(Statics.COMMAND_DESIGNATOR, "");
		this.consoleCommandHandler.clear();
		
		this.commandHandler = new CommandHandler();
		this.commandHandler.clear();
		
		this.logger.fine("Handlers : OK");
		this.logger.fine("registering commands...");
		
		CommandBase current = new CommandSayas();
		
		this.consoleCommandHandler.addCommand(current);
		this.commandHandler.addCommand(current);
		
		current = new CommandStop(this);
		
		this.consoleCommandHandler.addCommand(current);
		this.commandHandler.addCommand(current);
		
		this.logger.fine("Commands : OK");
		this.logger.fine("JesBot is ready to start !");
		
		JesBot.INSTANCE = this;
		
		return true;
	}
	
	public void stop(final String reason)
	{
		new Thread("Shutdown Thread") {
			
			public void run()
			{
				getLogger().info("Stopping client...");
				
				getLogger().info("Saving configuration...");
				JesBot.this.configMain.setString("shutdown.last.reason", reason);
				JesBot.this.configMain.save();
				getLogger().info("Saved !");
				
				getLogger().info("Good bye !");
				
				for (Handler handler : getLogger().getHandlers())
				{
					handler.close();
				}
				
				System.exit(0);
			}
		}.start();
	}
	
	public IDiscordClient getClient()
	{
		return this.client;
	}
	
	public long getMyId()
	{
		if (this.client == null || this.client.getOurUser() == null)
		{
			return 0;
		}
		return this.client.getOurUser().getLongID();
	}
	
	public IFlatPropertiesConfiguration getConfig()
	{
		return this.configMain;
	}
	
	public IGuild getMainGuild()
	{
		if (this.client != null)
		{
			return this.client.getGuildByID(this.mainServer);
		}
		return null;
	}
	
	public Logger getLogger()
	{
		return this.logger;
	}
	
	public ConsoleReader getConsoleReader()
	{
		return this.consoleReader;
	}
	
	public CommandHandler getConsoleCommandHandler()
	{
		return this.consoleCommandHandler;
	}
	
	public CommandHandler getCommandHandler()
	{
		return this.commandHandler;
	}
	
	public static final JesBot getInstance()
	{
		return JesBot.INSTANCE;
	}
}
