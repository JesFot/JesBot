package me.jesfot.jesbot.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration
{
	private File file;
	private Properties props;
	
	public Configuration(String fileName)
	{
		this.file = new File(fileName);
	}
	
	public Configuration(File folder, String fileName)
	{
		this.file = new File(folder, fileName);
	}
	
	public void init() throws IOException, FileNotFoundException
	{
		if(!this.file.exists())
		{
			this.file.createNewFile();
		}
		this.props = new Properties();
		this.props.load(new FileInputStream(this.file));
		this.props.store(new FileOutputStream(this.file), "Config");
	}
	
	public void reload()
	{
		try
		{
			this.props.load(new FileInputStream(this.file));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Properties getProps()
	{
		return this.props;
	}
	
	public Configuration setProperty(final String key, final String value)
	{
		this.props.setProperty(key, value);
		this.save();
		return this;
	}
	
	public void save()
	{
		try
		{
			this.props.store(new FileOutputStream(this.file), "Config");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}