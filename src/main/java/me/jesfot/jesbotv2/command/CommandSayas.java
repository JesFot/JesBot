package me.jesfot.jesbotv2.command;

import me.jesfot.jesbotv2.JesBot;
import me.jesfot.jesbotv2.management.command.CommandBase;
import me.jesfot.jesbotv2.management.command.CommandContext;
import me.jesfot.jesbotv2.management.command.CommandError;
import me.jesfot.jesbotv2.management.command.CommandResult;
import me.jesfot.jesbotv2.management.command.CommandResult.CommandStatus;
import me.jesfot.jesbotv2.utils.Utils;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;

public class CommandSayas extends CommandBase
{
	public CommandSayas()
	{
		super("/sayas", "Say something as bot", "Say something throught the bot in channel you want",
				"<cmd> [[g <guild>] c <channel>] <msg...>");
		this.setMinimalPermission(Permissions.MANAGE_MESSAGES);
		this.setOwnerBypass(true);
	}
	
	@Override
	protected boolean execute(CommandContext context, CommandResult state) throws CommandError
	{
		if (context.getArgs().size() < 1)
		{
			state.setStatus(CommandStatus.Failure);
			return false;
		}
		if (context.hasOriginDiscordMessage())
		{
			if (Utils.deleteSafeMessages(context.getOriginDiscordMessage()) != 0)
			{
				state.setStatus(CommandStatus.Failure);
			}
		}
		IChannel destChannel = context.getOriginDiscordChannel();
		IGuild destGuild = (destChannel != null ? destChannel.getGuild() : null);
		String message = null;
		if (context.getArgument(0).equalsIgnoreCase("g") && context.getArgs().size() >= 3)
		{
			String guild = context.getArgument(1);
			IGuild serv = Utils.getGuildAnyWay(JesBot.getInstance().getClient(), guild);
			if (serv != null)
			{
				destGuild = serv;
			}
			if (context.getArgument(2).equalsIgnoreCase("c") && context.getArgs().size() >= 5)
			{
				String channel = context.getArgument(3);
				IChannel chan = Utils.getChannelAnyWay(destGuild, channel);
				if (chan != null)
				{
					destChannel = chan;
				}
				message = context.compileArgumentsFrom(4);
			}
			else
			{
				message = context.compileArgumentsFrom(2);
			}
		}
		else if (context.getArgument(0).equalsIgnoreCase("c") && context.getArgs().size() >= 3)
		{
			String channel = context.getArgument(1);
			IChannel chan = Utils.getChannelAnyWay(destGuild, channel);
			if (chan != null)
			{
				destChannel = chan;
			}
			message = context.compileArgumentsFrom(2);
		}
		else
		{
			message = context.compileArgumentsFrom(0);
		}
		if (destChannel != null && message != null && !message.isEmpty())
		{
			if (Utils.sendSafeMessages(destChannel, message) != null)
			{
				state.setStatus(CommandStatus.Success);
			}
			else
			{
				state.setStatus(CommandStatus.Failure);
			}
		}
		return false;
	}
}
