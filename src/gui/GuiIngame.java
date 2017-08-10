package gui;

import java.util.ArrayList;

import main.Client;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import packet.DPacketEndTurn;
import packet.DPacketMovement;
import packet.DPacketObjectUse;
import packet.DPacketPoseObject;
import packet.DPacketPoseYTB;
import packet.DPacketRelease;
import packet.DPacketTechnique;
import packet.DPacketUse;
import sound.SoundManager;
import sounds.SoundSystem;
import util.CardPosition;
import util.MouseHelper;
import util.MovementCard;
import util.ScreenCoor;
import util.Translator;
import carte.CimetiereWrapper;
import carte.ClientObjectWrapper;
import carte.ClientYTBWrapper;
import carte.Object;
import carte.ObjectSlot;
import carte.Talent;
import carte.Technique;
import carte.Technique.TechniqueOnItself;
import carte.Technique.TechniqueOnSlot;
import carte.YTB;
import carte.YTBSlot;
import drawer.CustomDrawer;

public class GuiIngame extends Gui
{
	public int immobile = 0;
	public boolean moved = false;
	public ClientYTBWrapper[] ytbProps = new ClientYTBWrapper[YTBSlot.NUMBER * 2];
	public ClientObjectWrapper[] objectProps = new ClientObjectWrapper[ObjectSlot.NUMBER * 2];
	public CimetiereWrapper[] cimProps = new CimetiereWrapper[2];
	public ClientYTBWrapper YTBSlotPicked = null;
	public ClientObjectWrapper objectSlotPicked = null;
	public MovementCard picked = null;
	public MovementCard mainOn; 
	float angle;
	
	public GuiIngame()
	{
		for (int i = 0;i<ObjectSlot.NUMBER*2;i++)
			objectProps[i] = new ClientObjectWrapper(Client.game.objectSlots[i], getObjectSlotCoor(i >= ObjectSlot.NUMBER, i));
		for (int i = 0;i<YTBSlot.NUMBER*2;i++)
			ytbProps[i] = new ClientYTBWrapper(Client.game.ytbSlots[i], getYTBSlotCoor(i >= YTBSlot.NUMBER, i));

		cimProps[0] = new CimetiereWrapper(Client.game.cimetiere[0], getCimSlotCoor(false));
		cimProps[1] = new CimetiereWrapper(Client.game.cimetiere[1], getCimSlotCoor(true));
		
		boutons.add(new BasicBouton(Translator.translate("gui.endTurn"), 0, ScreenCoor.screen(.41f, .48f, .18f, .04f), true));

		SoundManager.stop();
		SoundSystem.play(SoundManager.battle);
	}
	
	@Override
	public void quit() {}

