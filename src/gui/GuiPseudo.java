package gui;

import main.Client;
import main.ClientAccount;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import util.ScreenCoor;
import drawer.CustomDrawer;

public class GuiPseudo extends Gui
{
	public GuiPseudo()
	{
		boutons.add(new ZoneTexte("Pseudo", 0, ScreenCoor.screen(.3f, .45f, .4f, .1f), true));
		if (Client.admin)
			boutons.add(new BasicBouton("Créer le serveur", 2, ScreenCoor.screen(.4f, .65f, .2f, .1f), false));
		boutons.add(new BasicBouton("Rejoindre le serveur", 1, ScreenCoor.screen(.4f, .8f, .2f, .1f), false));
	}
	
	public void quit() {}

	protected void drawBeforeButtons() 
	{
		CustomDrawer.drawString(Display.getWidth()/2, Display.getHeight() * .3f, true, true, "Entrez votre pseudo", Drawer.font, Color.white);
	}

	protected void keyboardEvent(char arg0, int arg1) 
	{
		if (boutons.get(0).getTexte().texte.length() > 0 && !boutons.get(0).getTexte().texte.contains(" ") && boutons.get(0).getTexte().texte.length() < 15)
		{
			boutons.get(1).activ();
			if (Client.admin)
				boutons.get(2).activ();
		}
		else
		{
			boutons.get(1).desactiv();
			if (Client.admin)
				boutons.get(2).desactiv();
		}
	}

	protected void mouseEvent(int clicID, int X, int Y, boolean press, CustomBouton boutonOn) 
	{
		if (clicID == 0 && press)
		{
			if (Client.admin && boutonOn != null && boutonOn.id == 2 && boutonOn.isActiv)
			{
				Client.compte = new ClientAccount(boutons.get(0).getTexte().texte, null);
				Client.createLocalServer();
				Client.setScreen(new GuiSalon());
			}
			if (boutonOn != null && boutonOn.id == 1 && boutonOn.isActiv)
			{
				Client.compte = new ClientAccount(boutons.get(0).getTexte().texte, null);
				if (Client.admin)
					Client.joinDistantServer("localhost");
				else
					Client.joinDistantServer("62.210.233.80");
				Client.setScreen(new GuiSalon());
			}
		}
	}
}
