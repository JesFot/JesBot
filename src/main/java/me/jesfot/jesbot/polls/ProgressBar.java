package me.jesfot.jesbot.polls;

public class ProgressBar
{
	private char start, end;
	private char filler;
	private char percent;
	private int size;
	
	public ProgressBar(char p_start, char p_per, char p_filler, char p_ends, int p_size)
	{
		this.start = p_start;
		this.end = p_ends;
		this.filler = p_filler;
		this.percent = p_per;
		this.size = p_size;
	}
	
	public String getFor(int percentage)
	{
		String papa = "";
		papa += this.start;
		int fills = (percentage == 0 ? 0 : ((this.size * percentage) / 100));
		for(int i = 0; i < fills; i++)
		{
			papa += this.percent;
		}
		for(int i = fills; i < this.size; i++)
		{
			papa += this.filler;
		}
		papa += this.end;
		return papa;
	}
}
