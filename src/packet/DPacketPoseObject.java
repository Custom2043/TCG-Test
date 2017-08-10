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
import carte.ClientObjectWrapper;
import carte.Object;
import carte.ObjectSlot;
import carte.Piege;

public class DPacketPoseObject extends Packet implements Packet.PacketDoubleSide
{
	public int cartId, slotId;
	
	public DPacketPoseObject(){}
	public DPacketPoseObject(Object p, int id){cartId = CarteList.getCarteForClass(p.getClass()).id;slotId = id;}
	public DPacketPoseObject(int cartI, int id){cartId = cartI;slotId = id;}
	
	@Override
	public void handleClient() 
	{
		if (cartId <= 0) //Piege
		{
			Client.game.objectSlots[slotId].faceCache = true;
			Client.game.getPlayingPlayer().addMana(cartId);
			((GuiIngame)Client.screen).objectProps[slotId].movement.addAnim(new PoseAnimation(((GuiIngame)Client.screen).objectProps[slotId], ((GuiIngame)Client.screen).picked.getLastCardPosition().coor, null));
			((GuiIngame)Client.screen).picked = null;
		}
		else
		{
			Object y = (Object)CarteList.getCarteForID(cartId).instance(Client.game.ytbSlots[slotId].possesseur);
			ClientObjectWrapper w = ((GuiIngame)Client.screen).objectProps[slotId];
			w.movement.update();
			if (((GuiIngame)Client.screen).picked.carte == null)
			{
				w.movement.to(((GuiIngame)Client.screen).picked.getLastCardPosition(), 0);
				w.movement.update();
				w.movement.addAnim(new FlipAnimation(w));
			}
			w.movement.addAnim(new PoseAnimation(w, ((GuiIngame)Client.screen).picked.getLastCardPosition().coor, y));
			((GuiIngame)Client.screen).picked = null;
			Client.game.objectSlots[slotId].putCarte(y);
		}
	}
	@Override
	public void handleServer(ServerAccount compte)
	{
		if (compte.getGame().getPlayingPlayerWrapper().compte == compte)
		{
			int id = slotId;
			if (compte.getGame().getPlayingPlayer() != compte.getGame().bas)
				id = ObjectSlot.reverse(slotId);
			try 
			{
				Object y = (Object)(CarteList.getCarteForID(cartId).clas.newInstance());
				compte.getGame().objectSlots[id].putCarte(y);
				if (compte.getGame().objectSlots[id].getCarte() != y && compte.getGame().suspend != null)
					compte.getGame().getPlayingPlayer().addCarte(compte.getGame().suspend);
				compte.getGame().suspend = null;
				if (compte.getGame().objectSlots[id].getCarte() == y)//Pose success
				{
					if (y instanceof Piege)
						new DPacketPoseObject(-y.getCost(compte.getGame().getPlayingPlayer()), ObjectSlot.reverse(slotId)).send(compte.foe.socket);
					else
						new DPacketPoseObject(y, ObjectSlot.reverse(slotId)).send(compte.foe.socket);
					new DPacketPoseObject(y, slotId).send(compte.socket);
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
