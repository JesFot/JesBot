package me.jesfot.jesbot.audio;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.dv8tion.d4j.player.MusicPlayer;
import net.dv8tion.jda.player.source.LocalSource;
import net.dv8tion.jda.player.source.RemoteSource;
import sx.blah.discord.handle.audio.IAudioProvider;
import sx.blah.discord.handle.obj.IGuild;

public class MusicManager
{
	private MusicPlayer player;
	//private AudioPlayer audioPlayer;
	
	public MusicManager(IGuild guild, final float default_volume)
	{
		IAudioProvider provider = guild.getAudioManager().getAudioProvider();
		//this.audioPlayer = AudioPlayer.getAudioPlayerForGuild(guild);
		if(!(provider instanceof MusicPlayer))
		{
			this.player = new MusicPlayer();
			this.player.setVolume(default_volume);
			guild.getAudioManager().setAudioProvider(this.player);
		}
		else
		{
			this.player = (MusicPlayer)provider;
		}
	}
	
	public void addMusic(File file) throws UnsupportedAudioFileException, IOException
	{
		this.player.getAudioQueue().add(new LocalSource(file));
		//this.audioPlayer.queue(file);
	}
	
	public void addMusics(File...files) throws UnsupportedAudioFileException, IOException
	{
		for(File file : files)
		{
			this.player.getAudioQueue().add(new LocalSource(file));
			//this.audioPlayer.queue(file);
		}
	}
	
	public void addMusic(URL url) throws UnsupportedAudioFileException, IOException
	{
		this.player.getAudioQueue().add(new RemoteSource(url.toString()));
		//this.audioPlayer.queue(url);
	}
	
	public void addMusic(RemoteSource source)
	{
		this.player.getAudioQueue().add(source);
	}
	
	public void addMusics(RemoteSource...sources)
	{
		for(RemoteSource source : sources)
		{
			this.player.getAudioQueue().add(source);
		}
	}
	
	public void play()
	{
		if(this.player.isPaused())
		{
			this.player.play();
		}
		if(!this.player.isPlaying())
		{
			if(!this.player.getAudioQueue().isEmpty())
			{
				this.player.play();
			}
		}			
	}
	
	public boolean isPaused()
	{
		return this.player.isPaused();
	}
	
	public void pause()
	{
		this.player.pause();
	}
	
	public boolean next()
	{
		if(!this.player.getAudioQueue().isEmpty())
		{
			this.player.skipToNext();
			return true;
		}
		else
		{
			this.player.stop();
			return false;
		}
	}
	
	public void setRepeat(boolean repeat)
	{
		this.player.setRepeat(repeat);
	}
	
	public void setVolume(float volume)
	{
		this.player.setVolume(volume);
	}
	
	public void stopAndClear()
	{
		this.stop();
		this.clear();
	}
	
	public void stop()
	{
		this.player.stop();
	}
	
	public void clear()
	{
		this.player.getAudioQueue().clear();
	}

	public boolean isNotPlaying()
	{
		return this.player.isStopped();
	}
	
	public boolean emptyList()
	{
		return this.player.getAudioQueue().isEmpty();
	}

	public float getVolume()
	{
		return this.player.getVolume();
	}
}