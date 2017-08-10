package packet;

import gui.GuiIngame;

import java.io.IOException;

import main.Client;
import main.ClientPlayerWrapper;
import util.CustomInputStream;
import util.CustomOutputStream;
import carte.Carte;
import carte.CarteList;

public class CPacketPioche extends Packet implements Packet.PacketToClient
{
	int cartId;boolean ennemi, ennemiDeck;
	public CPacketPioche(){}
	public CPacketPioche(Carte c, boolean e, boolean ed)
	{
		ennemi = e; ennemiDeck = ed;
		if (!e)
			cartId = CarteList.getCarteForClass(c.getClass()).id;
	}
	
	@Override
	public void handleClient()
	{
		if (Client.screen instanceof GuiIngame)
		{
			ClientPlayerWrapper c = ennemi ? Client.game.hautProps : Client.game.basProps;
			if (!ennemiDeck)//Prend d'un allié
			{
				if (cartId == -1)
					c.addCarte((Carte)null);
				else
					c.addCarte(CarteList.getCarteForID(cartId).instance(c.player));
			}
		}
	}

	@Override
	public void read(CustomInputStream cis) throws IOException 
	{
		ennemi = cis.readBoolean();
		ennemiDeck = cis.readBoolean();
		if (!ennemi)
			cartId = cis.readInt();
		else
			cartId = -1;
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException {
		cos.writeBoolean(ennemi);
		cos.writeBoolean(ennemiDeck);
		if (!ennemi)
			cos.writeInt(cartId);
	}
}
