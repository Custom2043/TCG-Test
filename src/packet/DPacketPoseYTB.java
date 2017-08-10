package packet;

import gui.GuiIngame;

import java.io.IOException;

import main.Client;
import main.ServerAccount;
import util.CustomInputStream;
import util.CustomOutputStream;
import animation.FlipAnimation;
import animation.PoseAnimation;
import carte.CarteList;
import carte.ClientYTBWrapper;
import carte.YTB;
import carte.YTBSlot;

public class DPacketPoseYTB extends Packet implements Packet.PacketDoubleSide
{
	public int cartId, slotId;
	
	public DPacketPoseYTB(){}
	public DPacketPoseYTB(YTB p, int id){cartId = CarteList.getCarteForClass(p.getClass()).id;slotId = id;}
	
	@Override
	public void handleClient() 
	{
		YTB y = (YTB)CarteList.getCarteForID(cartId).instance(Client.game.ytbSlots[slotId].possesseur);
		ClientYTBWrapper w = ((GuiIngame)Client.screen).ytbProps[slotId];
		w.movement.update();
		if (((GuiIngame)Client.screen).picked.carte == null)
		{
			w.movement.to(((GuiIngame)Client.screen).picked.getLastCardPosition(), 0);
			w.movement.update();
			w.movement.addAnim(new FlipAnimation(w));
		}
		w.movement.addAnim(new PoseAnimation(w, ((GuiIngame)Client.screen).picked.getLastCardPosition().coor, y));
		((GuiIngame)Client.screen).picked = null;
		Client.game.ytbSlots[slotId].putCarte(y);
}
	@Override
	public void handleServer(ServerAccount compte)
	{
		if (compte.getGame().getPlayingPlayerWrapper().compte == compte)
		{
			int id = slotId;
			if (compte.getGame().getPlayingPlayer() != compte.getGame().bas)
				id = YTBSlot.reverse(slotId);
			try 
			{
				YTB y = (YTB)(CarteList.getCarteForID(cartId).clas.newInstance());
				compte.getGame().ytbSlots[id].putCarte(y);
				if (compte.getGame().ytbSlots[id].getCarte() != y && compte.getGame().suspend != null)
					compte.getGame().getPlayingPlayer().addCarte(compte.getGame().suspend);
				compte.getGame().suspend = null;
				if (compte.getGame().ytbSlots[id].getCarte() == y)
				{
					new DPacketPoseYTB(y, YTBSlot.reverse(slotId)).send(compte.foe.socket);
					new DPacketPoseYTB(y, slotId).send(compte.socket);
				}
				else
				{
					new DPacketRelease().send(compte.foe.socket);
					new DPacketRelease().send(compte.socket);
				}
			} 
			catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
		}
	}
	@Override
	public void read(CustomInputStream cis) throws IOException 
	{
		cartId = cis.readInt();slotId = cis.readInt();
	}
	@Override
	public void write(CustomOutputStream cos) throws IOException 
	{
		cos.writeInt(cartId);cos.writeInt(slotId);
	}
}
