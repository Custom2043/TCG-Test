package util;

import org.newdawn.slick.Color;

public class TimeSection 
{
	private static int section;
	public static final String[] sectionNames = new String[]{"Render","Client logic","Sleep time", "Packet", "Update"};
	private static CustomTimer timer = new CustomTimer();
	public static int RENDER = 0,
					  CLIENT_LOGIC = 1,
					  REPOS = 2,
					  PACKET = 3,
					  UPDATE = 4,
					  SECTION_NUMBER = 5;
	public static int[] times = new int[SECTION_NUMBER];
	public static int[] last = new int[SECTION_NUMBER];
	public static void beginSection(int sec)
	{
		if (section != -1)
			times[section] += timer.getDifference();
		section = sec;
		timer.set0();
	}
	public static Color getColor(int sec)
	{
		if (sec == RENDER)
			return Color.blue;
		else if (sec == CLIENT_LOGIC)
			return Color.red;
		else if (sec == REPOS)
			return Color.green;
		else if (sec == PACKET)
			return Color.orange;
		else if (sec == UPDATE)
			return Color.pink;
		else
			return Color.white;
	}
	public static void setLast()
	{
		times[section] += timer.getDifference();
		timer.set0();
		last = times.clone();
		for (int i=0;i<SECTION_NUMBER;i++)
			times[i] = 0;
	}
}

