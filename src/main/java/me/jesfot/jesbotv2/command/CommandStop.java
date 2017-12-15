package me.jesfot.jesbotv2.command;

import me.jesfot.jesbotv2.JesBot;
import me.jesfot.jesbotv2.management.command.CommandBase;
import me.jesfot.jesbotv2.management.command.CommandContext;
import me.jesfot.jesbotv2.management.command.CommandError;
import me.jesfot.jesbotv2.management.command.CommandResult;
import me.jesfot.jesbotv2.management.command.CommandResult.CommandStatus;
import me.jesfot.jesbotv2.utils.Utils;

import sx.blah.discord.handle.obj.Permissions;

public class CommandStop extends CommandBase
{
	private JesBot bot;
	
	public CommandStop(JesBot jb)
	{
		super("/stop", "Stop the bot", "Terminate the bot procecss", "<cmd> [reason...]");
		this.setMinimalPermission(Permissions.ADMINISTRATOR);
		this.setOwnerBypass(true);
		this.bot = jb;
	}
	
	@Override
	protected boolean execute(CommandContext context, CommandResult state) throws CommandError
	{
		if (!Utils.isMyOwner(context.getSender().getDiscordUser()) && !context.getSender().isConsole())
		{
			state.setStatus(CommandStatus.Failure);
			return false;
		}
		if (context.hasOriginDiscordMessage())
		{
			Utils.deleteSafeMessages(context.getOriginDiscordMessage());
		}
		String reason = "R.A.S.";
		if (context.getArgs().size() > 0)
		{
			reason = context.compileArgumentsFrom(0);
		}
		context.getSender().sendMessage("Ok, I'm now going to sleep for a while...");
		this.bot.getLogger().warning("Normal shutdown : " + reason);
		this.bot.stop(reason);
		return true;
	}
}