	@Override
	protected void drawAfterButtons()
	{
		if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1))
			immobile = 0;
		
		Drawer.drawBackCard(Client.game.bas, new CardPosition(getDeckCoor(true), 0, 0, 1), 1);
		Drawer.drawBackCard(Client.game.haut, new CardPosition(getDeckCoor(false), 0, 0, 1), 1);
		
		Color trans = new Color(1, 1, 1, 1/3f);
		
		for (ClientYTBWrapper s : ytbProps)
		{
			if (YTBSlotPicked != null && s.slot.getCarte() != null)
			{
				if (s == YTBSlotPicked)
					Drawer.drawRect(s.coor, Color.blue.multiply(trans));
				else if (YTBSlotPicked.slot.getCarte().canBeUseOnSlot(s.slot))
					Drawer.drawRect(s.coor, Color.yellow.multiply(trans));
				else
					Drawer.drawRect(s.coor, Color.red.multiply(trans));
			}
			if (objectSlotPicked != null && s.slot.getCarte() != null)
			{
				if (((Talent)(objectSlotPicked.slot.getCarte())).canBeUseOnSlot(s.slot))
					Drawer.drawRect(s.coor, Color.yellow.multiply(trans));
				else
					Drawer.drawRect(s.coor, Color.red.multiply(trans));
			}
			if (picked != null && picked.carte instanceof Technique  && s.slot.getCarte() != null)
			{
				if (((Technique)picked.carte).canBeUseOnSlot(s.slot))
					Drawer.drawRect(s.coor, Color.yellow.multiply(trans));
				else
					Drawer.drawRect(s.coor, Color.red.multiply(trans));
			}
		}
		for (ClientYTBWrapper s : ytbProps)
			if (!s.movement.inAnim())
				Drawer.drawYTBSlot(s);
		
		for (ClientObjectWrapper s : objectProps)
			if (!s.movement.inAnim())
				Drawer.drawObjectSlot(s);

		if (YTBSlotPicked != null)
			if (YTBSlotPicked.slot.getCarte().canBeUseOnPlayer(Client.game.bas))
				Drawer.drawRect(getPlayerCoor(true), Color.yellow.multiply(trans));
			else
				Drawer.drawRect(getPlayerCoor(true), Color.red.multiply(trans));
		
		
		if (YTBSlotPicked != null)
			if (YTBSlotPicked.slot.getCarte().canBeUseOnPlayer(Client.game.haut))
				Drawer.drawRect(getPlayerCoor(false), Color.yellow.multiply(trans));
			else
				Drawer.drawRect(getPlayerCoor(false), Color.red.multiply(trans));
		
		if (objectSlotPicked != null)
			if (objectSlotPicked.slot.getCarte().canBeUseOnPlayer(Client.game.bas))
				Drawer.drawRect(getPlayerCoor(true), Color.yellow.multiply(trans));
			else
				Drawer.drawRect(getPlayerCoor(true), Color.red.multiply(trans));
		
		if (objectSlotPicked != null)
			if (objectSlotPicked.slot.getCarte().canBeUseOnPlayer(Client.game.haut))
				Drawer.drawRect(getPlayerCoor(false), Color.yellow.multiply(trans));
			else
				Drawer.drawRect(getPlayerCoor(false), Color.red.multiply(trans));
		
		Drawer.drawPlayer(true);Drawer.drawPlayer(false); 
		
		for (MovementCard c : Client.game.basProps.main)
			Drawer.drawMovementCard(Client.game.bas, c.getCardPosition(), c.carte);
		
		for (MovementCard c : Client.game.hautProps.main)
			Drawer.drawMovementCard(Client.game.haut, c.getCardPosition(), c.carte);
		
		for (ClientYTBWrapper s : ytbProps)
			if (s.movement.inAnim())
				Drawer.drawYTBSlot(s);
		
		for (ClientObjectWrapper s : objectProps)
			if (s.movement.inAnim())
				Drawer.drawObjectSlot(s);
		
		for (CimetiereWrapper s : cimProps)
			Drawer.drawCimetiere(s);
		
		if (picked != null)
		{
			if (Client.game.basTurn)
				picked.toCancel(new CardPosition(ScreenCoor.screenGui(Mouse.getX() / (float)Display.getWidth(), MouseHelper.getYMouse() / (float)Display.getHeight(), 0, 0, -744/12f, -1034/12f, 744/6f, 1034/6f), 0, 0, 1.3f), 100);
			Drawer.drawMovementCard(Client.game.bas, picked.getCardPosition(), picked.carte);
		}
		
		CustomDrawer.drawString(Display.getWidth() - 200, Client.game.basTurn ? Display.getHeight()-175: 170, false, true, "Playing", Drawer.font, Color.white);
		
		if (YTBSlotPicked != null && moved)
			CustomDrawer.drawLine(YTBSlotPicked.coor.getMiddleX(), YTBSlotPicked.coor.getMiddleY(), MouseHelper.getXMouse(), MouseHelper.getYMouse(), Color.white);
		
		if (objectSlotPicked != null && moved)
			CustomDrawer.drawLine(objectSlotPicked.coor.getMiddleX(), objectSlotPicked.coor.getMiddleY(), MouseHelper.getXMouse(), MouseHelper.getYMouse(), Color.white);
		
		if (picked == null)
		{			
			MovementCard c = getCarteOn(Client.game.basProps.main);
			
			if (c != mainOn)
			{
				if (mainOn != null)
				{
					mainOn.toCancel(new CardPosition(getMainCarteScreenCoor(true, Client.game.basProps.main.size(), Client.game.basProps.main.indexOf(mainOn)), angle, 0, 1f), 150);
					mainOn = null;
				}
				if (c != null && c.ended())
				{
					mainOn = c;
					angle = c.getCardPosition().z;
					c.toCancel(new CardPosition(getMainCarteScreenCoor(true, Client.game.basProps.main.size(), Client.game.basProps.main.indexOf(c)).addYGui(-50), 0, 0, 1.2f), 100);
				}
			}
			if (c != null)
				if (immobile > 50)
				{
					CardPosition cp = c.getCardPosition();
					Drawer.drawZoomedCarte(Client.game.bas, c.carte, cp.coor);
				}
			
			if (immobile > 50)
			{
				for (ClientYTBWrapper s : ytbProps)
					if (s.coor.isIn(MouseHelper.getXMouse(), MouseHelper.getYMouse()) && s.slot.getCarte() != null)
						Drawer.drawZoomedCarte(s.slot.possesseur, s.slot.getCarte(), s.coor.addXGui(s.coor.getStartX() > Display.getWidth()/2 ? - 744/3f: 744/3f));
			
				for (ClientObjectWrapper s : objectProps)
					if (s.coor.isIn(MouseHelper.getXMouse(), MouseHelper.getYMouse()) && s.slot.getCarte() != null)
						Drawer.drawZoomedCarte(s.slot.possesseur, s.slot.getCarte(), s.coor.addXGui(s.coor.getStartX() > Display.getWidth()/2 ? - 744/3f: 744/3f));
			}
		}
		if (Mouse.isInsideWindow())
			immobile ++;
		else
			immobile = 0;
	}

	@Override
	protected void mouseEvent(int clicID, int X, int Y, boolean press, CustomBouton boutonOn)
	{
		immobile = 0;
		if (clicID == -1)
		{
			moved = true;
			if (picked != null && Client.game.basTurn)
				new DPacketMovement(X/((float)Display.getWidth()), Y/((float)Display.getHeight()), -1).send(Client.compte.socket);
		}
		if (clicID == 0 && press && Client.game.basTurn)
		{
			mainOn = null;
			moved = false;
			if (picked == null)
			{
				MovementCard mc = getCarteOn(Client.game.getPlayingPlayerWrapper().main);
				for (int i=0;i<Client.game.getPlayingPlayerWrapper().main.size();i++)
					if (Client.game.getPlayingPlayerWrapper().main.get(i) == mc)
					{
						picked = Client.game.getPlayingPlayerWrapper().main.get(i);
						Client.game.getPlayingPlayerWrapper().removeCarte(i);
						new DPacketMovement(((float)X)/Display.getWidth(), ((float)Y)/Display.getHeight(), i).send(Client.compte.socket);
						angle = getZRotMain(Client.game.basTurn, Client.game.getPlayingPlayerWrapper().main.size(), i);
					}
			}
			if (YTBSlotPicked == null)
			{
				ClientYTBWrapper s = searchYTB(X, Y, true, false);
				if (s != null && s.slot.getCarte().pouvoir.canAttack())
					YTBSlotPicked = s;
			}
			if (objectSlotPicked == null)
			{
				ClientObjectWrapper s = searchObject(X, Y, true, false);
				if (s != null && s.slot.getCarte() instanceof Talent && ((Talent)s.slot.getCarte()).pouvoir.use > 0)
					objectSlotPicked = s;
			}
			if (boutonOn != null && boutonOn.id == 0)
			{
				new DPacketEndTurn().send(Client.compte.socket);
				YTBSlotPicked = null;picked = null; objectSlotPicked = null;
			}
		}
		else if (clicID == 0 && !press && Client.game.basTurn) // Relachement
		{
			if (picked != null)
			{
				if (picked.carte instanceof YTB)
				{
					ClientYTBWrapper s = searchYTB(X, Y, null, null);
					if (s != null)
						new DPacketPoseYTB((YTB)picked.carte, Client.game.getSlotId(s.slot)).send(Client.compte.socket);
					else
					{
						releasePicked();
						new DPacketRelease().send(Client.compte.socket);
					}
				}
				else if (picked.carte instanceof Technique)
				{
					ClientYTBWrapper s = searchYTB(X, Y, null, null);
					if ((picked.carte instanceof TechniqueOnSlot) && s != null)
						new DPacketTechnique((Technique)picked.carte, Client.game.getSlotId(s.slot)).send(Client.compte.socket);
					else if ((picked.carte instanceof TechniqueOnItself) && Y < 0.75f * Display.getHeight())
						new DPacketTechnique((Technique)picked.carte, -1).send(Client.compte.socket);
					else
					{
						releasePicked();
						new DPacketRelease().send(Client.compte.socket);
					}
				}
				else if (picked.carte instanceof Object)
				{
					ClientObjectWrapper s = searchObject(X, Y, null, null);
					if (s != null)
						new DPacketPoseObject((Object)picked.carte, Client.game.getSlotId(s.slot)).send(Client.compte.socket);
					else
					{
						releasePicked();
						new DPacketRelease().send(Client.compte.socket);
					}
				}
			}
			if (YTBSlotPicked != null) // Attaquer
			{
				if (!moved)
				{
					int i = Client.game.getSlotId(YTBSlotPicked.slot);
					new DPacketUse(i, i).send(Client.compte.socket);
				}
				else
				{
					ClientYTBWrapper attacked = searchYTB(X, Y, null, false);
					if (attacked != YTBSlotPicked)
					{
						if (attacked != null && YTBSlotPicked.slot.getCarte().canBeUseOnSlot(attacked.slot))
							new DPacketUse(Client.game.getSlotId(YTBSlotPicked.slot), Client.game.getSlotId(attacked.slot)).send(Client.compte.socket);
						if (getPlayerCoor(true).isIn(X, Y))
							if (YTBSlotPicked.slot.getCarte().canBeUseOnPlayer(Client.game.bas))
								new DPacketUse(Client.game.getSlotId(YTBSlotPicked.slot), Client.game.getPlayingPlayer() == Client.game.bas ? -2 : -1).send(Client.compte.socket);
						if (getPlayerCoor(false).isIn(X, Y))
							if (YTBSlotPicked.slot.getCarte().canBeUseOnPlayer(Client.game.haut))
								new DPacketUse(Client.game.getSlotId(YTBSlotPicked.slot), Client.game.getPlayingPlayer() == Client.game.haut? -2 : -1).send(Client.compte.socket);
					}
				}
				YTBSlotPicked = null;
			}
			if (objectSlotPicked != null) // Utiliser
			{
				if (objectSlotPicked.slot.getCarte() instanceof Talent)
				{
					Talent talent = (Talent)objectSlotPicked.slot.getCarte();
					if (!moved)
					{
						int i = Client.game.getSlotId(talent.slot);
						new DPacketObjectUse(i, -3).send(Client.compte.socket);
					}
					else
					{
						ClientYTBWrapper attacked = searchYTB(X, Y, null, false);
						if (attacked != talent.slot.prop)
						{
							if (attacked != null && talent.canBeUseOnSlot(attacked.slot))
								new DPacketObjectUse(Client.game.getSlotId(talent.slot), Client.game.getSlotId(attacked.slot)).send(Client.compte.socket);
							if (getPlayerCoor(true).isIn(X, Y))
								if (talent.canBeUseOnPlayer(Client.game.bas))
									new DPacketObjectUse(Client.game.getSlotId(talent.slot), Client.game.getPlayingPlayer() == Client.game.bas ? -2 : -1).send(Client.compte.socket);
							if (getPlayerCoor(false).isIn(X, Y))
								if (talent.canBeUseOnPlayer(Client.game.haut))
									new DPacketObjectUse(Client.game.getSlotId(talent.slot), Client.game.getPlayingPlayer() == Client.game.haut? -2 : -1).send(Client.compte.socket);
						}
					}
				}
				objectSlotPicked = null;
			}
		}
	}

	@Override
	protected void keyboardEvent(char carac, int keyCode){}
	public static ScreenCoor getMainCarteScreenCoor(boolean bas, int mainSize, int i)
	{
		return retourneIf(bas, ScreenCoor.screenGui(.5f, 1f, 0, 0, 500/6f*(i-(mainSize-1)/2f)-744/12f, - 820/6f + Math.abs((i-(mainSize-1)/2f)*10), 744/6f, 1034/6f));
	}
	public static ScreenCoor retourne(ScreenCoor sc)
	{
		return sc.setY(1-(sc.yScreen+sc.hScreen), 1-(sc.yGui+sc.hGui),  1-(sc.yFlat+sc.hFlat));
	}
	public static ScreenCoor retourneIf(ScreenCoor sc)
	{
		return retourneIf(Client.game.getPlayingPlayer().bas, sc);
	}
	public static ScreenCoor retourneIf(boolean bas, ScreenCoor sc)
	{
		if (!bas)
			return sc.setYScreen(1-(sc.yScreen+sc.hScreen)).setYGui(1-(sc.yGui+sc.hGui));
		else
			return sc;
	}
	@Override
	protected void drawBeforeButtons() 
	{
		Drawer.drawBackground();
	}
	public ClientYTBWrapper searchYTB(int X, int Y, Boolean player, Boolean empty)
	{
		for (ClientYTBWrapper cp : ytbProps)
			if (cp.coor.isIn(X, Y))
				if (empty == null || empty == (cp.slot.getCarte() == null))
					if(player == null || player == (cp.slot.possesseur == Client.game.getPlayingPlayer()))
						return cp;
		return null;
	}
	public ClientObjectWrapper searchObject(int X, int Y, Boolean player, Boolean empty)
	{
		for (ClientObjectWrapper cp : objectProps)
			if (cp.coor.isIn(X, Y))
				if (empty == null || empty == (cp.slot.getCarte() == null))
					if(player == null || player == (cp.slot.possesseur == Client.game.getPlayingPlayer()))
						return cp;
		return null;
	}
	public void releasePicked()
	{
		if (picked != null)
		{
			int j = -1;
			for (int i=0;i<Client.game.getPlayingPlayerWrapper().main.size() && j == -1;i++)
			{
				if (GuiIngame.getMainCarteScreenCoor(Client.game.getPlayingPlayer().bas, Client.game.getPlayingPlayerWrapper().main.size(), i).getMiddleX() > picked.getCardPosition().coor.getMiddleX())
					j = i;
			}
			if (j == -1)
				j = Client.game.getPlayingPlayerWrapper().main.size();
			Client.game.getPlayingPlayerWrapper().addCarte(picked, j);
			picked = null;
		}
	}
	public static ScreenCoor getYTBSlotCoor(boolean bas, int id)
	{
		return retourneIf(bas, ScreenCoor.screenGui(.5f, .5f, 0, 0, 900/6f*(id % YTBSlot.NUMBER) - 744/12f - 2 * 900/6f, 56/6f, 744/6f,688/6f));
	}
	public static ScreenCoor getObjectSlotCoor(boolean bas, int id)
	{
		if (id % ObjectSlot.NUMBER < 2)
			return retourneIf(bas, ScreenCoor.screenGui(.5f, .5f, 0, 0, 900/6f*(id % ObjectSlot.NUMBER) - 744/12f - 2 * 900/6f, 56/6f*2+688/6f, 744/6f,688/6f));
		else
			return retourneIf(bas, ScreenCoor.screenGui(.5f, .5f, 0, 0, 900/6f*(id % ObjectSlot.NUMBER) - 744/12f - 900 / 6f, 56/6f*2+688/6f, 744/6f,688/6f));
	}
	public static ScreenCoor getCimSlotCoor(boolean bas)
	{
		return retourneIf(bas, ScreenCoor.screenGui(0, 1, 0, 0, 500/6f-744/12f, -1000/6f-1034/12f, 744/6f,1034/6f));
	}
	public static ScreenCoor getPlayerCoor(boolean bas)
	{
		return retourneIf(bas, ScreenCoor.screenGui(.5f, .5f, 0, 0, -744/12f, 56/6f*2+688/6f, 744/6f,688/6f));
	}
	public static float getZRotMain(boolean bas, int mainSize, int i)
	{
		return (bas ? 1 : -1) *(float)((i-(mainSize-1)/2f)*(Math.PI/45));
	}
	public static boolean isMouseOnCard(MovementCard mc)
	{
		CardPosition cp = mc.getCardPosition();
		float x = Mouse.getX() - cp.coor.getMiddleX();
		float y = MouseHelper.getYMouse() - cp.coor.getMiddleY();
		float X = (float) (x*Math.cos(-cp.z)-y*Math.sin(-cp.z));
		float Y = (float) (x*Math.sin(-cp.z)+y*Math.cos(-cp.z));
		return cp.coor.isIn(X + cp.coor.getMiddleX(), Y + cp.coor.getMiddleY());
	}
	public static MovementCard getCarteOn(ArrayList<MovementCard> l)
	{
		if (!Mouse.isInsideWindow())
			return null;
		MovementCard m = null;
		for (MovementCard mc : l)
			if (isMouseOnCard(mc))
				m = mc;
		return m;
	}
	public static ScreenCoor getDeckCoor(boolean bas)
	{
		return retourneIf(bas, ScreenCoor.screenGui(.9f, .85f, 0, 0, -744/12f, -1034/12f, 744/6f, 1034/6f));
	}
}
