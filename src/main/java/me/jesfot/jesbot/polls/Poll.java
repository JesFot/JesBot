package me.jesfot.jesbot.polls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import sx.blah.discord.handle.obj.IMessage;

public class Poll
{
	private final String id;
	private final String summonerID;
	private final String channelID;
	
	private String question;
	private final AtomicInteger totalVotes;
	
	private List<Response> answers;
	
	private HashMap<String, Integer> voters;
	
	public Poll(final String p_id, final IMessage original)
	{
		this.id = p_id;
		this.summonerID = original.getAuthor().getID();
		this.channelID = original.getChannel().getID();
		this.totalVotes = new AtomicInteger(0);
		this.question = null;
		this.answers = new ArrayList<Response>();
		this.voters = new HashMap<String, Integer>();
	}
	
	public void question(String qu)
	{
		this.question = qu;
	}
	
	public String getSummonerID()
	{
		return this.summonerID;
	}
	
	public String getChannelID()
	{
		return this.channelID;
	}
	
	public String getQuestion()
	{
		return this.question;
	}
	
	public void addAnswer(Response data)
	{
		this.answers.add(data);
	}
	
	public Response newAnswer(String prop)
	{
		Response resp = new Response(Integer.toString(this.answers.size()));
		resp.setAnswer(prop);
		this.addAnswer(resp);
		return resp;
	}
	
	public Response getAt(final int index)
	{
		if(index < 0 || index >= this.answers.size())
		{
			return null;
		}
		return this.answers.get(index);
	}
	
	public List<Response> getAlls()
	{
		return Collections.unmodifiableList(this.answers);
	}
	
	public int facticeVote()
	{
		return this.totalVotes.incrementAndGet();
	}
	
	public int facticeRemove()
	{
		return this.totalVotes.decrementAndGet();
	}
	
	public Result removeVote(IMessage original)
	{
		if(!this.voters.containsKey(original.getAuthor().getID()))
		{
			return new Result(-1, Result.Type.ABORTED, "No registred votes");
		}
		int index = this.voters.get(original.getAuthor().getID()).intValue();
		this.voters.remove(original.getAuthor().getID());
		Response resp = this.getAt(index);
		if(resp == null)
		{
			return new Result(-1, Result.Type.ERROR, "Index out of bounds or not found");
		}
		resp.getVoters().remove(original.getAuthor().getID());
		this.totalVotes.decrementAndGet();
		return new Result(resp.removeVote(), Result.Type.REMOVED, "Removed vote");
	}
	
	public Result vote(final int index, IMessage original, boolean change)
	{
		if(index >= 0 && index < this.answers.size())
		{
			Result res = new Result(0, Result.Type.VOTED, "Voted");
			if(this.voters.containsKey(original.getAuthor().getID()))
			{
				int idx = this.voters.get(original.getAuthor().getID()).intValue();
				Response old = this.getAt(idx);
				if(old == null)
				{
					this.voters.remove(original.getAuthor().getID());
				}
				else if(!change)
				{
					return new Result(0, Result.Type.ABORTED, "Cannot change vote");
				}
				else
				{
					old.removeVote();
					old.getVoters().remove(original.getAuthor().getID());
					res.setType(Result.Type.CHANGED);
					res.setMessage("Changed vote");
				}
			}
			this.voters.put(original.getAuthor().getID(), new Integer(index));
			Response selected = this.getAt(index);
			if(selected.getVoters().contains(original.getAuthor().getID()))
			{
				return new Result(selected.getVotes(), Result.Type.ABORTED, "Nothing changed");
			}
			selected.getVoters().add(original.getAuthor().getID());
			res.setValue(selected.vote());
			if (res.getType() == Result.Type.VOTED)
				this.totalVotes.incrementAndGet();
			return res;
		}
		return new Result(-1, Result.Type.ERROR, "Index out of bounds");
	}
	
	public int getTotalVotes()
	{
		return this.totalVotes.get();
	}
	
	public String getID()
	{
		return this.id;
	}
}
