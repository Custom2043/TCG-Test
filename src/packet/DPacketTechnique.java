package packet;

import gui.GuiIngame;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import main.Client;
import main.Player;
import main.ServerAccount;
import util.CustomInputStream;
import util.CustomOutputStream;
import carte.CarteList;
import carte.Technique;

public class DPacketTechnique extends Packet implements Packet.PacketDoubleSide
{
	public int techniqueID, slotID;
	public DPacketTechnique(){slotID = -1;}
	public DPacketTechnique(Technique t){techniqueID = CarteList.getCarteForClass(t.getClass()).id;}
	public DPacketTechnique(Technique t, int s){techniqueID = CarteList.getCarteForClass(t.getClass()).id; slotID = s;}
	@Override
	public void handleClient() 
	{
		try
		{
			Technique y = (Technique)(CarteList.getCarteForID(techniqueID).clas.getConstructor(Player.class).newInstance(Client.game.getPlayingPlayer()));
			if (slotID == -1) //On itself
			{
				if (slotID == -1 && y.canBeUseOnItself())
				{
					y.use();
					((GuiIngame)Client.screen).picked = null;
				}
				else
					((GuiIngame)Client.screen).releasePicked();
			}
			else
			{
				if (slotID != -1 && Client.game.ytbSlots[slotID].getCarte() != null && y.canBeUseOnSlot(Client.game.ytbSlots[slotID]))
				{
					y.use(Client.game.ytbSlots[slotID]);
					((GuiIngame)Client.screen).picked = null;
				}
				else
					((GuiIngame)Client.screen).releasePicked();
			}
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {e.printStackTrace();}
	}

	@Override
	public void handleServer(ServerAccount compte) 
	{
		if (compte.getGame().getPlayingPlayerWrapper().compte == compte)
		{
			try
			{
				Technique y = (Technique)(CarteList.getCarteForID(techniqueID).clas.getConstructor(Player.class).newInstance(compte.getGame().getPlayingPlayer()));
				if (slotID == -1) //On itself
				{
					if (slotID == -1 && y.canBeUseOnItself())
						y.use();
					else
						if (compte.getGame().suspend != null)
							compte.getGame().getPlayingPlayer().addCarte(compte.getGame().suspend);
					compte.getGame().suspend = null;
					new DPacketTechnique(y, slotID).send(compte.foe.socket);
					new DPacketTechnique(y, slotID).send(compte.socket);
				}
				else
				{
					int id = slotID;
					if (compte.getGame().getPlayingPlayer() != compte.getGame().bas)
					{
						if (slotID < 6) id += 6;
						else			id -= 6;
					}
					if (slotID != -1 && compte.getGame().ytbSlots[id].getCarte() != null && y.canBeUseOnSlot(compte.getGame().ytbSlots[id]))
						y.use(compte.getGame().ytbSlots[id]);
					else
						if (compte.getGame().suspend != null)
							compte.getGame().getPlayingPlayer().addCarte(compte.getGame().suspend);
					compte.getGame().suspend = null;
					if (compte.getGame().getPlayingPlayer() == compte.getGame().bas)
					{
						if (slotID >= 6) id -= 6;
						if (slotID < 6) id += 6;
					}
					new DPacketTechnique(y, id).send(compte.foe.socket);
					new DPacketTechnique(y, slotID).send(compte.socket);
				}
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {e.printStackTrace();}
		}
	}

	@Override
	public void read(CustomInputStream cis) throws IOException 
	{
		techniqueID = cis.readInt();
		slotID = cis.readInt();
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException 
	{
		cos.writeInt(techniqueID);
		cos.writeInt(slotID);
	}

}
