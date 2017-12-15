package me.jesfot.jesbot;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Statics
{
	public static final String BOT_NAME = "JësBot";
	public static final String VERSION = "1.9.2";
	public static final String DISPLAYED_AUTHOR = "JësFot";
	public static final List<Long> AUTHORS_IDS = Collections.unmodifiableList(Arrays.asList(160793283314843649L));
	public static final List<String> AUTHORS = Arrays.asList("JësFot#5823|160793283314843649");
	
	@Deprecated
	public static final String TOKEN = "My Discord Bot Token";
	@Deprecated
	public static final String YT_API_KEY = "My Youtube API Key";
	
	/**
	 * see {@code model_secrets.cfg} file for how to setup your token
	 */
	public static final String SECRETS_FILE_NAME = "secrets.cfg";
	
	public static final boolean LOGIN_ON_START = true;
	public static final boolean IS_DEAMON = false;
	public static final String COMMAND_DESIGNATOR = "//";
	
	public static final String INPUT_PROMPT = "$> ";
	
	public static final String PERSONNAL_ROLE_DESGNATOR = "[RU]";
}