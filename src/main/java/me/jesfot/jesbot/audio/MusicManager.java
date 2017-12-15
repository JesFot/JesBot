package me.jesfot.jesbot.audio;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.UnsupportedAudioFileException;

import me.jesfot.jesbot.JesBot;
import net.dv8tion.d4j.player.MusicPlayer;
import net.dv8tion.jda.player.hooks.PlayerEventListener;
import net.dv8tion.jda.player.hooks.events.FinishEvent;
import net.dv8tion.jda.player.hooks.events.PlayerEvent;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioSource;
import net.dv8tion.jda.player.source.LocalSource;
import net.dv8tion.jda.player.source.RemoteSource;
import sx.blah.discord.handle.audio.IAudioProvider;
import sx.blah.discord.handle.obj.IGuild;

public class MusicManager implements PlayerEventListener
{
	private MusicPlayer player;
	private final Long gid;
	
	public MusicManager(IGuild guild, final float default_volume)
	{
		this.gid = guild.getLongID();
		IAudioProvider provider = guild.getAudioManager().getAudioProvider();
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
		if(!this.player.getListeners().contains(this))
		{
			this.player.addEventListener(this);
		}
	}
	
	public void addMusic(File file) throws UnsupportedAudioFileException, IOException
	{
		this.player.getAudioQueue().add(new LocalSource(file));
	}
	
	public void addMusics(File...files) throws UnsupportedAudioFileException, IOException
	{
		for(File file : files)
		{
			this.player.getAudioQueue().add(new LocalSource(file));
		}
	}
	
	public void addMusic(URL url) throws UnsupportedAudioFileException, IOException
	{
		this.player.getAudioQueue().add(new RemoteSource(url.toString(), Long.toUnsignedString(this.gid)));
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
	
	public boolean isPlaying()
	{
		return this.player.isPlaying();
	}
	
	public boolean emptyList()
	{
		return this.player.getAudioQueue().isEmpty();
	}
	
	public int getSize()
	{
		return this.player.getAudioQueue().size();
	}
	
	public AudioSource getTrackAt(int index)
	{
		return this.player.getAudioQueue().get(index);
	}
	
	public AudioSource getCurrent()
	{
		return this.player.getCurrentAudioSource();
	}
	
	public String getTime()
	{
		return this.player.getCurrentTimestamp().getFullTimestamp();
	}

	public float getVolume()
	{
		return this.player.getVolume();
	}

	public void repeat()
	{
		this.player.setRepeat(!this.player.isRepeat());
	}

	public boolean getRepeat()
	{
		return this.player.isRepeat();
	}

	public void shuffle()
	{
		this.player.setShuffle(!this.player.isShuffle());
	}

	public boolean getShuffle()
	{
		return this.player.isShuffle();
	}

	public AudioInfo removeMusic(int index)
	{
		if(index < 0 || index >= this.getSize())
		{
			return null;
		}
		return this.player.getAudioQueue().remove(index).getInfo();
	}
	
	public void onEvent(PlayerEvent event)
	{
		if(event instanceof FinishEvent)
		{
			JesBot.getInstance().leaveCh(JesBot.getInstance().getClient().getGuildByID(this.gid));
		}
	}
}