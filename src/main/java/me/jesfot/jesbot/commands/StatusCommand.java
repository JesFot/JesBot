package me.jesfot.jesbot.commands;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.api.internal.DiscordClientImpl;
import sx.blah.discord.api.internal.DiscordUtils;
import sx.blah.discord.api.internal.json.requests.PresenceUpdateRequest;
import sx.blah.discord.handle.impl.events.PresenceUpdateEvent;
import sx.blah.discord.handle.impl.events.StatusChangeEvent;
import sx.blah.discord.handle.impl.obj.User;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.handle.obj.Presences;
import sx.blah.discord.handle.obj.Status;

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
				msg += "I'm currently " + this.bot.getClient().getOurUser().getPresence().name();
			}
			else
			{
				msg += "I'm playing " + this.bot.getClient().getOurUser().getStatus().getStatusMessage();
			}
			Utils.sendSafeMessages(channel, msg);
			Utils.deleteSafeMessages(datas);
			return true;
		}
		DiscordClientImpl client = (DiscordClientImpl)this.bot.getClient();
		String status = this.compileFrom(2).toLowerCase();
		if(type.equalsIgnoreCase("status"))
		{
			if(status.contains("online"))
			{
				this.updatePresence(Presences.ONLINE, client.getOurUser().getStatus(), client);
			}
			else if(status.contains("idle"))
			{
				this.updatePresence(Presences.IDLE, client.getOurUser().getStatus(), client);
			}
			else if(status.contains("dnd") || status.contains("do not disturb"))
			{
				this.updatePresence(Presences.DND, client.getOurUser().getStatus(), client);
			}
			else if(status.contains("stream"))
			{
				this.updatePresence(Presences.STREAMING, client.getOurUser().getStatus(), client);
			}
			else if(status.contains("offline"))
			{
				this.updatePresence(Presences.OFFLINE, client.getOurUser().getStatus(), client);
			}
		}
		else
		{
			this.updatePresence(client.getOurUser().getPresence(), Status.game(status), client);
		}
		Utils.deleteSafeMessages(datas);
		return true;
	}

	private void updatePresence(Presences newprc, Status newstatus, DiscordClientImpl client)
	{
		if (newstatus != null && !newstatus.equals(client.getOurUser().getStatus())) {
			Status oldStatus = client.getOurUser().getStatus();
			((User) client.getOurUser()).setStatus(newstatus);
			client.getDispatcher().dispatch(new StatusChangeEvent(client.getOurUser(), oldStatus, newstatus));
		}

		if ((client.getOurUser().getPresence() != newprc)) {
			Presences oldPresence = client.getOurUser().getPresence();
			((User) client.getOurUser()).setPresence(newprc);
			client.getDispatcher().dispatch(new PresenceUpdateEvent(client.getOurUser(), oldPresence, newprc));
		}

		client.ws.send(DiscordUtils.GSON.toJson(new PresenceUpdateRequest(newprc == Presences.IDLE ? System.currentTimeMillis() : null, newstatus)));
	}
}