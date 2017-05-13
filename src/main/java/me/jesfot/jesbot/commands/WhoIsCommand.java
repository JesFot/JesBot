package me.jesfot.jesbot.commands;

import java.util.ArrayList;
import java.util.List;

import me.jesfot.jesbot.utils.Namer;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class WhoIsCommand extends BaseCommand
{
	public WhoIsCommand()
	{
		super("whois", "Who is he / she ?", "Tells public informations about someone", "<cmd> <user>");
		this.setMinimalPermission(Permissions.READ_MESSAGES);
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas) throws CommandError
	{
		if(this.getArguments().size() != 1)
		{
			throw new CommandError("Must have 1 argument", this);
		}
		Utils.deleteSafeMessages(datas);
		IUser target = null;
		String name = this.getArguments().get(0);
		target = Utils.getUserById(channel.getGuild(), name);
		List<IUser> users = new ArrayList<IUser>();
		if(target == null)
		{
			users = channel.getGuild().getUsersByName(name, true);
		}
		String msg = sender.mention(true) + " ";
		if(target == null && users.isEmpty())
		{
			msg += "Nobody was found for the name '" + name + "' in this guild, sorry.";
			Utils.sendSafeMessages(channel, msg);
			return true;
		}
		if(users.size() > 1 || (users.size() == 1 && target != null))
		{
			msg += "There is more than one user found (" + users.size() + " exactly) :\n";
		}
		if(target != null)
		{
			msg += "```";
			msg += "User: " + target.getName() + "\n";
			msg += "Discr: " + target.getDiscriminator() + "\n";
			msg += "Display Name: " + target.getDisplayName(channel.getGuild()) + "\n";
			msg += "Is a bot: " + target.isBot() + "\n";
			msg += "Status: " + Namer.presence(target.getPresence().getStatus()) + "\n";
			if(target.getPresence().getPlayingText().isPresent())
			{
				msg += "Game: " + target.getPresence().getPlayingText() + "\n";
			}
			if(target.getPresence().getStreamingUrl().isPresent())
			{
				msg += "Streaming URL: " + target.getPresence().getStreamingUrl().get() + "\n";
			}
			if(Utils.hasPermissionSomewhere(sender, channel, Permissions.MANAGE_ROLES))
			{
				msg += "User ID: " + target.getStringID() + "\n";
				msg += "Roles :\n";
				for(IRole role : target.getRolesForGuild(channel.getGuild()))
				{
					msg += "   - " + role.getName() + "\n";
				}
			}
			msg += "```";
		}
		for(IUser user : users)
		{
			msg += "```";
			msg += "User: " + user.getName() + "\n";
			msg += "Discr: " + user.getDiscriminator() + "\n";
			msg += "Display Name: " + user.getDisplayName(channel.getGuild()) + "\n";
			msg += "Is a bot: " + user.isBot() + "\n";
			msg += "Status: " + Namer.presence(user.getPresence().getStatus()) + "\n";
			if(target.getPresence().getPlayingText().isPresent())
			{
				msg += "Game: " + target.getPresence().getPlayingText() + "\n";
			}
			if(target.getPresence().getStreamingUrl().isPresent())
			{
				msg += "Streaming URL: " + target.getPresence().getStreamingUrl().get() + "\n";
			}
			if(Utils.hasPermissionSomewhere(sender, channel, Permissions.MANAGE_ROLES))
			{
				msg += "User ID: " + target.getStringID() + "\n";
				msg += "Roles :\n";
				for(IRole role : user.getRolesForGuild(channel.getGuild()))
				{
					msg += "   - " + role.getName() + "\n";
				}
			}
			msg += "```";
		}
		Utils.sendSafeMessages(channel, msg);
		return true;
	}
}
