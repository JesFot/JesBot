package me.jesfot.jesbotv2.log;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Erase;

import jline.console.ConsoleReader;

public class ConsoleWriter extends Handler
{
	private final ConsoleReader console;
	
	public ConsoleWriter(ConsoleReader console)
	{
		this.console = console;
	}
	
	public void print(String s)
	{
		try
		{
			console.print(Ansi.ansi().eraseLine(Erase.ALL).toString() + ConsoleReader.RESET_LINE + s
					+ Ansi.ansi().reset().toString());
			console.drawLine();
			console.flush();
		}
		catch (IOException ex)
		{
			//
		}
	}
	
	@Override
	public void publish(LogRecord record)
	{
		if (this.isLoggable(record))
		{
			this.print(this.getFormatter().format(record));
		}
	}
	
	@Override
	public void flush()
	{
	}
	
	@Override
	public void close() throws SecurityException
	{
	}
}
