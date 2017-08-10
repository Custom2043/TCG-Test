package main;

import util.CustomTimer;
import util.TimeSection;

public abstract class Main 
{
	public static final int PORT = 26302;
	public static final String gameName = "BattleTube";
	public static final int milliEntreTick = 20; // 50 par secondes
	public static boolean continu = true;
	protected static CustomTimer timer;
	public static boolean isClient;
	public static BattleTubeServer server = null;
	public static int NOMBRECARTE = 151;
	
	public static final int PINGPERIOD = 500, TIMEOUT = 50000;
	
	protected Main()
	{
		isClient = isClient();
		timer = new CustomTimer();
	}
	protected void run()
	{
		while (continu)
		{
			if (timer.getDifference() >= milliEntreTick)
				try
				{
					long dif = timer.getDifference();
					timer.resetUnderATick(milliEntreTick);
					if (server != null)
						server.tick(dif);
					tick(dif);
				}
				catch(Exception e){e.printStackTrace();System.out.println("Main ; Fail in drawer render");Main.stop();}
			sleep(Math.max(0, milliEntreTick - timer.getDifference()));
		}
		quit();
	}
	
	protected abstract void tick(long dif);
	protected abstract void quit();
	protected abstract boolean isClient();
	
	
	public static void sleep(long milli)
	{
		try
		{
			TimeSection.beginSection(TimeSection.REPOS);
			Thread.sleep(milli);
		}
		catch(Exception e){}
	}
	public static void stop()
	{
		continu = false;
	}
}
