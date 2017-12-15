package me.jesfot.jesbot.polls;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Response
{
	private final String id;
	
	private String text;
	
	private final AtomicInteger votes;
	
	private List<Long> votedIDS;
	
	public Response(final String p_id)
	{
		this.id = p_id;
		this.text = null;
		this.votedIDS = new ArrayList<Long>();
		this.votes = new AtomicInteger(0);
	}
	
	public Response setAnswer(final String data)
	{
		this.text = data;
		return this;
	}
	
	public List<Long> getVoters()
	{
		return this.votedIDS;
	}
	
	public int vote()
	{
		return this.votes.incrementAndGet();
	}
	
	public int removeVote()
	{
		return this.votes.decrementAndGet();
	}
	
	public int getVotes()
	{
		return this.votes.get();
	}
	
	public String getAnswer()
	{
		return this.text;
	}
	
	public String getID()
	{
		return this.id;
	}
}
