package gui;

import main.Client;
import sound.SoundManager;
import sounds.SoundSystem;
import util.ScreenCoor;
import util.Translator;
import value.MultipleStateValue;
import value.SliderValue;

public class GuiSoundTest extends Gui
{
	public static SliderValue volumeJeu = new SliderValue(0, 100, 20),
							  volumeMusic = new SliderValue(0,100,20),
							  volumeSound = new SliderValue(0,100,20);
	public GuiSoundTest()
	{
		this.boutons.add(new BasicSlider(Translator.translate("gui.sound.mainVolume"), 0, ScreenCoor.screenGui(.5f, .3f, 0, 0, -300, 0, 600, 40), volumeJeu));
		this.boutons.add(new BasicSlider(Translator.translate("gui.sound.musicVolume"), 1, ScreenCoor.screenGui(.5f, .3f, 0, 0, -300, 60, 600, 40), volumeMusic));
		this.boutons.add(new BasicSlider(Translator.translate("gui.sound.soundVolume"), 2, ScreenCoor.screenGui(.5f, .3f, 0, 0, -300, 120, 600, 40), volumeSound));

		this.boutons.add(new BasicBouton(Translator.translate("gui.back"), 4, ScreenCoor.screenGui(.5f, .4f, 0, 0, -150, 260, 300, 50), true));
	
		this.boutons.add(new BasicMultipleValueButton("gui.lang", 5, ScreenCoor.screenGui(.5f, .15f, 0, 0, -250, -40, 500, 80), new MultipleStateValue(Translator.languageNumber(), Translator.languages)));
	}
	@Override
	public void quit() {
	}

	@Override
	protected void drawBeforeButtons() {
		SoundSystem.setVolume(SoundManager.battle, volumeJeu.value * volumeMusic.getPourcent());
		SoundSystem.setVolume(SoundManager.menu, volumeJeu.getPourcent() * volumeMusic.getPourcent());
		SoundSystem.setVolume(SoundManager.win, volumeJeu.getPourcent() * volumeMusic.getPourcent());
		SoundSystem.setVolume(SoundManager.lose, volumeJeu.getPourcent() * volumeMusic.getPourcent());
	}

	@Override
	protected void mouseEvent(int clicID, int X, int Y, boolean press,
			CustomBouton boutonOn) {
		if (boutonOn != null && clicID == 0 && press)
		{
			if (boutonOn.id == 4)
				Client.setScreen(new GuiMainMenu());
		}
	}

	@Override
	protected void keyboardEvent(char carac, int keyCode) {
		
	}

}
