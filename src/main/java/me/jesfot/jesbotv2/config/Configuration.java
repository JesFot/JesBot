package me.jesfot.jesbotv2.config;

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
	private boolean locked = false;
	
	public Configuration(String fileName)
	{
		this.file = new File(fileName);
		try
		{
			this.init();
		}
		catch (IOException r)
		{
			r.printStackTrace();
		}
	}
	
	public Configuration(File folder, String fileName)
	{
		this.file = new File(folder, fileName);
		try
		{
			this.init();
		}
		catch (IOException r)
		{
			r.printStackTrace();
		}
	}
	
	public void lock()
	{
		this.locked = true;
	}
	
	private void init() throws IOException, FileNotFoundException
	{
		if (!this.file.exists())
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
		catch (IOException e)
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
		if (this.locked)
		{
			return this;
		}
		this.props.setProperty(key, value);
		this.save();
		return this;
	}
	
	public void save()
	{
		if (this.locked)
		{
			return;
		}
		try
		{
			this.props.store(new FileOutputStream(this.file), "Config");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}