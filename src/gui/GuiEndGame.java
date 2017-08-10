package gui;

import main.Client;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import sound.SoundManager;
import sounds.SoundSystem;
import util.ScreenCoor;
import drawer.CustomDrawer;

public class GuiEndGame extends Gui
{
	final boolean bas;
	public GuiEndGame(boolean b)
	{
		bas = b;
		boutons.add(new BasicBouton("gui.back", 0, ScreenCoor.screenFlat(.5f,.7f,0,0,-100,-40,200,80), true));
	
		SoundManager.stop();
		SoundSystem.play(bas ? SoundManager.win : SoundManager.lose);
	}
	public void quit() {
		SoundManager.stop();SoundManager.play(SoundManager.menu);
	}
	protected void drawBeforeButtons() 
	{
		CustomDrawer.drawString(Display.getWidth()/2, Display.getHeight()/2, true, true, bas ? "Vous avez gagné !" : "Vous avez perdu", Drawer.font, Color.white);
	}

	@Override
	protected void mouseEvent(int clicID, int X, int Y, boolean press,
			CustomBouton boutonOn) 
	{
		if (boutonOn != null && press && clicID == 0)
			Client.setScreen(new GuiSalon());
	}

	@Override
	protected void keyboardEvent(char carac, int keyCode) {
	}
	
}
