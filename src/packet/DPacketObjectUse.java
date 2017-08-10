package packet;

import gui.GuiIngame;

import java.io.IOException;

import main.Client;
import main.ServerAccount;
import util.CustomInputStream;
import util.CustomOutputStream;
import carte.ClientObjectWrapper;
import carte.ObjectSlot;

public class DPacketObjectUse extends Packet implements Packet.PacketDoubleSide
{
	public int attacking, attacked; //attacked = -1 = attack your hp, -2 = its own
	public DPacketObjectUse(){}
	public DPacketObjectUse(int a, int b){attacking = a; attacked = b;}
	@Override
	public void handleClient() 
	{
		if (Client.screen instanceof GuiIngame)
		{
			if (attacked >= 0)
			{
				ClientObjectWrapper y = ((GuiIngame)Client.screen).objectProps[attacking];
				y.slot.getCarte().use(Client.game.ytbSlots[attacked]);
			}
			else if (attacked == -3)
			{
				ClientObjectWrapper y = ((GuiIngame)Client.screen).objectProps[attacking];
				y.slot.getCarte().use();
			}
			else
			{
				ClientObjectWrapper y = ((GuiIngame)Client.screen).objectProps[attacking];
				y.slot.getCarte().use((attacked == -2) == (Client.game.basTurn) ? Client.game.bas : Client.game.haut);
			}
		}
	}

	@Override
	public void handleServer(ServerAccount compte) 
	{
		if (compte.getGame().getPlayingPlayerWrapper().compte == compte)
		{
			int iding = attacking, ided = attacked;
			if (compte.getGame().getPlayingPlayer() != compte.getGame().bas)
				iding = ObjectSlot.reverse(attacking);
			if (attacked >= 0)
			{
				if (compte.getGame().ytbSlots[ided] != null && compte.getGame().objectSlots[iding] != null && compte.getGame().objectSlots[iding].getCarte().canBeUseOnSlot(compte.getGame().ytbSlots[ided]))
				{
					compte.getGame().objectSlots[iding].getCarte().use(compte.getGame().ytbSlots[ided]);//Must use before to send random infos
					new DPacketObjectUse(ObjectSlot.reverse(attacking), ObjectSlot.reverse(attacked)).send(compte.foe.socket);
					new DPacketObjectUse(attacking, attacked).send(compte.socket);
				}
			}
			else if (attacked == -1)
			{
				if (compte.getGame().objectSlots[iding].getCarte().canBeUseOnPlayer(compte.getGame().getNotPlayingPlayer()))
				{
					compte.getGame().objectSlots[iding].getCarte().use(compte.getGame().getNotPlayingPlayer());
					new DPacketObjectUse(ObjectSlot.reverse(attacking), attacked).send(compte.foe.socket);
					new DPacketObjectUse(attacking, attacked).send(compte.socket);
				}
			}
			else if (attacked == -2)
			{
				if (compte.getGame().objectSlots[iding].getCarte().canBeUseOnPlayer(compte.getGame().getPlayingPlayer()))
				{
					compte.getGame().objectSlots[iding].getCarte().use(compte.getGame().getPlayingPlayer());
					new DPacketObjectUse(ObjectSlot.reverse(attacking), attacked).send(compte.foe.socket);
					new DPacketObjectUse(attacking, attacked).send(compte.socket);
				}
			}
			else if (attacked == -3)
			{
				if (compte.getGame().objectSlots[iding].getCarte().canBeUseOnItself())
				{
					compte.getGame().objectSlots[iding].getCarte().use();
					new DPacketObjectUse(attacking, attacked).send(compte.socket);
					new DPacketObjectUse(ObjectSlot.reverse(attacking), attacked).send(compte.foe.socket);
				}
			}
		}
	}

	@Override
	public void read(CustomInputStream cis) throws IOException {
		attacking = cis.readInt(); attacked = cis.readInt();
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException {
		cos.writeInt(attacking);
		cos.writeInt(attacked);
	}
}
