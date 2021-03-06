package me.jesfot.jesbotv2.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter
{
	private final DateFormat date = new SimpleDateFormat(
			System.getProperty("me.jesfot.jesbot.log-date-format", "HH:mm:ss"));
	
	@Override
	public String format(LogRecord record)
	{
		StringBuilder formatted = new StringBuilder();
		
		formatted.append(date.format(record.getMillis()));
		formatted.append(' ').append('[');
		formatted.append(record.getLevel().getLocalizedName());
		formatted.append(']').append(' ');
		formatted.append(this.formatMessage(record));
		formatted.append('\n');
		
		if (record.getThrown() != null)
		{
			StringWriter writer = new StringWriter();
			record.getThrown().printStackTrace(new PrintWriter(writer));
			formatted.append(writer);
		}
		
		return formatted.toString();
	}
}
