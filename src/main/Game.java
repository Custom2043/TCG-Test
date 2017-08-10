package main;

import java.util.Iterator;

import carte.Cimetiere;
import carte.Effet.Checkable;
import carte.MainEffet;
import carte.ObjectSlot;
import carte.Slot;
import carte.YTBSlot;

public abstract class Game
{
	public int numberTurn = 1;
	public boolean basTurn;
	public final boolean basStart;
	public ObjectSlot[] objectSlots = new ObjectSlot[ObjectSlot.NUMBER * 2];
	public YTBSlot[] ytbSlots = new YTBSlot[YTBSlot.NUMBER * 2];
	public Cimetiere[] cimetiere = new Cimetiere[2];
	public final Player bas = new Player(true), haut = new Player(false);
	public Game(boolean bs)
	{
		basTurn = basStart = bs;
		
		for (int i = 0;i<ObjectSlot.NUMBER*2;i++)
			objectSlots[i] = new ObjectSlot(i < ObjectSlot.NUMBER ? haut : bas);
		for (int i = 0;i<YTBSlot.NUMBER*2;i++)
			ytbSlots[i] = new YTBSlot(i < YTBSlot.NUMBER ? haut : bas);
		
		cimetiere[0] = new Cimetiere(haut);
		cimetiere[1] = new Cimetiere(bas);
	}
	public void tick(long dif){}
	public Player getPlayingPlayer()
	{
		return basTurn ? bas : haut;
	} 
	public Player getNotPlayingPlayer()
	{
		return basTurn ? haut : bas;
	}
	public void passTurn()
	{
		basTurn = !basTurn;
		if (basTurn == basStart)
			numberTurn ++;
		getPlayingPlayer().setMana(numberTurn == 1 ? 2 : numberTurn);
		
		for (YTBSlot s : ytbSlots)
			if (s.getCarte() != null && s.possesseur == getPlayingPlayer())
				s.getCarte().newTurn();
		for (ObjectSlot s : objectSlots)
			if (s.getCarte() != null && s.possesseur == getPlayingPlayer())
				s.getCarte().newTurn();
		for (Iterator<MainEffet> iter = getPlayingPlayer().effets.iterator() ; iter.hasNext() ; )
			if (iter.next().newTurn())
				iter.remove();
	}
	public boolean foeProperty(Class<? extends Checkable> c)
	{
		for (YTBSlot s : ytbSlots)
			if (s.getCarte() != null && s.possesseur != getPlayingPlayer() && s.getCarte().hasProperty(c))
				return true;
		return false;
	}
	public Cimetiere getCim(boolean b)
	{
		return b ? cimetiere[1] : cimetiere[0];
	}
	public YTBSlot searchYTBSlot(Boolean player, Boolean empty)
	{
		for (YTBSlot s : ytbSlots)
			if (empty == null || empty == (s.getCarte() == null))
				if(player == null || player == (s.possesseur == getPlayingPlayer()))
					return s;
		return null;
	}
	public int getSlotId(Slot s)
	{
		for (int i=0;i<ytbSlots.length;i++)
			if (ytbSlots[i] == s)
				return i;
	
		for (int i=0;i<objectSlots.length;i++)
			if (objectSlots[i] == s)
				return i;
		
		for (int i=0;i<cimetiere.length;i++)
			if (cimetiere[i] == s)
				return i;
		
		return -1;
	}
	public abstract boolean random();
	public int remainingYTBSlot(Player p)
	{
		int i = 0;
		for (YTBSlot s : ytbSlots)
			if (s.possesseur == p && s.getCarte() == null)
				i ++;
		return i;
	}
	public Player getPlayer(boolean bas)
	{
		return bas ? this.bas : haut;
	}
}
