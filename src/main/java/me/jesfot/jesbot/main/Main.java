package me.jesfot.jesbot.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

import me.jesfot.jesbot.JesBot;
import me.jesfot.jesbot.Statics;
import me.jesfot.jesbot.commands.CommandError;
import me.jesfot.jesbot.utils.MyLogger;
import sx.blah.discord.api.IDiscordClient;

public final class Main
{
	private static Scanner keyboard = null;
	
	public static void main(String[] args)
	{
		if (Statics.IS_DEAMON)
		{
			Main.keyboard = new Scanner(System.in);
		}
		
		JesBot bot = new JesBot();
		bot.init(Statics.IS_DEAMON);
		
		if (Statics.IS_DEAMON)
		{
			Main.deamon(bot);
		}
		
		
		if (Main.keyboard != null)
		{
			Main.keyboard.close();
		}
	}
	
	public static void deamon(JesBot bot)
	{
		boolean stop = false;
		String line = null;
		while (!stop)
		{
			System.out.print(Statics.INPUT_PROMPT);
			line = Main.keyboard.nextLine();
			
			if (line != null && line.equalsIgnoreCase("stop"))
			{
				bot.getReportManager().saveConfig();
				bot.getConfig().save();
				bot.getCommandHandler().clear();
				bot.getClient().logout();
				stop = true;
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException ignored)
				{}
			}
		}
	}
	
	public static abstract class ConsoleCommand
	{
		private final String commandName;
		private final String shortDescription;
		private final String description;
		private final String usage;
		
		private String fullCommand;
		
		private boolean activated;
		
		protected ConsoleCommand(final String command, final String shortDesc, final String longDesc, final String p_usage)
		{
			this.commandName = command;
			this.shortDescription = shortDesc;
			this.description = longDesc;
			this.usage = p_usage;
			this.activated = true;
		}
		
		protected final void disable()
		{
			this.activated = false;
		}
		
		final void setEnable(final boolean enable)
		{
			this.activated = enable;
		}
		
		public final boolean isDisabled()
		{
			return !this.activated;
		}
		
		public final String getName()
		{
			return this.commandName;
		}
		
		public final String getShortDescription()
		{
			return this.shortDescription;
		}
		
		public final String getFullDescription()
		{
			return this.description;
		}
		
		public final String getUsage()
		{
			return this.usage;
		}
		
		protected void setFullCommand(String something)
		{
			this.fullCommand = something;
		}
		
		public final boolean onCommand(String fullContents, IDiscordClient client)
		{
			if (!this.activated)
			{
				ConsoleCommand.logCommand("This command is not activated, sorry.");
				return false;
			}
			this.setFullCommand(fullContents);
			try
			{
				boolean res = this.execute(fullContents, client);
				if (res)
				{
					ConsoleCommand.logCommand("Command successfully executed.");
				}
				else
				{
					ConsoleCommand.logCommand("Command unsuccessfully executed.");
				}
			}
			catch (CommandError error)
			{
				ConsoleCommand.logCommand("Some error occured while executing your command :");
				ConsoleCommand.logCommand("Error reason : " + error.getMessage());
			}
			catch (NullPointerException nulex)
			{
				return false;
			}
			return false;
		}
		
		public abstract boolean execute(String fullContents, IDiscordClient client) throws CommandError;
		
		protected final List<String> getArguments()
		{
			List<String> result = new ArrayList<String>();
			String[] tmp = this.fullCommand.split(" ");
			for(int i = 1; i < tmp.length; i++)
			{
				String t = tmp[i];
				if(t.startsWith("\""))
				{
					i++;
					while(!tmp[i].endsWith("\"") && i < tmp.length)
					{
						t += " ";
						t += tmp[i];
						i++;
					}
					if(tmp[i].endsWith("\""))
					{
						t += " " + tmp[i];
					}
					result.add(t.substring(1, t.length() - 1));
				}
				else
				{
					result.add(t);
				}
			}
			return result;
		}
		
		protected final String compileFrom(int index)
		{
			String result;
			String[] tmp = this.getArguments().toArray(new String[]{});
			result = tmp[index];
			for(int i = (index + 1); i < tmp.length; i++)
			{
				result += " " + tmp[i];
			}
			return result;
		}
		
		private static final void logCommand(final String fullCmd)
		{
			MyLogger logger = MyLogger.getLogger(Statics.BOT_NAME, "ConsoleCommandManager");
			logger.log(Level.INFO, fullCmd);
		}
	}
}
