package me.jesfot.jesbot.reports;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import me.jesfot.jesbot.Statics;
import me.jesfot.jesbot.reports.Report.Status;
import me.jesfot.jesbot.utils.MyLogger;
import me.unei.configuration.api.format.INBTCompound;
import me.unei.configuration.api.impl.NBTConfig;
import me.unei.configuration.formats.nbtlib.TagCompound;

import sx.blah.discord.handle.obj.IGuild;

public class ReportManager
{
	public static final String model = "<guild>.<id>.<field>";
	
	private HashMap<String, HashMap<String, Report>> reports;
	
	private File save_folder = new File(".");
	private String fileName = "reports";
	
	public ReportManager()
	{
		this.reports = new HashMap<String, HashMap<String, Report>>();
	}
	
	public ReportManager(File dest_folder)
	{
		this();
		this.save_folder = dest_folder;
	}
	
	public ReportManager(File dest_folder, String file)
	{
		this(dest_folder);
		this.fileName = file;
	}
	
	public void readConfig()
	{
		NBTConfig saveData = new NBTConfig(this.save_folder, this.fileName);
		saveData.reload();
		saveData.save();
		/*
		 * if (this.save_folder != null && !this.save_folder.isDirectory()) { if
		 * (!this.save_folder.exists()) { this.save_folder.mkdirs(); } else {
		 * this.save_folder = this.save_folder.getParentFile();
		 * this.save_folder.mkdirs(); } }
		 */
		/*
		 * Configuration config = new Configuration(this.save_folder,
		 * "reports.cfg"); try { config.init(); } catch (FileNotFoundException
		 * e) { e.printStackTrace(); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
		//Set<Object> values = config.getProps().keySet();
		MyLogger logger = MyLogger.getLogger(Statics.BOT_NAME, "Report Manager");
		
		// Parsing Compound :
		
		Set<String> guilds = saveData.getKeys();
		
		for (String guild : guilds)
		{
			if (!this.reports.containsKey(guild))
			{
				logger.log(Level.CONFIG, "New guild reports data : \"" + guild + "\"");
				this.reports.put(guild, new HashMap<String, Report>());
			}
			
			HashMap<String, Report> guildsReps = this.reports.get(guild);
			INBTCompound guildComp = saveData.getTagCopy().getCompound(guild);
			Set<String> reportsID = guildComp.keySet();
			
			for (String reportID : reportsID)
			{
				if (!guildsReps.containsKey(reportID))
				{
					logger.log(Level.CONFIG, "New reports data : \"" + reportID + "\" for guild \"" + guild + "\"");
					guildsReps.put(reportID, new Report());
				}
				
				INBTCompound reportComp = guildComp.getCompound(reportID);
				Set<String> fields = reportComp.keySet();
				
				Report rep = guildsReps.get(reportID);
				for (String field : fields)
				{
					String missing = "";
					for (int i = field.length(); i < 11; i++)
					{
						missing += " ";
					}
					logger.log(Level.CONFIG, "Filling field '" + field.concat(missing) + "' of report \"" + reportID
							+ "\" : \"" + reportComp.getString(field) + "\";");
					rep.setField(field, reportComp.getString(field));
				}
			}
		}
		
		/*
		 * for (Object value : values) { if (!(value instanceof String)) {
		 * continue; } String str = new String(value.toString()); String[]
		 * splited = str.replace('.', '|').split("\\|"); if (splited.length < 3)
		 * { logger.info("Ignored key is : \"" + str +
		 * "\" Replaceing dots by '|' : \"" + str.replace('.', '|') +
		 * "\", length = " + splited.length + ";"); continue; } String guild =
		 * splited[0]; String id = splited[1]; String field = splited[2]; if
		 * (guild.length() != 18 || id.length() != 18) { logger.info(
		 * "Ignored key is : \"" + str + "\" guild length == " + guild.length()
		 * + ", id length == " + id.length() + ";"); continue; } if
		 * (!this.reports.containsKey(guild)) { logger.info(
		 * "New guild reports data : \"" + guild + "\"");
		 * this.reports.put(guild, new HashMap<String, Report>()); }
		 * HashMap<String, Report> guildsReps = this.reports.get(guild); if
		 * (!guildsReps.containsKey(id)) { guildsReps.put(id, new Report()); }
		 * Report rep = guildsReps.get(id); rep.setField(field,
		 * config.getProps().getProperty(str)); }
		 */
	}
	
