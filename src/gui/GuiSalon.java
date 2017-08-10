package gui;

import java.util.ArrayList;
import java.util.List;

import main.Client;
import main.OtherAccount;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import packet.DPacketCancel;
import packet.DPacketDefi;
import packet.SPacketAnswer;
import util.ScreenCoor;
import drawer.CustomDrawer;

public class GuiSalon extends Gui
{
	private static final int RIEN = 0, DEMANDE = 1, RECOIT = 2;
	
	private int state = RIEN;
	private String name = null;// = "a";
	
	public BasicBouton accept = new BasicBouton("Accepter", -1, ScreenCoor.screenFlat(.5f,.5f,0,0,-180,50,120,80), true),
					   refuse = new BasicBouton("Refuser", -2, ScreenCoor.screenFlat(.5f,.5f,0,0,60,50,120,80), true),
					   cancel = new BasicBouton("Annuler", -3, ScreenCoor.screenFlat(.5f,.5f,0,0,-60,50,120,80), true),
						deck  = new BasicBouton("Deck", -4, ScreenCoor.screenFlat(.5f,0,0,0,-60,60,120,80), true);
	
	List<String> names = new ArrayList<String>();
	
	public GuiSalon(){}
	
	@Override
	public void quit() {}

	@Override
	protected void drawBeforeButtons() 
	{
		boutons.clear();
		names.clear();
		CustomDrawer.drawString(100, 50, true, true, "En ligne : ", Drawer.font, Color.white);
		int i = 0;
		for (OtherAccount oa : Client.connected)
		{
			if (!oa.ingame)
			{
				CustomDrawer.drawRect(ScreenCoor.screenFlat(0,0,1,0,40,150+50*i, -80,50), Color.white);
				CustomDrawer.drawRect(ScreenCoor.screenFlat(0,0,1,0,41,151+50*i, -82,48), Color.black);
				CustomDrawer.drawString(100f, 175+50*i, false, true, oa.name, Drawer.font, Color.white);
				names.add(oa.name);
				boutons.add(new BasicBouton("Défier", i, ScreenCoor.screenFlat(1, 0, 0, 0, -160, 160+50*i, 100, 30), true));
				i++;
			}
		}
		CustomDrawer.drawString(Display.getWidth()-200, 50, true, true, "Ping : "+Client.compte.ping, Drawer.font, Color.white);
		if (state == RECOIT)
		{
			CustomDrawer.drawRect(ScreenCoor.screenFlat(.5f,.5f,0,0,-201,-151,402,302), Color.white);
			CustomDrawer.drawRect(ScreenCoor.screenFlat(.5f,.5f,0,0,-200,-150,400,300), Color.black);
			
			CustomDrawer.drawString(Display.getWidth()/2, Display.getHeight()/2-50, true, true, name, Drawer.font, Color.white);
			CustomDrawer.drawString(Display.getWidth()/2, Display.getHeight()/2, true, true, "vous defie", Drawer.font, Color.white);
			
			accept.draw();
			refuse.draw();
		}
		if (state == DEMANDE)
		{
			CustomDrawer.drawRect(ScreenCoor.screenFlat(.5f,.5f,0,0,-201,-151,402,302), Color.white);
			CustomDrawer.drawRect(ScreenCoor.screenFlat(.5f,.5f,0,0,-200,-150,400,300), Color.black);
			
			CustomDrawer.drawString(Display.getWidth()/2, Display.getHeight()/2-50, true, true, "En attente d'une", Drawer.font, Color.white);
			CustomDrawer.drawString(Display.getWidth()/2, Display.getHeight()/2, true, true, "réponse de "+name, Drawer.font, Color.white);
			
			cancel.draw();
		}
		deck.draw();
	}

	@Override
	protected void mouseEvent(int clicID, int X, int Y, boolean press,CustomBouton boutonOn) 
	{
		if (clicID == 0 && press)
		{
			if (deck.coor.isIn(X, Y))
			{
				if (state == DEMANDE)
					new DPacketCancel().send(Client.compte.socket);
				else if (state == RECOIT)
					new SPacketAnswer(name, false).send(Client.compte.socket);
				Client.setScreen(new GuiDeck());
			}
			else if (state == DEMANDE)
			{
				if (cancel.coor.isIn(X, Y))
				{
					cancel();
					new DPacketCancel().send(Client.compte.socket);
				}
			}
			else if (state == RECOIT)
			{
				if (accept.coor.isIn(X, Y))
					new SPacketAnswer(name, true).send(Client.compte.socket);
				if (refuse.coor.isIn(X, Y))
				{
					new SPacketAnswer(name, false).send(Client.compte.socket);
					cancel();
				}
			}
			else if (boutonOn != null)
			{
				state = DEMANDE;
				name = names.get(boutonOn.id);
				new DPacketDefi(name).send(Client.compte.socket);
			}
		}
	}

	@Override
	protected void keyboardEvent(char carac, int keyCode) {}
	public void inviteDuel(String p){state = RECOIT; name = p;}
	public void cancel(){state = RIEN; name = null;}
}
