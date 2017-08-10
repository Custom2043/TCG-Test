package gui;

import main.Client;
import main.Main;
import util.ScreenCoor;
import util.TextureCoor;
import util.Translator;
import drawer.CustomDrawer;

public class GuiMainMenu extends Gui
{
	public GuiMainMenu()
	{
		this.boutons.add(new BasicBouton(Translator.translate("gui.button.play"), 0, ScreenCoor.screenGui(.25f,.7f,0,0,-100,-40,200, 80), true));
		this.boutons.add(new BasicBouton(Translator.translate("gui.button.options"), 1, ScreenCoor.screenGui(.5f,.7f,0,0,-100,-40,200,80), true));
		this.boutons.add(new BasicBouton(Translator.translate("gui.button.quit"), 2, ScreenCoor.screenGui(.75f,.7f,0,0,-100, -40,200,80), true));

	}
	@Override
	public void quit(){}
	@Override
	public void drawBeforeButtons()
	{
		Drawer.drawBackground();
		CustomDrawer.blit(Drawer.TITLE, ScreenCoor.screenFlat(0.5f, .3f,0,0,-168,-104,316,208), TextureCoor.allPicture);
	}

	@Override
	protected void keyboardEvent(char carac, int keyCode) {}

	@Override
	protected void mouseEvent(int clicID, int X, int Y, boolean press, CustomBouton boutonOn)
	{
		if (boutonOn != null && press && clicID == 0)
		{
			if (boutonOn.id == 0)
				Client.setScreen(new GuiPseudo());
			if (boutonOn.id == 1)
				Client.setScreen(new GuiSoundTest());//this.state = INTRANSITIONDOWN;
			if (boutonOn.id == 2)
				Main.stop();
		}
	}
}
