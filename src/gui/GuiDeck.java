package gui;

import java.util.ArrayList;
import java.util.Iterator;

import main.Client;
import main.Deck;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import packet.DPacketDecks;
import util.CardPosition;
import util.MouseHelper;
import util.MovementCard;
import util.ScreenCoor;
import carte.CarteList;

public class GuiDeck extends Gui
{
	public int height, immobile = 0;
	public int of = 0, to = 0;//offset
	public ArrayList<MovementCard> cards = new ArrayList<MovementCard>();
	public Deck deck = Client.compte.decks.get(Client.compte.currentDeck);
	public GuiDeck()
	{
		update();
		boutons.add(new BasicBouton("Quitter", 0, ScreenCoor.screen(0.01f,.95f,0.08f,0.04f), true));
		boutons.add(new BasicBouton("Gauche", 1, ScreenCoor.screen(0.05f,.01f,0.1f,0.04f), true));
		boutons.add(new BasicBouton("Droite", 2, ScreenCoor.screen(0.7f,.01f,0.1f,0.04f), true));
		boutons.add(new BasicBouton("Ajouter", 3, ScreenCoor.screen(0.01f,.85f,0.08f,0.04f), true));
		boutons.add(new BasicBouton("Supprimer", 4, ScreenCoor.screen(0.01f,.9f,0.08f,0.04f), true));
	}
	public void quit() {}

	@Override
	protected void drawBeforeButtons() 
	{
		if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1))
			immobile = 0;
		of += to;
		if (of < 0)
			of = 0;
		if (height != Display.getHeight())
			update();
		for (MovementCard mc : cards)
		{
			CardPosition cp = mc.getCardPosition();
			Drawer.drawMovementCard(null, new CardPosition(cp.coor.addXGui(-of), cp.z, cp.y, cp.gui), mc.carte);
		}
		Drawer.drawRect(ScreenCoor.screen(.85f, 0, .15f, 1f), Color.white);
		int offset = 0;
		for (int i=0;i<deck.cards.size();i++)
			if (!deck.before(i, deck.cards.get(i)))
			{
				Drawer.drawString((int)(.86f*Display.getWidth()), 20+25*offset, false, false, deck.cards.get(i).name + (deck.after(i, deck.cards.get(i)) ? " x2" : ""), BasicBouton.font, Color.black);
				offset ++;
			}
		
		if (immobile > 30)
			for (MovementCard mc : cards)
			{
				CardPosition cp = mc.getCardPosition();
				if (MouseHelper.getXMouse() < .85f * Display.getWidth() && MouseHelper.getXMouse() > .1f * Display.getWidth())
					if (cp.coor.addXGui(-of).isIn(MouseHelper.getXMouse(), MouseHelper.getYMouse()))
						Drawer.drawZoomedCarte(null, mc.carte, cp.coor.addXGui(- of + ((cp.coor.getStartX()-of > Display.getWidth()/2) ? - 744/3f: 744/3f)));
			}
		
		Drawer.drawRect(ScreenCoor.screen(0, 0, .1f, 1f), Color.white);

		for (int i=1;i<=Client.compte.decks.size();i++)
		{
			if (i-1 == Client.compte.currentDeck)
				Drawer.drawRect(ScreenCoor.screenGui(0, 0, .1f, 0, 0, 20+25*i, 0, 20), Color.gray);
			Drawer.drawString((int)(.01f*Display.getWidth()), 20+25*i, false, false, "Deck "+i, BasicBouton.font, Color.black);
		}
		
		if (Mouse.isInsideWindow())
			immobile ++;
		else
			immobile = 0;
	}

	private void update()
	{
		cards.clear();
		height = Display.getHeight();
		int max = (int)(Display.getHeight() / (1200d/6));
		int bord = (int)(Display.getHeight() % (1200d/6)) / 2;
		for (int i=0;i<CarteList.getListSize();i++)
			cards.add(new MovementCard(new CardPosition(ScreenCoor.screenGui(.1f,0,0,0,900/6f*(i/max), bord + (i%max)*(1200/6f), 744/6f, 1034/6f), 0, 0, 1),CarteList.getCarteList(i).instance(null)));
	}
	
	@Override
	protected void mouseEvent(int clicID, int X, int Y, boolean press,CustomBouton boutonOn) 
	{
		immobile = 0;
		if (clicID == 0 && !press)
			to = 0;
		if (clicID == 0 && press)
		{
			if (boutonOn != null)//Quitter
			{
				if (boutonOn.id == 0)
				{
					new DPacketDecks(Client.compte.decks, Client.compte.currentDeck).send(Client.compte.socket);
					Client.setScreen(new GuiSalon());
				}
				else if (boutonOn.id == 1)
					to = -7;
				else if (boutonOn.id == 2)
					to = 7;
				else if (boutonOn.id == 3)
					Client.compte.decks.add(new Deck());
				else if (boutonOn.id == 4)
				{
					if (Client.compte.decks.size() > 1)
						Client.compte.decks.remove(Client.compte.currentDeck);
					if (Client.compte.currentDeck == Client.compte.decks.size())
						Client.compte.currentDeck --;
					deck = Client.compte.decks.get(Client.compte.currentDeck);
				}
			}
			else if (X >= .85*Display.getWidth())
			{
				int id = (MouseHelper.getYEventMouse()-20)/25; int offset = 0, i = 0;
				for (Iterator<CarteList> iter = deck.cards.iterator() ; iter.hasNext() && offset <= id; )
				{
					CarteList cl = iter.next();
					if (offset == id && !deck.before(i, cl))
					{
						iter.remove();
						offset ++;
					}
					else if (!deck.before(i, cl))
						offset ++;
					i++;
				}
			}
			else if (X <= .1*Display.getWidth())
			{
				int i = (MouseHelper.getYMouse() - 20)/25;
				if (i > 0 && i <= Client.compte.decks.size())
				{
					deck = Client.compte.decks.get(i-1);
					Client.compte.currentDeck = i - 1;
				}
			}
			else
				for (MovementCard mc : cards)
					if (mc.getCardPosition().coor.addXGui(-of).isIn(X, MouseHelper.getYEventMouse()))
					{
						int i = deck.numberOf(CarteList.getCarteForClass(mc.carte.getClass()));
						if (i < 2 && deck.cards.size()<30)
						{
							if (i == 0)
								deck.cards.add(CarteList.getCarteForClass(mc.carte.getClass()));
							else // i == 1
								deck.cards.add(deck.cards.indexOf(CarteList.getCarteForClass(mc.carte.getClass()))+1, CarteList.getCarteForClass(mc.carte.getClass()));
						}
					}
		}
	}

	@Override
	protected void keyboardEvent(char carac, int keyCode) {
		
	}

}
