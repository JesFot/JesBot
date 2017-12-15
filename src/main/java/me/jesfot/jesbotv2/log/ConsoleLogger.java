package me.jesfot.jesbotv2.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.unei.configuration.plugin.UneiConfiguration;

import jline.console.ConsoleReader;

public class ConsoleLogger extends Logger
{
	private final Formatter formatter;
	
	public ConsoleLogger(String loggerName, String filePattern, ConsoleReader reader)
	{
		super(loggerName, null);
		this.formatter = new ConsoleFormatter();
		this.setLevel(Level.parse(System.getProperty("me.jesfot.jesbot.log-level", Level.INFO.getName())));
		
		try
		{
			FileHandler fileHandler = new FileHandler(filePattern, 1 << 24, 8, true);
			fileHandler.setLevel(Level.ALL);
			fileHandler.setFormatter(formatter);
			this.addHandler(fileHandler);
			
			ConsoleWriter consoleHandler = new ConsoleWriter(reader);
			consoleHandler.setLevel(this.getLevel());
			consoleHandler.setFormatter(formatter);
			this.addHandler(consoleHandler);
			UneiConfiguration.getInstance().getLogger().addHandler(consoleHandler);
		}
		catch (IOException e)
		{
			System.err.println("Could not register logger!");
			e.printStackTrace();
		}
		
		//
	}
}
