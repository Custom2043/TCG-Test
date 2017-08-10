package packet;

import gui.GuiIngame;

import java.io.IOException;

import main.Client;
import main.ServerAccount;
import util.CardPosition;
import util.CustomInputStream;
import util.CustomOutputStream;
import util.MovementCard;
import util.ScreenCoor;

public class DPacketMovement extends Packet implements Packet.PacketDoubleSide
{
	public float x, y;public byte id;
	public DPacketMovement(){}
	public DPacketMovement(float xx, float yy, int i){x = xx;y = yy;id = (byte)i;}
	@Override
	public void handleClient() 
	{
		if (Client.screen instanceof GuiIngame)
		{
			if (id != -1)
			{
				((GuiIngame)Client.screen).picked = new MovementCard(new CardPosition(GuiIngame.retourne(ScreenCoor.screenGui(x, y, 0, 0, -744/12f, -1034/12f, 744/6f, 1034/6f)), 0, 0, 1.3f), null);
				Client.game.haut.props.removeCarte(id);
			}
			else if (((GuiIngame)Client.screen).picked != null)
				((GuiIngame)Client.screen).picked.toCancel(new CardPosition(GuiIngame.retourne(ScreenCoor.screenGui(x, y, 0, 0, -744/12f, -1034/12f, 744/6f, 1034/6f)), 0, 0, 1.3f), 100);
		}
	}

	@Override
	public void handleServer(ServerAccount compte) 
	{
		if (compte.getGame().getPlayingPlayerWrapper().compte == compte)
		{
			new DPacketMovement(x, y, id).send(compte.foe.socket);
			if (id != -1)
				compte.getGame().suspend = compte.getGame().getPlayingPlayerWrapper().main.remove(id);
		}
	}

	@Override
	public void read(CustomInputStream cis) throws IOException 
	{
		x = cis.readFloat();
		y = cis.readFloat();
		id = cis.readByte();
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException 
	{
		cos.writeFloat(x);cos.writeFloat(y); cos.writeByte(id);
	}
	public boolean toWrite(){return false;}
}
