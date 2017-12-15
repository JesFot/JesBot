package me.jesfot.jesbot.commands;

import java.awt.Color;
import java.util.List;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.Statics;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class RuCommand extends BaseCommand
{
	private JesBot jesbot;
	
	public RuCommand(JesBot bot)
	{
		super("ru", "Uniques roles", "Used to manage personnal roles", "<com> ...");
		this.setAllowedForOwner(false);
		this.setMinimalPermission(null);
		this.jesbot = bot;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas) throws CommandError
	{
		if (!Boolean.parseBoolean(this.jesbot.getConfig().getProps().getProperty("PersonnalsRolesFor." + channel.getGuild().getStringID(), Boolean.toString(true))))
		{
			throw new CommandError("RU not allowed on this server", this);
		}
		List<String> args = this.getArguments();
		if (args.isEmpty())
		{
			this.jesbot.getConfig().setProperty("PersonnalsRolesFor." + channel.getGuild().getStringID(), Boolean.TRUE.toString());
			return false;
		}
		if (args.get(0).equalsIgnoreCase("add") && args.size() >= 2)
		{
			String roleName = this.compileFrom(1);
			IGuild guild = channel.getGuild();
			IRole role = null;
			
			if (!roleName.contains(Statics.PERSONNAL_ROLE_DESGNATOR))
			{
				roleName = roleName.concat(" ").concat(Statics.PERSONNAL_ROLE_DESGNATOR);
			}
			
			List<IRole> roles = guild.getRolesByName(roleName);
			if (roles == null || roles.isEmpty())
			{
				try
				{
					role = guild.createRole();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					throw new CommandError("Cannot create empty role", this);
				}
			}
			else
			{
				role = roles.get(0);
			}
			try
			{
				for (IRole pRole : sender.getRolesForGuild(guild))
				{
					if (pRole.getName().toLowerCase().contains(Statics.PERSONNAL_ROLE_DESGNATOR.toLowerCase()))
					{
						sender.removeRole(pRole);
						pRole.delete();
					}
				}
				role.changePermissions(guild.getEveryoneRole().getPermissions().clone());
				//role.changeColor(new Color(0, 0, 0, 0));
				role.changeName(roleName);
				role.changeMentionable(false);
				role.changeHoist(false);
				sender.addRole(role);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new CommandError("Cannot edit role [Missing bot permission]", this);
			}
			Utils.sendSafeMessages(channel, sender.mention(true) + " Succesfuly created new Unique role");
			Utils.deleteSafeMessages(datas);
		}
		else if (args.get(0).equalsIgnoreCase("color") && args.size() == 2)
		{
			try
			{
				for (IRole pRole : sender.getRolesForGuild(channel.getGuild()))
				{
					if (pRole.getName().toLowerCase().contains(Statics.PERSONNAL_ROLE_DESGNATOR.toLowerCase()))
					{
						pRole.changeColor(Color.getColor(args.get(1), pRole.getColor()));
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new CommandError("Cannot edit role [Missing bot permission]", this);
			}
			Utils.sendSafeMessages(channel, sender.mention(true) + " Successfuly changed color");
			Utils.deleteSafeMessages(datas);
			return true;
		}
		else if (args.get(0).equalsIgnoreCase("remove") && args.size() == 1)
		{
			try
			{
				for (IRole pRole : sender.getRolesForGuild(channel.getGuild()))
				{
					if (pRole.getName().toLowerCase().contains(Statics.PERSONNAL_ROLE_DESGNATOR.toLowerCase()))
					{
						sender.removeRole(pRole);
						pRole.delete();
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new CommandError("Cannot remove role [Missing bot permission]", this);
			}
			Utils.sendSafeMessages(channel, sender.mention(true) + " Successfuly remove your unique role(s)");
			Utils.deleteSafeMessages(datas);
			return true;
		}
		return true;
	}
}
