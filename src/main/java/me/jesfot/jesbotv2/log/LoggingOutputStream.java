package me.jesfot.jesbotv2.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingOutputStream extends ByteArrayOutputStream
{
	private static final String SEPARATOR = System.getProperty("line.separator");
	
	private final Logger logger;
	private final Level level;
	
	public LoggingOutputStream(Logger logger, Level level)
	{
		this.logger = logger;
		this.level = level;
	}
	
	public void flush() throws IOException
	{
		String contents = this.toString(StandardCharsets.UTF_8.name());
		super.reset();
		if (!contents.isEmpty() && !contents.equals(LoggingOutputStream.SEPARATOR))
		{
			this.logger.logp(this.level, "", "", contents);
		}
	}
}
