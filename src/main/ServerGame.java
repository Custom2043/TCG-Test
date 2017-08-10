package main;

import packet.CPacketRandom;
import carte.Carte;
import carte.ObjectSlot;
import carte.ServerSlotWrapper;
import carte.YTBSlot;

public class ServerGame extends Game
{
	public final ServerPlayerWrapper basProps, hautProps;
	public Carte suspend;
	public ServerSlotWrapper[] ytbProps = new ServerSlotWrapper[YTBSlot.NUMBER * 2], objectProps = new ServerSlotWrapper[ObjectSlot.NUMBER * 2], cimProps = new ServerSlotWrapper[2];
	public ServerGame(ServerAccount sa, ServerAccount ssa) 
	{
		super(true);
		basProps = new ServerPlayerWrapper(bas, sa);
		hautProps = new ServerPlayerWrapper(haut, ssa);
		
		for (int i=0;i<ytbSlots.length;i++)
			ytbProps[i] = new ServerSlotWrapper(ytbSlots[i], this);
		
		for (int i=0;i<objectSlots.length;i++)
			objectProps[i] = new ServerSlotWrapper(objectSlots[i], this);
		
		for (int i=0;i<cimetiere.length;i++)
			cimProps[i] = new ServerSlotWrapper(cimetiere[i], this);
		
	}
	public ServerPlayerWrapper getPlayingPlayerWrapper()
	{
		return basTurn ? basProps : hautProps;
	} 
	public ServerPlayerWrapper getNotPlayingPlayerWrapper()
	{
		return basTurn ? hautProps : basProps;
	}
	@Override
	public boolean random() 
	{
		boolean b = Math.random() < 0.5;
		new CPacketRandom(b).send(basProps.compte.socket);
		new CPacketRandom(b).send(hautProps.compte.socket);
		return b;
	}
}
