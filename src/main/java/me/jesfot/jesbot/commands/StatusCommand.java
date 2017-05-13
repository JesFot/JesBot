package me.jesfot.jesbot.commands;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.api.internal.DiscordClientImpl;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.handle.obj.StatusType;

public class StatusCommand extends BaseCommand
{
	private JesBot bot;
	
	public StatusCommand(JesBot jb)
	{
		super("/status", "Set the bot's status", "Define or get the actual bot status / game",
				"<cmd> status|game [newStatus]");
		this.setMinimalPermission(Permissions.MANAGE_MESSAGES);
		this.setAllowedForOwner(true);
		this.bot = jb;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas)
	{
		if(this.getArguments().size() < 1)
		{
			return false;
		}
		String type = this.getArguments().get(0);
		if(!type.equalsIgnoreCase("status") && !type.equalsIgnoreCase("game"))
		{
			return false;
		}
		if(this.getArguments().size() == 1)
		{
			String msg = sender.mention(true) + " ";
			if(type.equalsIgnoreCase("status"))
			{
				msg += "I'm currently " + this.bot.getClient().getOurUser().getPresence().getStatus().name();
			}
			else
			{
				msg += "I'm playing " + this.bot.getClient().getOurUser().getPresence().getPlayingText().orElse("nothing");
			}
			Utils.sendSafeMessages(channel, msg);
			Utils.deleteSafeMessages(datas);
			return true;
		}
		DiscordClientImpl client = (DiscordClientImpl)this.bot.getClient();
		String status = this.compileFrom(1).toLowerCase();
		if(type.equalsIgnoreCase("status"))
		{
			if(status.contains("online"))
			{
				this.updatePresence(StatusType.ONLINE, client.getOurUser().getPresence().getStatus(), client);
			}
			else if(status.contains("idle"))
			{
				this.updatePresence(StatusType.IDLE, client.getOurUser().getPresence().getStatus(), client);
			}
			else if(status.contains("dnd") || status.contains("do not disturb"))
			{
				this.updatePresence(StatusType.DND, client.getOurUser().getPresence().getStatus(), client);
			}
			else if(status.contains("stream"))
			{
				this.updatePresence(StatusType.STREAMING, client.getOurUser().getPresence().getStatus(), client);
			}
			else if(status.contains("offline"))
			{
				this.updatePresence(StatusType.OFFLINE, client.getOurUser().getPresence().getStatus(), client);
			}
		}
		else
		{
			//this.updatePresence(client.getOurUser().getPresence(), .game(status), client);
		}
		Utils.deleteSafeMessages(datas);
		return true;
	}

	private void updatePresence(StatusType newprc, StatusType newstatus, DiscordClientImpl client)
	{
		/*if (newstatus != null && !newstatus.equals(client.getOurUser().getPresence().getStatus())) {
			StatusType oldStatus = client.getOurUser().getPresence().getStatus();
			client.getDispatcher().dispatch(new StatusChangeEvent(client.getOurUser(), oldStatus, newstatus));
		}

		if ((client.getOurUser().getPresence() != newprc)) {
			Presences oldPresence = client.getOurUser().getPresence();
			((User) client.getOurUser()).setPresence(newprc);
			client.getDispatcher().dispatch(new PresenceUpdateEvent(client.getOurUser(), oldPresence, newprc));
		}

		client.ws.send(DiscordUtils.GSON.toJson(new PresenceUpdateRequest(newprc == StatusType.IDLE ? System.currentTimeMillis() : null, newstatus)));*/
	}
}