package me.jesfot.jesbot.reports;

public class Report
{
	private String name = "";
	private String senderID = "";
	private String targetID = "";
	private String reason = "";
	private String content = "";
	private String messageID = "";
	private String channelID = "";
	private String date = "";
	private Status status = Status.CANCELLED;
	
	public Report()
	{
		//
	}
	
	public void setName(final String p_name)
	{
		this.name = p_name;
	}
	
	public void setDate(final String p_date)
	{
		this.date = p_date;
	}
	
	public void setContent(final String message)
	{
		this.content = message;
	}
	
	public void setSenderID(final String p_sender)
	{
		this.senderID = p_sender;
	}
	
	public void setTargetID(final String p_target)
	{
		this.targetID = p_target;
	}
	
	public void setMessageID(final String p_message)
	{
		this.messageID = p_message;
	}
	
	public void setChannelID(final String p_channel)
	{
		this.channelID = p_channel;
	}
	
	public void setReason(final String p_reason)
	{
		this.reason = p_reason;
	}
	
	public void setStatus(final Status p_status)
	{
		this.status = p_status;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getDate()
	{
		return this.date;
	}
	
	public String getContent()
	{
		return this.content;
	}
	
	public String getSenderID()
	{
		return this.senderID;
	}
	
	public String getTargetID()
	{
		return this.targetID;
	}
	
	public String getMessageID()
	{
		return this.messageID;
	}
	
	public String getChannelID()
	{
		return this.channelID;
	}
	
	public String getReasons()
	{
		return this.reason;
	}
	
	public Status getStatus()
	{
		return this.status;
	}
	
	public void setField(String field, String value)
	{
		if (field.equalsIgnoreCase("name"))
		{
			this.setName(value);
		}
		else if (field.equalsIgnoreCase("sender"))
		{
			this.setSenderID(value);
		}
		else if (field.equalsIgnoreCase("target"))
		{
			this.setTargetID(value);
		}
		else if (field.equalsIgnoreCase("reason"))
		{
			this.setReason(value);
		}
		else if (field.equalsIgnoreCase("status"))
		{
			this.setStatus(Status.getByID(value));
		}
		else if (field.equalsIgnoreCase("date"))
		{
			this.setDate(value);
		}
		else if (field.equalsIgnoreCase("message"))
		{
			this.setMessageID(value);
		}
		else if (field.equalsIgnoreCase("channel"))
		{
			this.setChannelID(value);
		}
		else if (field.equalsIgnoreCase("content"))
		{
			this.setContent(value);
		}
	}
	
	public static enum Status
	{
		THROWN("thr", "thr"),
		READ("read", "readed"),
		WORKING("working", "working"),
		CANCELLED("cancelled", "canceled"),
		APPLIED("applied", "applyed");
		
		private final String ID;
		private final String old;
		
		private Status(final String p_id, final String p_old)
		{
			this.ID = p_id;
			this.old = p_old;
		}
		
		public String id()
		{
			return this.ID;
		}
		
		public static Status getByID(final String id)
		{
			for (Status status : Status.values())
			{
				if (status.id().equalsIgnoreCase(id) || status.old.equalsIgnoreCase(id))
				{
					return status;
				}
			}
			return null;
		}
	}
}
