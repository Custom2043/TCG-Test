package sound;

import sounds.CodecOgg;
import sounds.SoundSystem;
import util.Logger;

public class SoundManager 
{
	public static int battle, lose, win, menu;
	
	public static void init()
	{
		SoundSystem.init();

		SoundSystem.setDefaultCodec(CodecOgg.class);
		
		Logger.setLoggerProperties(true, true, false, true);
		
		battle = SoundSystem.newSource(true, true, "Ressources/sounds/BATTLE_MASTER.ogg", true);
		lose = SoundSystem.newSource(true, true, "Ressources/sounds/LOSE_MASTER.ogg", true);
		win = SoundSystem.newSource(true, true, "Ressources/sounds/WIN_MASTER.ogg", true);
		menu = SoundSystem.newSource(true, true, "Ressources/sounds/MAIN_MASTER.ogg", true);
		
		SoundSystem.setVolume(SoundManager.battle, 0.04f);
		SoundSystem.setVolume(SoundManager.menu, 0.04f);
		SoundSystem.setVolume(SoundManager.win, 0.04f);
		SoundSystem.setVolume(SoundManager.lose, 0.04f);
	}
	public static void play(int i)
	{
		stop();
		SoundSystem.play(i);
	}
	public static void stop()
	{
		SoundSystem.stop(battle);
		SoundSystem.stop(menu);
		SoundSystem.stop(win);
		SoundSystem.stop(lose);
	}
	public static void quit()
	{
		SoundSystem.quit();
	}
}
