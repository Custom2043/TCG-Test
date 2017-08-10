package packet;

import gui.GuiIngame;

import java.io.IOException;

import main.Client;
import main.ServerAccount;
import util.CustomInputStream;
import util.CustomOutputStream;
import animation.AttackAnimation;
import animation.AttackPlayerAnimation;
import carte.ClientYTBWrapper;
import carte.ObjectSlot;
import carte.YTB;
import carte.YTBSlot;

public class DPacketUse extends Packet implements Packet.PacketDoubleSide
{
	public int attacking, attacked; //attacked = -1 = attack your hp, -2 = its own
	public DPacketUse(){}
	public DPacketUse(int a, int b){attacking = a; attacked = b;}
	@Override
	public void handleClient() 
	{
		if (Client.screen instanceof GuiIngame)
		{
			if (attacked == attacking)
			{
				ClientYTBWrapper y = ((GuiIngame)Client.screen).ytbProps[attacking];
				y.slot.getCarte().use();
			}
			else if (attacked >= 0)
			{
				ClientYTBWrapper y = ((GuiIngame)Client.screen).ytbProps[attacking];
				
				for (YTBSlot ss : Client.game.ytbSlots)
					if (ss.getCarte() != null)
						ss.getCarte().attack(y.slot.getCarte(), Client.game.ytbSlots[attacked].getCarte());
				
				YTB yyy = y.slot.getCarte();
				YTB yy = Client.game.ytbSlots[attacked].getCarte();
				for (ObjectSlot ss : Client.game.objectSlots)
					if (ss.getCarte() != null)
						if (yyy.alive() && yy.alive())
							ss.getCarte().attack(yyy, yy);
			
				if (y.slot.getCarte().getVie() > 0)
				{
					if (y.slot.getCarte().isNextAttackSpecial())
						y.slot.getCarte().use(Client.game.ytbSlots[attacked]);
					else
						y.movement.addAnim(new AttackAnimation(y, ((GuiIngame)Client.screen).ytbProps[attacked]));
				}
			}
			else
			{
				ClientYTBWrapper y = ((GuiIngame)Client.screen).ytbProps[attacking];
				
				for (YTBSlot ss : Client.game.ytbSlots)
					if (ss.getCarte() != null)
						ss.getCarte().attackPlayer(y.slot.getCarte(),(attacked == -2) ==  Client.game.basTurn ?  Client.game.bas : Client.game.haut);

				for (ObjectSlot ss : Client.game.objectSlots)
					if (ss.getCarte() != null)
						ss.getCarte().attackPlayer(y.slot.getCarte(), (attacked == -2) == Client.game.basTurn ? Client.game.bas : Client.game.haut);
				
				if (y.slot.getCarte().getVie() > 0)
				{
					if (y.slot.getCarte().isNextAttackSpecial())
						y.slot.getCarte().use((attacked == -2) == (Client.game.basTurn) ? Client.game.bas : Client.game.haut);
					else
						y.movement.addAnim(new AttackPlayerAnimation(y, (attacked == -2) == (Client.game.basTurn) ? Client.game.bas : Client.game.haut));
				}
			}
		}
	}

	@Override
	public void handleServer(ServerAccount compte) 
	{
		if (compte.getGame().getPlayingPlayerWrapper().compte == compte)
		{
			int iding = attacking;
			if (compte.getGame().getPlayingPlayer() != compte.getGame().bas)
				iding = YTBSlot.reverse(attacking);
			if (attacking == attacked)
			{
				if (compte.getGame().ytbSlots[iding].getCarte().canBeUseOnItself())
				{
					compte.getGame().ytbSlots[iding].getCarte().use();
					new DPacketUse(attacking, attacked).send(compte.socket);
					new DPacketUse(YTBSlot.reverse(attacking), YTBSlot.reverse(attacked)).send(compte.foe.socket);
				}
			}
			else if (attacked >= 0)
			{
				int ided = attacked;
				if (compte.getGame().getPlayingPlayer() != compte.getGame().bas)
					ided = YTBSlot.reverse(attacked);
				
				for (YTBSlot ss : compte.getGame().ytbSlots)
					if (ss.getCarte() != null)
						ss.getCarte().attack(compte.getGame().ytbSlots[iding].getCarte(), compte.getGame().ytbSlots[ided].getCarte());
				
				YTB yyy = compte.getGame().ytbSlots[iding].getCarte();
				YTB yy = compte.getGame().ytbSlots[ided].getCarte();
				for (ObjectSlot ss : compte.getGame().objectSlots)
					if (ss.getCarte() != null)
						if (yyy.alive() && yy.alive())
							ss.getCarte().attack(yyy, yy);
				
				if (compte.getGame().ytbSlots[ided].getCarte() != null && compte.getGame().ytbSlots[iding].getCarte() != null && compte.getGame().ytbSlots[iding].getCarte().canBeUseOnSlot(compte.getGame().ytbSlots[ided]))
					compte.getGame().ytbSlots[iding].getCarte().use(compte.getGame().ytbSlots[ided]);//Must use before to send random infos
				new DPacketUse(YTBSlot.reverse(attacking), YTBSlot.reverse(attacked)).send(compte.foe.socket);
				new DPacketUse(attacking, attacked).send(compte.socket);
			}
			else if (attacked == -1)
			{
				for (YTBSlot ss : compte.getGame().ytbSlots)
					if (ss.getCarte() != null)
						ss.getCarte().attackPlayer(compte.getGame().ytbSlots[iding].getCarte(), !compte.getGame().basTurn ? compte.getGame().bas : compte.getGame().haut);

				for (ObjectSlot ss : compte.getGame().objectSlots)
					if (ss.getCarte() != null)
						ss.getCarte().attackPlayer(compte.getGame().ytbSlots[iding].getCarte(), !compte.getGame().basTurn ? compte.getGame().bas : compte.getGame().haut);

				if (compte.getGame().ytbSlots[iding].getCarte() != null && compte.getGame().ytbSlots[iding].getCarte().canBeUseOnPlayer(compte.getGame().getNotPlayingPlayer()))
					compte.getGame().ytbSlots[iding].getCarte().use(compte.getGame().getNotPlayingPlayer());
				new DPacketUse(YTBSlot.reverse(attacking), attacked).send(compte.foe.socket);
				new DPacketUse(attacking, attacked).send(compte.socket);
			}
			else if (attacked == -2)
			{
				for (YTBSlot ss : compte.getGame().ytbSlots)
					if (ss.getCarte() != null)
						ss.getCarte().attackPlayer(compte.getGame().ytbSlots[iding].getCarte(), compte.getGame().basTurn ?  compte.getGame().bas : compte.getGame().haut);

				for (ObjectSlot ss : compte.getGame().objectSlots)
					if (ss.getCarte() != null)
						ss.getCarte().attackPlayer(compte.getGame().ytbSlots[iding].getCarte(), compte.getGame().basTurn ? compte.getGame().bas : compte.getGame().haut);
				
				if (compte.getGame().ytbSlots[iding].getCarte() != null && compte.getGame().ytbSlots[iding].getCarte().canBeUseOnPlayer(compte.getGame().getPlayingPlayer()))
					compte.getGame().ytbSlots[iding].getCarte().use(compte.getGame().getPlayingPlayer());
				new DPacketUse(YTBSlot.reverse(attacking), attacked).send(compte.foe.socket);
				new DPacketUse(attacking, attacked).send(compte.socket);
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
