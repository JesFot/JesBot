package me.jesfot.jesbot.commands.mychan;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.commands.BaseCommand;
import me.jesfot.jesbot.commands.CommandError;
import me.jesfot.jesbot.utils.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class ChanPermsCommand extends BaseCommand
{
	public static final int[] Limitations = {5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
	private JesBot jesbot;
	
	public ChanPermsCommand(JesBot bot)
	{
		super("perma", "Manage main channel", "Manage the main channel permissions", "<cmd> [<perm_nb> ...]");
		this.setAllowedForOwner(true);
		this.setMinimalPermission(Permissions.MANAGE_PERMISSIONS);
		this.jesbot = bot;
	}
	
	@Override
	public boolean execute(IUser sender, String fullContents, IChannel channel, IMessage datas) throws CommandError
	{
		IChannel target = null;
		String tar = this.jesbot.getConfig().getProps().getProperty("channel.main." + datas.getGuild().getStringID());
		if (tar != null)
		{
			target = datas.getGuild().getChannelByID(Long.parseUnsignedLong(tar));
		}
		if (target == null)
		{
			Utils.deleteSafeMessages(datas);
			Utils.sendSafeMessages(channel, sender.mention(true) + " Cannot found the main channel for this guild.");
			return false;
		}
		Utils.deleteSafeMessages(datas);
		
		List<String> args = this.getArguments();
		if (args.size() == 0)
		{
			String msg;
			msg = sender.mention() + " Permissions are :\n";
			for (Permissions perm : Permissions.values())
			{
				int	idx = Arrays.binarySearch(ChanPermsCommand.Limitations, perm.ordinal());
				if (idx >= 0 && idx < ChanPermsCommand.Limitations.length && ChanPermsCommand.Limitations[idx] == perm.ordinal())
				{
					msg += " " + perm.ordinal() + " - " + perm.name() + "\n";
				}
			}
			Utils.sendSafeMessages(channel, msg);
			return true;
		}
		boolean removal = false;
		Permissions selected;
		if (args.get(0).startsWith("-") || args.get(0).startsWith("R"))
		{
			removal = true;
			args.set(0, args.get(0).substring(1));
		}
		int	idx = Arrays.binarySearch(ChanPermsCommand.Limitations, Utils.toInt(args.get(0), 0));
		if (!(idx >= 0 && idx < ChanPermsCommand.Limitations.length && ChanPermsCommand.Limitations[idx] == Utils.toInt(args.get(0), 0)))
		{
			Utils.sendSafeMessages(channel, sender.mention() + " Wrong permission !");
			return false;
		}
		selected = Permissions.values()[ChanPermsCommand.Limitations[idx]];
		if (args.size() == 1)
		{
			String msg;
			msg = sender.mention() + " Groups are :\n";
			for (IRole role : target.getGuild().getRoles())
			{
				if (!role.isDeleted() && !role.isEveryoneRole() && !role.getPermissions().contains(Permissions.ADMINISTRATOR))
				{
					msg += " - " + role.getName() + " (" + role.getStringID() + ")\n";
				}
			}
			Utils.sendSafeMessages(channel, msg);
			return true;
		}
		IRole selected_role = null;
		IUser selected_user = null;
		if (args.get(1).equalsIgnoreCase("all"))
		{
			selected_role = target.getGuild().getEveryoneRole();
		}
		else
		{
			try
			{
				selected_role = target.getGuild().getRoleByID(Long.valueOf(args.get(1)));
			}
			catch (Exception e)
			{
				List<IRole> rls = target.getGuild().getRolesByName(args.get(1));
				if (rls != null && rls.size() > 0)
				{
					selected_role = rls.get(0);
				}
			}
		}
		if (selected_role == null)
		{
			selected_user = Utils.getUserById(target.getGuild(), args.get(1));
		}
		if (selected_role == null && (selected_user == null || Utils.isMyOwner(selected_user) || Utils.isMe(selected_user, channel.getClient())))
		{
			Utils.sendSafeMessages(channel, sender.mention() + " Wrong group or User !");
			return false;
		}
		if (selected_user != null)
		{
			this.change_user_perm(target, selected, selected_user, removal);
		}
		else
		{
			this.change_role_perm(target, selected, selected_role, removal);
		}
		return true;
	}
	
	private void change_user_perm(IChannel channel, Permissions perm, IUser user, boolean to_rm)
	{
		EnumSet<Permissions> perms_ch = channel.getModifiedPermissions(user);
		if (!to_rm)
		{
			perms_ch.add(perm);
			//channel.overrideUserPermissions(user, perms_ch, null);
		}
		else
		{
			perms_ch.remove(perm);
			/*channel.overrideUserPermissions(user, null, perms_ch);
			if (channel.getModifiedPermissions(user).isEmpty())
			{
				channel.removePermissionsOverride(user);
			}*/
		}
		channel.overrideUserPermissions(user, perms_ch, null);
	}
	
	private void change_role_perm(IChannel channel, Permissions perm, IRole role, boolean to_rm)
	{
		EnumSet<Permissions> perms_ch = channel.getModifiedPermissions(role);
		//EnumSet<Permissions> perms_ch = EnumSet.of(perm);
		if (!to_rm)
		{
			perms_ch.add(perm);
			//channel.overrideRolePermissions(role, perms_ch, null);
		}
		else
		{
			perms_ch.remove(perm);
			/*channel.overrideRolePermissions(role, null, perms_ch);
			if (channel.getModifiedPermissions(role).isEmpty())
			{
				channel.removePermissionsOverride(role);
			}*/
		}
		channel.overrideRolePermissions(role, perms_ch, null);
	}
}