	public void saveConfig()
	{
		NBTConfig saveData = new NBTConfig(this.save_folder, this.fileName);
		
		/*
		 * if (this.save_folder != null && !this.save_folder.isDirectory()) { if
		 * (!this.save_folder.exists()) { this.save_folder.mkdirs(); } else {
		 * this.save_folder = this.save_folder.getParentFile();
		 * this.save_folder.mkdirs(); } } Configuration config = new
		 * Configuration(this.save_folder, "reports.cfg"); try { config.init();
		 * } catch (Exception e) { e.printStackTrace(); return; }
		 */
		
		INBTCompound globalComp = new TagCompound();
		
		for (Entry<String, HashMap<String, Report>> guild : this.reports.entrySet())
		{
			INBTCompound guildComp = new TagCompound();
			
			for (Entry<String, Report> reportsID : guild.getValue().entrySet())
			{
				INBTCompound reportComp = new TagCompound();
				reportComp.setString("name", reportsID.getValue().getName());
				reportComp.setString("sender", reportsID.getValue().getSenderID());
				reportComp.setString("target", reportsID.getValue().getTargetID());
				reportComp.setString("message", reportsID.getValue().getMessageID());
				reportComp.setString("content", reportsID.getValue().getContent());
				reportComp.setString("channel", reportsID.getValue().getChannelID());
				reportComp.setString("reason", reportsID.getValue().getReasons());
				reportComp.setString("date", reportsID.getValue().getDate());
				reportComp.setString("status", reportsID.getValue().getStatus().id());
				
				guildComp.set(reportsID.getKey(), reportComp);
			}
			
			globalComp.set(guild.getKey(), guildComp);
		}
		
		saveData.setTagCopy(globalComp);
		saveData.save();
		
		/*
		 * config.getProps().clear(); for (Entry<String, HashMap<String,
		 * Report>> entry : this.reports.entrySet()) { for (Entry<String,
		 * Report> entryReps : entry.getValue().entrySet()) { String repl =
		 * ReportManager.model.replace("<guild>",
		 * entry.getKey()).replace("<id>", entryReps.getKey());
		 * config.getProps().setProperty(repl.replace("<field>", "name"),
		 * entryReps.getValue().getName());
		 * config.getProps().setProperty(repl.replace("<field>", "sender"),
		 * entryReps.getValue().getSenderID());
		 * config.getProps().setProperty(repl.replace("<field>", "target"),
		 * entryReps.getValue().getTargetID());
		 * config.getProps().setProperty(repl.replace("<field>", "message"),
		 * entryReps.getValue().getMessageID());
		 * config.getProps().setProperty(repl.replace("<field>", "content"),
		 * entryReps.getValue().getContent());
		 * config.getProps().setProperty(repl.replace("<field>", "channel"),
		 * entryReps.getValue().getChannelID());
		 * config.getProps().setProperty(repl.replace("<field>", "reason"),
		 * entryReps.getValue().getReasons());
		 * config.getProps().setProperty(repl.replace("<field>", "date"),
		 * entryReps.getValue().getDate());
		 * config.getProps().setProperty(repl.replace("<field>", "status"),
		 * entryReps.getValue().getStatus().id()); } } config.save();
		 */
	}
	
	public HashMap<String, Report> getReportsForGuild(IGuild guild)
	{
		return this.reports.get(guild.getStringID());
	}
	
	public Report getReport(IGuild guild, String id)
	{
		if (!this.reports.containsKey(guild.getStringID()))
		{
			return null;
		}
		return ReportManager.get(this.getReportsForGuild(guild), id);
	}
	
	public void remove(IGuild guild, String id)
	{
		if (!this.reports.containsKey(guild.getStringID()))
		{
			return;
		}
		Report rep = this.reports.get(guild.getStringID()).remove(id);
		rep.setStatus(Status.CANCELLED);
	}
	
	public void addReport(IGuild guild, String id, Report report)
	{
		if (!this.reports.containsKey(guild.getStringID()))
		{
			this.reports.put(guild.getStringID(), new HashMap<String, Report>());
		}
		this.getReportsForGuild(guild).put(id, report);
	}
	
	public static Report get(Map<String, Report> map, String key)
	{
		Report res = map.get(key);
		if (res == null)
		{
			for (Entry<String, Report> entry : map.entrySet())
			{
				if (entry.getValue().getName().equals(key))
				{
					res = entry.getValue();
					break;
				}
			}
		}
		return res;
	}
}
